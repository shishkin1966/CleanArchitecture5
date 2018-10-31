package shishkin.cleanarchitecture.mvi.sl;


import java.lang.ref.WeakReference;

/**
 * Абстрактное объединение
 */
public abstract class AbsUnion<T extends SpecialistSubscriber> extends AbsSmallUnion<T> implements Union<T> {

    private WeakReference<T> mCurrentSubscriber;

    @Override
    public void register(final T subscriber) {
        if (subscriber == null) return;

        super.register(subscriber);

        if (subscriber != null && mCurrentSubscriber != null && mCurrentSubscriber.get() != null) {
            if (subscriber.getName().equalsIgnoreCase(mCurrentSubscriber.get().getName())) {
                mCurrentSubscriber = new WeakReference<>(subscriber);
            }
        }
    }


    @Override
    public void unregister(final T subscriber) {
        if (subscriber == null) return;

        super.unregister(subscriber);

        if (subscriber != null && mCurrentSubscriber != null && mCurrentSubscriber.get() != null) {
            if (subscriber.getName().equalsIgnoreCase(mCurrentSubscriber.get().getName())) {
                if (mCurrentSubscriber.get().equals(subscriber)) {
                    mCurrentSubscriber.clear();
                    mCurrentSubscriber = null;
                }
            }
        }
    }

    @Override
    public void setCurrentSubscriber(final T subscriber) {
        if (subscriber != null) {
            mCurrentSubscriber = new WeakReference<>(subscriber);
        }
    }

    @Override
    public T getCurrentSubscriber() {
        if (mCurrentSubscriber != null && mCurrentSubscriber.get() != null) {
            return mCurrentSubscriber.get();
        }
        return null;
    }
}
