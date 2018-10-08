package shishkin.cleanarchitecture.mvi.sl.observe;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


import shishkin.cleanarchitecture.mvi.common.utils.StringUtils;
import shishkin.cleanarchitecture.mvi.sl.DbObservableSubscriber;
import shishkin.cleanarchitecture.mvi.sl.ObservableSubscriber;
import shishkin.cleanarchitecture.mvi.sl.ObservableUnion;
import shishkin.cleanarchitecture.mvi.sl.ObservableUnionImpl;
import shishkin.cleanarchitecture.mvi.sl.SL;

/**
 * Created by Shishkin on 16.12.2017.
 */

public abstract class AbsDbObservable extends AbsObservable<String> {

    private Map<String, List<String>> mTables = Collections.synchronizedMap(new ConcurrentHashMap<>());

    @Override
    public void addObserver(ObservableSubscriber subscriber) {
        if (subscriber == null) return;

        super.addObserver(subscriber);

        if (DbObservableSubscriber.class.isInstance(subscriber)) {
            final List<String> list = ((DbObservableSubscriber) subscriber).getTables();
            for (String table : list) {
                if (!mTables.containsKey(table)) {
                    mTables.put(table, new ArrayList<>());
                }
                if (!mTables.get(table).contains(subscriber.getName())) {
                    mTables.get(table).add(subscriber.getName());
                }
            }
        }
    }

    @Override
    public void removeObserver(ObservableSubscriber subscriber) {
        if (subscriber == null) return;

        super.removeObserver(subscriber);

        if (DbObservableSubscriber.class.isInstance(subscriber)) {
            for (List<String> tables : mTables.values()) {
                if (tables.contains(subscriber.getName())) {
                    tables.remove(subscriber.getName());
                }
            }
        }
    }

    @Override
    public void onChange(String object) {
        if (StringUtils.isNullOrEmpty(object)) return;

        final List<String> tableSubscribers = mTables.get(object);
        for (String name : tableSubscribers) {
            final ObservableSubscriber subscriber = ((ObservableUnion) SL.getInstance().get(ObservableUnionImpl.NAME)).getSubscriber(name);
            if (subscriber != null) {
                subscriber.onChange(object);
            }
        }
    }

    @Override
    public void register() {
    }

    @Override
    public void unregister() {
    }
}
