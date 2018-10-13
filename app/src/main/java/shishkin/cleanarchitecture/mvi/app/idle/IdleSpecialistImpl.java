package shishkin.cleanarchitecture.mvi.app.idle;

import android.support.annotation.NonNull;


import java.util.concurrent.TimeUnit;


import shishkin.cleanarchitecture.mvi.R;
import shishkin.cleanarchitecture.mvi.app.ApplicationController;
import shishkin.cleanarchitecture.mvi.app.SLUtil;
import shishkin.cleanarchitecture.mvi.common.utils.ApplicationUtils;
import shishkin.cleanarchitecture.mvi.sl.AbsSpecialist;
import shishkin.cleanarchitecture.mvi.sl.AutoCompleteHandler;

public class IdleSpecialistImpl extends AbsSpecialist implements IdleSpecialist, AutoCompleteHandler.OnShutdownListener {

    public static final String NAME = IdleSpecialistImpl.class.getName();

    private long timeout = TimeUnit.MINUTES.toMillis(10);
    private AutoCompleteHandler handler;
    private long currentTime = System.currentTimeMillis();

    @Override
    public void onRegister() {
        handler = new AutoCompleteHandler<>(NAME);
        handler.setOnShutdownListener(this);
        handler.setShutdownTimeout(timeout);
        handler.post(true);
    }

    @Override
    public int compareTo(@NonNull Object o) {
        return (IdleSpecialist.class.isInstance(o)) ? 0 : 1;
    }

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public long getCurrentTime() {
        return currentTime;
    }

    @Override
    public void onUserInteraction() {
        if (System.currentTimeMillis() - currentTime > timeout) {
            onShutdown(handler);
        } else {
            currentTime = System.currentTimeMillis();
            handler.post(true);
        }
    }

    @Override
    public void onShutdown(AutoCompleteHandler handler) {
        ApplicationUtils.runOnUiThread(() -> {
            SLUtil.getNotificationSpecialist().replaceMessage(SLUtil.getContext().getString(R.string.app_name), SLUtil.getContext().getString(R.string.timeout_exit));
            ApplicationUtils.runOnUiThread(() -> ApplicationController.getInstance().finish());
        });
    }

    @Override
    public void setTimeout(final long timeout) {
        if (timeout > 0) {
            handler.setShutdownTimeout(timeout);
        }
    }

    @Override
    public long getTimeout() {
        return timeout;
    }

}