package shishkin.cleanarchitecture.mvi.sl;


import java.lang.ref.WeakReference;
import java.util.List;

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
        return getAnySubscriber();
    }

    private T getAnySubscriber() {
        final List<WeakReference<T>> list = getSubscribers();
        if (!list.isEmpty()) {
            if (list.size() == 1) {
                return list.get(0).get();
            }

            for (WeakReference<T> ref : list) {
                if (ref.get() != null && ref.get().validate()) {
                    return ref.get();
                }
            }

            return list.get(0).get();
        } else {
            ErrorSpecialistImpl.getInstance().onError(getName(), "Subscribers not found", false);
        }
        return null;
    }

}
