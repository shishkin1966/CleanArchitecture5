package shishkin.cleanarchitecture.mvi.sl;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


import shishkin.cleanarchitecture.mvi.common.utils.StringUtils;
import shishkin.cleanarchitecture.mvi.sl.data.Result;

/**
 * Абстрактное малое объединение
 */
@SuppressWarnings("unused")
public abstract class AbsSmallUnion<T extends SpecialistSubscriber> extends AbsSpecialist implements SmallUnion<T> {

    private static final String NAME = AbsSmallUnion.class.getName();

    private Secretary<WeakReference<T>> mSecretary = new Secretary();

    private void checkNullSubscriber() {
        for (Map.Entry<String, WeakReference<T>> entry : mSecretary.entrySet()) {
            if (entry.getValue() == null || entry.getValue().get() == null) {
                mSecretary.remove(entry.getKey());
            }
        }
    }

    @Override
    public void register(final T subscriber) {
        if (subscriber == null) {
            return;
        }

        if (!subscriber.validate()) {
            ErrorSpecialistImpl.getInstance().onError(NAME, "Registration not valid subscriber: " + subscriber.toString(), false);
        }

        checkNullSubscriber();

        final int cnt = mSecretary.size();

        mSecretary.put(subscriber.getName(), new WeakReference<>(subscriber));

        if (cnt == 0 && mSecretary.size() == 1) {
            onRegisterFirstSubscriber();
        }
        onAddSubscriber(subscriber);
    }

    @Override
    public void unregister(final T subscriber) {
        if (subscriber == null) {
            return;
        }

        checkNullSubscriber();

        final int cnt = mSecretary.size();
        if (mSecretary.containsKey(subscriber.getName())) {
            if (subscriber.equals(mSecretary.get(subscriber.getName()).get())) {
                mSecretary.remove(subscriber.getName());
            }
        }

        if (cnt == 1 && mSecretary.size() == 0) {
            onUnRegisterLastSubscriber();
        }
    }

    @Override
    public void onAddSubscriber(final T subscriber) {
    }

    @Override
    public void onRegisterFirstSubscriber() {
    }

    @Override
    public void onUnRegisterLastSubscriber() {
    }

    @Override
    public List<WeakReference<T>> getSubscribers() {
        checkNullSubscriber();

        return new ArrayList<>(mSecretary.values());
    }

    @Override
    public T getSubscriber(final String name) {
        checkNullSubscriber();

        if (StringUtils.isNullOrEmpty(name)) return null;

        if (!mSecretary.containsKey(name)) {
            return null;
        }

        return mSecretary.get(name).get();
    }

    @Override
    public boolean hasSubscribers() {
        checkNullSubscriber();

        return (!mSecretary.isEmpty());
    }

    @Override
    public Result<Boolean> validateExt(final String name) {
        final T subscriber = getSubscriber(name);
        if (subscriber != null) {
            return subscriber.validateExt();
        }
        return new Result<>(false);
    }

    @Override
    public boolean validate(final String name) {
        return validateExt().getData();
    }

    @Override
    public void onFinishApplication() {
    }
}
