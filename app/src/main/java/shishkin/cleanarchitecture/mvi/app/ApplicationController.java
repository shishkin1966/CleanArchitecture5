package shishkin.cleanarchitecture.mvi.app;

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Intent;


import shishkin.cleanarchitecture.mvi.R;
import shishkin.cleanarchitecture.mvi.app.db.MviDb;
import shishkin.cleanarchitecture.mvi.app.job.JobSpecialistImpl;
import shishkin.cleanarchitecture.mvi.app.location.LocationUnionImpl;
import shishkin.cleanarchitecture.mvi.app.media.MediaSpecialistImpl;
import shishkin.cleanarchitecture.mvi.app.net.NetProviderImpl;
import shishkin.cleanarchitecture.mvi.app.notification.NotificationSpecialistImpl;
import shishkin.cleanarchitecture.mvi.app.observe.AccountObserver;
import shishkin.cleanarchitecture.mvi.app.observe.DbObservable;
import shishkin.cleanarchitecture.mvi.app.observe.ScreenOnOffObserver;
import shishkin.cleanarchitecture.mvi.app.preference.PreferencesSpecialistImpl;
import shishkin.cleanarchitecture.mvi.app.storage.CacheSpecialistImpl;
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
        SL.getInstance().register(CacheSpecialistImpl.NAME);
        SL.getInstance().register(NotificationSpecialistImpl.NAME);
        SL.getInstance().register(LocationUnionImpl.NAME);
        SL.getInstance().register(PreferencesSpecialistImpl.NAME);
        SL.getInstance().register(NetProviderImpl.NAME);
        SL.getInstance().register(JobSpecialistImpl.NAME);
        SL.getInstance().register(MediaSpecialistImpl.NAME);

        SLUtil.register(ScreenOnOffObserver.getInstance());
        SLUtil.register(AccountObserver.getInstance());

        SLUtil.getMediaSpecialist().play(R.raw.music);
    }


    @Override
    public void onFinish() {
        SLUtil.getJobSpecialist().cancel();
        SLUtil.getNotificationSpecialist().clear();
        SLUtil.getCacheSpecialist().clear();
        SLUtil.getMediaSpecialist().release();

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

    public void updateWidget() {
        final Intent intent = new Intent(getInstance(), ApplicationWidget.class);
        intent.setAction("android.appwidget.action.APPWIDGET_UPDATE");
        final int ids[] = AppWidgetManager.getInstance(getInstance()).getAppWidgetIds(new ComponentName(getInstance(), ApplicationWidget.class));
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, ids);
        getInstance().sendBroadcast(intent);
    }

}
