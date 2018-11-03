package shishkin.cleanarchitecture.mvi.sl.observe;

import java.util.List;


import shishkin.cleanarchitecture.mvi.sl.ObservableSubscriber;
import shishkin.cleanarchitecture.mvi.sl.RefSecretaryImpl;
import shishkin.cleanarchitecture.mvi.sl.Secretary;

/**
 * Created by Shishkin on 15.12.2017.
 */

public abstract class AbsObservable<T, K extends ObservableSubscriber> implements Observable<T, K> {
    private Secretary<K> secretary = new RefSecretaryImpl<>();

    @Override
    public void addObserver(K subscriber) {
        if (subscriber == null) return;

        secretary.put(subscriber.getName(), subscriber);

        if (secretary.size() == 1) {
            register();
        }
    }

    @Override
    public void removeObserver(K subscriber) {
        if (subscriber == null) return;

        if (secretary.containsKey(subscriber.getName())) {
            if (subscriber.equals(secretary.get(subscriber.getName()))) {
                secretary.remove(subscriber.getName());
            }

            if (secretary.isEmpty()) {
                unregister();
            }
        }
    }

    @Override
    public void onChange(T object) {
        for (ObservableSubscriber observableSubscriber : secretary.values()) {
            if (observableSubscriber.validate()) {
                observableSubscriber.onChange(object);
            }
        }
    }

    @Override
    public List<K> getObserver() {
        return secretary.values();
    }

    @Override
    public String getPasport() {
        return getName();
    }


}
