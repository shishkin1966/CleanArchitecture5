package shishkin.cleanarchitecture.mvi.sl;

import android.os.Bundle;
import android.support.annotation.NonNull;


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
    public void onFinishApplication() {
        for (Presenter presenter : getSubscribers()) {
            unregister(presenter);
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
