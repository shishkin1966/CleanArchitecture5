package shishkin.cleanarchitecture.mvi.sl;

import android.support.annotation.NonNull;


import java.util.ArrayList;
import java.util.List;


import shishkin.cleanarchitecture.mvi.common.utils.StringUtils;
import shishkin.cleanarchitecture.mvi.sl.observe.Observable;

/**
 * Объединение Observable объектов
 */
public class ObservableUnionImpl extends AbsSmallUnion<ObservableSubscriber> implements ObservableUnion {

    public static final String NAME = ObservableUnionImpl.class.getName();
    private Secretary<Observable> observableSecretary = new SecretaryImpl<>();

    @Override
    public void register(final Observable observable) {
        if (observable == null) return;

        observableSecretary.put(observable.getName(), observable);
    }

    @Override
    public void unregister(String name) {
        if (StringUtils.isNullOrEmpty(name)) return;

        if (observableSecretary.containsKey(name)) {
            observableSecretary.get(name).unregister();
            observableSecretary.remove(name);
        }
    }

    @Override
    public void register(final ObservableSubscriber subscriber) {
        if (subscriber == null) return;

        super.register(subscriber);

        final List<String> list = subscriber.getObservable();
        if (list != null) {
            for (Observable observable : observableSecretary.values()) {
                if (observable != null) {
                    final String name = observable.getName();
                    if (list.contains(name)) {
                        observable.addObserver(subscriber);
                    }
                }
            }
        }
    }

    @Override
    public void unregister(final ObservableSubscriber subscriber) {
        if (subscriber == null) return;

        super.unregister(subscriber);

        final List<String> list = subscriber.getObservable();
        if (list != null) {
            for (Observable observable : observableSecretary.values()) {
                if (observable != null) {
                    if (list.contains(observable.getName())) {
                        observable.removeObserver(subscriber);
                    }
                }
            }
        }
    }

    @Override
    public void onUnRegister() {
        for (Observable observable : observableSecretary.values()) {
            if (observable != null) {
                observable.unregister();
            }
        }
        observableSecretary.clear();
    }

    @Override
    public Observable get(final String name) {
        if (StringUtils.isNullOrEmpty(name)) return null;

        if (observableSecretary.containsKey(name)) {
            return observableSecretary.get(name);
        }
        return null;
    }

    @Override
    public List<Observable> getObservables() {
        final List<Observable> list = new ArrayList<>();
        for (Observable observable : observableSecretary.values()) {
            if (observable != null) {
                list.add(observable);
            }
        }
        return list;
    }

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public int compareTo(@NonNull Object o) {
        return (ObservableUnion.class.isInstance(o)) ? 0 : 1;
    }
}
