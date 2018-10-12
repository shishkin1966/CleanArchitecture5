package shishkin.cleanarchitecture.mvi.app.idle;

import android.support.annotation.NonNull;


import java.util.concurrent.TimeUnit;


import shishkin.cleanarchitecture.mvi.app.ApplicationController;
import shishkin.cleanarchitecture.mvi.common.utils.ApplicationUtils;
import shishkin.cleanarchitecture.mvi.sl.AbsSpecialist;
import shishkin.cleanarchitecture.mvi.sl.AutoCompleteHandler;

public class IdleSpecialistImpl extends AbsSpecialist implements IdleSpecialist, AutoCompleteHandler.OnShutdownListener {

    public static final String NAME = IdleSpecialistImpl.class.getName();

    private long timeout = TimeUnit.MINUTES.toMillis(5);
    private AutoCompleteHandler handler;

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
    public void onUserInteraction() {
        handler.post(true);
    }

    @Override
    public void onShutdown(AutoCompleteHandler handler) {
        ApplicationUtils.runOnUiThread(() -> ApplicationController.getInstance().finish());
    }

    @Override
    public void setTimeout(final long timeout) {
        if (timeout > 0) {
            handler.setShutdownTimeout(timeout);
        }
    }

}
