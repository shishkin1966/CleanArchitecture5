package shishkin.cleanarchitecture.mvi.sl;

import java.util.concurrent.TimeUnit;


import shishkin.cleanarchitecture.mvi.common.AutoCompleteHandler;

/**
 * Абстрактное коротко живущее малое объединение
 */
@SuppressWarnings("unused")
public abstract class AbsShortlyLiveSmallUnion<T extends SpecialistSubscriber> extends AbsSmallUnion<T> implements SmallUnion<T>, AutoCompleteHandler.OnShutdownListener {

    private AutoCompleteHandler<Boolean> mServiceHandler;
    private static final TimeUnit TIMEUNIT = TimeUnit.SECONDS;
    private static final long TIMEUNIT_DURATION = 10L;

    public AbsShortlyLiveSmallUnion() {
        mServiceHandler = new AutoCompleteHandler<>("AbsShortlyLiveSmallUnion [" + getName() + "]");
        mServiceHandler.setOnShutdownListener(this);
        setShutdownTimeout(TIMEUNIT.toMillis(TIMEUNIT_DURATION));
    }

    public void setShutdownTimeout(final long shutdownTimeout) {
        if (shutdownTimeout > 0) {
            mServiceHandler.setShutdownTimeout(shutdownTimeout);
        }
    }

    @Override
    public void onUnRegisterLastSubscriber() {
        mServiceHandler.post(true);
    }

    @Override
    public void onShutdown(AutoCompleteHandler handler) {
        if (!hasSubscribers()) {
            SL.getInstance().unregister(getName());
        }
    }
}
