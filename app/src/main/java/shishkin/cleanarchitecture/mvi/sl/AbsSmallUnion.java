package shishkin.cleanarchitecture.mvi.sl;

import java.util.ArrayList;
import java.util.List;


import shishkin.cleanarchitecture.mvi.common.utils.StringUtils;
import shishkin.cleanarchitecture.mvi.sl.data.Result;
import shishkin.cleanarchitecture.mvi.sl.state.Stateable;
import shishkin.cleanarchitecture.mvi.sl.state.ViewStateObserver;

/**
 * Абстрактное малое объединение
 */
@SuppressWarnings("unused")
public abstract class AbsSmallUnion<T extends SpecialistSubscriber> extends AbsSpecialist implements SmallUnion<T> {

    private static final String NAME = AbsSmallUnion.class.getName();

    private Secretary<T> mSecretary = new RefSecretaryImpl<>();

    @Override
    public void register(final T subscriber) {
        if (subscriber == null) {
            return;
        }

        if (!subscriber.validate()) {
            ErrorSpecialistImpl.getInstance().onError(NAME, "Registration not valid subscriber: " + subscriber.toString(), false);
        }

        final int cnt = mSecretary.size();

        mSecretary.put(subscriber.getName(), subscriber);

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

        final int cnt = mSecretary.size();
        if (mSecretary.containsKey(subscriber.getName())) {
            if (subscriber.equals(mSecretary.get(subscriber.getName()))) {
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
    public List<T> getSubscribers() {
        return mSecretary.values();
    }

    @Override
    public List<T> getValidatedSubscribers() {
        final List<T> subscribers = new ArrayList<>();
        for (T subscriber : getSubscribers()) {
            if (subscriber != null && subscriber.validate()) {
                subscribers.add(subscriber);
            }
        }
        return subscribers;
    }

    @Override
    public List<T> getReadySubscribers() {
        final List<T> subscribers = new ArrayList<>();
        for (T subscriber : getSubscribers()) {
            if (subscriber != null && subscriber.validate()) {
                if (subscriber instanceof Stateable) {
                    final int state = ((Stateable) subscriber).getState();
                    if (state != ViewStateObserver.STATE_CREATE && state != ViewStateObserver.STATE_DESTROY) {
                        subscribers.add(subscriber);
                    }
                }
            }
        }
        return subscribers;
    }

    @Override
    public T getSubscriber(final String name) {
        if (StringUtils.isNullOrEmpty(name)) return null;

        if (!mSecretary.containsKey(name)) {
            return null;
        }

        return mSecretary.get(name);
    }

    @Override
    public boolean hasSubscribers() {
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
