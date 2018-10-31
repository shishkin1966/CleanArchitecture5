package shishkin.cleanarchitecture.mvi.sl;

import android.support.annotation.NonNull;


import shishkin.cleanarchitecture.mvi.common.utils.StringUtils;
import shishkin.cleanarchitecture.mvi.sl.usecase.DataSourceSubscriber;

public class DataSourceUnionImpl extends AbsSmallUnion<DataSourceSubscriber> implements DataSourceUnion {

    public static final String NAME = DataSourceUnionImpl.class.getName();

    private Secretary<DataSourceSubscriber> secretary = new SecretaryImpl<>();
    private DataSourceFactory factory = new DataSourceFactory();

    @Override
    public boolean register(final DataSourceSubscriber subscriber) {
        final boolean result = super.register(subscriber);
        if (result) {
            secretary.put(subscriber.getName(), subscriber);
        }
        return result;
    }

    @Override
    public boolean unregister(final DataSourceSubscriber subscriber) {
        final boolean result = super.register(subscriber);
        if (result) {
            secretary.remove(subscriber.getName());
        }
        return result;
    }

    @Override
    public void unregister(final String name) {
        if (StringUtils.isNullOrEmpty(name)) return;

        if (secretary.containsKey(name)) {
            final DataSourceSubscriber subscriber = secretary.get(name);
            super.unregister(subscriber);
            secretary.remove(name);
        }
    }

    @Override
    public DataSourceSubscriber createSubscriber(String name) {
        return factory.create(name);
    }

    @Override
    public void stop() {
        for (DataSourceSubscriber subscriber : secretary.values()) {
            if (subscriber.validate()) {
                subscriber.invalidate();
            }
        }
        secretary.clear();
    }

    @Override
    public int compareTo(@NonNull Object o) {
        return (DataSourceUnion.class.isInstance(o)) ? 0 : 1;
    }

    @Override
    public String getName() {
        return NAME;
    }
}
