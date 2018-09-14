package shishkin.cleanarchitecture.mvi.sl;

import android.os.Bundle;
import android.support.annotation.NonNull;


import java.lang.ref.WeakReference;
import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


import shishkin.cleanarchitecture.mvi.sl.presenter.Presenter;

/**
 * Объединение презенторов приложения
 */
@SuppressWarnings("unused")
public class PresenterUnionImpl extends AbsUnion<Presenter>
        implements PresenterUnion {

    public static final String NAME = PresenterUnionImpl.class.getName();

    private Map<String, Bundle> mStates = Collections.synchronizedMap(new ConcurrentHashMap<String, Bundle>());

    public PresenterUnionImpl() {
        super();
    }

    @Override
    public void register(final Presenter subscriber) {
        if (subscriber != null && subscriber.isRegister()) {
            super.register(subscriber);
        }
    }

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public void saveStateData(final Presenter presenter, final Bundle state) {
        if (state != null && !ApplicationSpecialistImpl.getInstance().isFinished()) {
            mStates.put(presenter.getName(), state);
        }
    }

    @Override
    public Bundle restoreStateData(final Presenter presenter) {
        return mStates.get(presenter.getName());
    }

    @Override
    public void clearStateData(final Presenter presenter) {
        mStates.remove(presenter.getName());
    }

    @Override
    public void clearStateData() {
        mStates.clear();
    }

    @Override
    public void onUnRegisterLastSubscriber() {
        if (ApplicationSpecialistImpl.getInstance().isFinished()) {
            clearStateData();
        }
    }

    @Override
    public void onFinishApplication() {
        for (WeakReference<Presenter> ref : getSubscribers()) {
            unregister(ref.get());
        }
    }

    @Override
    public <C> C getPresenter(final String name) {
        return (C) getSubscriber(name);
    }

    @Override
    public int compareTo(@NonNull Object o) {
        return (PresenterUnion.class.isInstance(o)) ? 0 : 1;
    }

}
