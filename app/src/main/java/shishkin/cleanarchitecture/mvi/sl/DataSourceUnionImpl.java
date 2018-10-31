package shishkin.cleanarchitecture.mvi.sl;

import android.support.annotation.NonNull;


import shishkin.cleanarchitecture.mvi.sl.usecase.DataSourceSubscriber;

public class DataSourceUnionImpl extends AbsSmallUnion<DataSourceSubscriber> implements DataSourceUnion {

    public static final String NAME = DataSourceUnionImpl.class.getName();

    private DataSourceFactory factory = new DataSourceFactory();

    @Override
    public Secretary createSecretary() {
        return new SecretaryImpl<>();
    }

    @Override
    public DataSourceSubscriber createSubscriber(String name) {
        final DataSourceSubscriber subscriber = factory.create(name);
        register(subscriber);
        return subscriber;
    }

    @Override
    public void stop() {
        for (DataSourceSubscriber subscriber : getSubscribers()) {
            if (subscriber.validate()) {
                subscriber.invalidate();
            }
        }
        super.stop();
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
