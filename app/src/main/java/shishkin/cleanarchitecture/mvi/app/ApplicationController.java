package shishkin.cleanarchitecture.mvi.app;

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Intent;


import es.dmoral.toasty.Toasty;
import shishkin.cleanarchitecture.mvi.R;
import shishkin.cleanarchitecture.mvi.app.db.MviDb;
import shishkin.cleanarchitecture.mvi.app.net.NetCbProviderImpl;
import shishkin.cleanarchitecture.mvi.app.net.NetProviderImpl;
import shishkin.cleanarchitecture.mvi.app.observe.AccountObserver;
import shishkin.cleanarchitecture.mvi.app.observe.DbObservable;
import shishkin.cleanarchitecture.mvi.app.observe.ScreenOnOffObserver;
import shishkin.cleanarchitecture.mvi.app.setting.Setting;
import shishkin.cleanarchitecture.mvi.app.setting.SettingFactory;
import shishkin.cleanarchitecture.mvi.app.setting.SettingPlayMusicEnabled;
import shishkin.cleanarchitecture.mvi.app.specialist.calculation.CalculationUnionImpl;
import shishkin.cleanarchitecture.mvi.app.specialist.idle.IdleSpecialistImpl;
import shishkin.cleanarchitecture.mvi.app.specialist.job.JobSpecialistImpl;
import shishkin.cleanarchitecture.mvi.app.specialist.location.LocationUnionImpl;
import shishkin.cleanarchitecture.mvi.app.specialist.media.MediaSpecialistImpl;
import shishkin.cleanarchitecture.mvi.app.specialist.notification.NotificationSpecialistImpl;
import shishkin.cleanarchitecture.mvi.app.specialist.repository.RepositoryImpl;
import shishkin.cleanarchitecture.mvi.app.specialist.scanner.ScannerUnionImpl;
import shishkin.cleanarchitecture.mvi.common.utils.ViewUtils;
import shishkin.cleanarchitecture.mvi.sl.ApplicationSpecialistImpl;
import shishkin.cleanarchitecture.mvi.sl.DataSourceUnionImpl;
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

        Toasty.Config.getInstance()
                .setTextSize(ViewUtils.getDimensionSp(this, R.dimen.text_size_large))
                .apply();

        SLUtil.getDbProvider().getDb(MviDb.class, MviDb.NAME);
        SLUtil.register(RepositoryImpl.NAME);
        SLUtil.getObservableUnion().register(new DbObservable());
        SLUtil.getObservableUnion().register(new NetworkBroadcastReceiverObservable());
        SLUtil.getObservableUnion().register(new ScreenBroadcastReceiverObservable());
        SLUtil.register(NotificationSpecialistImpl.NAME);
        SLUtil.register(LocationUnionImpl.NAME);
        SLUtil.register(NetProviderImpl.NAME);
        SLUtil.register(NetCbProviderImpl.NAME);
        SLUtil.register(JobSpecialistImpl.NAME);
        SLUtil.register(MediaSpecialistImpl.NAME);
        SLUtil.register(ScannerUnionImpl.NAME);
        SLUtil.register(IdleSpecialistImpl.NAME);
        SLUtil.register(CalculationUnionImpl.NAME);
        SLUtil.register(DataSourceUnionImpl.NAME);

        SLUtil.register(ScreenOnOffObserver.getInstance());
        SLUtil.register(AccountObserver.getInstance());

        final Setting setting = SettingFactory.getSetting(SettingPlayMusicEnabled.NAME);
        if (setting.getCurrentValue().equalsIgnoreCase("true")) {
            SLUtil.getMediaSpecialist().play(R.raw.music);
        }
    }

    @Override
    public void onStop() {
        SLUtil.getDataSourceUnion().stop();
        SLUtil.getIdleSpecialist().stop();
        SLUtil.getJobSpecialist().stop();
        SLUtil.getCacheSpecialist().stop();
        SLUtil.getMediaSpecialist().release();

        super.onStop();
    }

    @Override
    public void onBackgroundApplication() {
        SLUtil.getLocationUnion().stop();
    }

    @Override
    public void onResumeApplication() {
        if (System.currentTimeMillis() - SLUtil.getIdleSpecialist().getCurrentTime() > SLUtil.getIdleSpecialist().getTimeout()) {
            SLUtil.getIdleSpecialist().onUserInteraction();
        }
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
