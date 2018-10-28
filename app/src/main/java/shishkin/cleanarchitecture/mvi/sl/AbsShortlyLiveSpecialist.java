package shishkin.cleanarchitecture.mvi.sl;

import java.util.concurrent.TimeUnit;

/**
 * Абстрактный коротко живущий специалист
 */
@SuppressWarnings("unused")
public abstract class AbsShortlyLiveSpecialist extends AbsSpecialist implements AutoCompleteHandler.OnShutdownListener {

    private AutoCompleteHandler<Boolean> mServiceHandler;
    private static final TimeUnit TIMEUNIT = TimeUnit.SECONDS;
    private static final long TIMEUNIT_DURATION = 10L;

    public AbsShortlyLiveSpecialist() {
        mServiceHandler = new AutoCompleteHandler<>("AbsShortlyLiveModule [" + getName() + "]");
        mServiceHandler.setOnShutdownListener(this);
        mServiceHandler.post(true);
        setShutdownTimeout(TIMEUNIT.toMillis(TIMEUNIT_DURATION));
    }

    public void setShutdownTimeout(final long shutdownTimeout) {
        if (shutdownTimeout > 0) {
            mServiceHandler.setShutdownTimeout(shutdownTimeout);
        }
    }

    public void post() {
        mServiceHandler.post(true);
    }

    @Override
    public void onShutdown(AutoCompleteHandler handler) {
        SL.getInstance().unregister(getName());
    }
}
