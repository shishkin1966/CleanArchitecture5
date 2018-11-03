package shishkin.cleanarchitecture.mvi.sl;

import androidx.annotation.NonNull;
import shishkin.cleanarchitecture.mvi.sl.presenter.Presenter;

/**
 * Объединение презенторов приложения
 */
@SuppressWarnings("unused")
public class PresenterUnionImpl extends AbsUnion<Presenter>
        implements PresenterUnion {

    public static final String NAME = PresenterUnionImpl.class.getName();

    @Override
    public boolean register(final Presenter subscriber) {
        if (subscriber == null) return false;

        if (checkSubscriber(subscriber)) {
            if (subscriber.isRegister()) {
                return super.register(subscriber);
            }
            return true;
        }
        return false;
    }

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public void stop() {
        for (Presenter presenter : getSubscribers()) {
            unregister(presenter);
        }
        super.stop();
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
