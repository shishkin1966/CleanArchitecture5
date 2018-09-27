package shishkin.cleanarchitecture.mvi.app;

import shishkin.cleanarchitecture.mvi.app.db.MviDb;
import shishkin.cleanarchitecture.mvi.app.job.JobSpecialistImpl;
import shishkin.cleanarchitecture.mvi.app.location.LocationUnionImpl;
import shishkin.cleanarchitecture.mvi.app.net.NetProviderImpl;
import shishkin.cleanarchitecture.mvi.app.notification.NotificationSpecialistImpl;
import shishkin.cleanarchitecture.mvi.app.observe.DbObservable;
import shishkin.cleanarchitecture.mvi.app.observe.ScreenOnOffObserver;
import shishkin.cleanarchitecture.mvi.app.preference.PreferencesSpecialistImpl;
import shishkin.cleanarchitecture.mvi.sl.ApplicationSpecialistImpl;
import shishkin.cleanarchitecture.mvi.sl.SL;
import shishkin.cleanarchitecture.mvi.sl.observe.NetworkBroadcastReceiverObservable;
import shishkin.cleanarchitecture.mvi.sl.observe.ScreenBroadcastReceiverObservable;

/**
 * Created by Shishkin on 08.02.2018.
 */

public class ApplicationController extends ApplicationSpecialistImpl {
    @Override
    public boolean isKillOnFinish() {
        return true;
    }

    @Override
    public void onStart() {
        super.onStart();

        SLUtil.getDbProvider().getDb(MviDb.class, MviDb.NAME);
        SLUtil.getObservableUnion().register(new DbObservable());
        SLUtil.getObservableUnion().register(new NetworkBroadcastReceiverObservable());
        SLUtil.getObservableUnion().register(new ScreenBroadcastReceiverObservable());
        SL.getInstance().register(NotificationSpecialistImpl.NAME);
        SL.getInstance().register(LocationUnionImpl.NAME);
        SL.getInstance().register(PreferencesSpecialistImpl.NAME);
        SL.getInstance().register(NetProviderImpl.NAME);
        SL.getInstance().register(JobSpecialistImpl.NAME);
        SLUtil.register(new ScreenOnOffObserver());
    }

    @Override
    public void onFinish() {
        SLUtil.getJobSpecialist().cancelAll();
        SLUtil.getNotificationSpecialist().clear();
        SLUtil.getStorageSpecialist().onFinishApplication();

        super.onFinish();
    }

    @Override
    public void onBackgroundApplication() {
        SLUtil.getLocationUnion().stop();
    }

    @Override
    public void onResumeApplication() {
        SLUtil.getLocationUnion().start();
    }

    @Override
    public void onScreenOff() {
        super.onScreenOff();

        SLUtil.getLocationUnion().stop();
    }

    @Override
    public void onScreenOn() {
        super.onScreenOn();

        SLUtil.getLocationUnion().start();
    }


}
