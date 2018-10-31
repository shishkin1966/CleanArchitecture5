package shishkin.cleanarchitecture.mvi.sl;

import shishkin.cleanarchitecture.mvi.sl.usecase.DataSourceSubscriber;

public interface DataSourceUnion extends SmallUnion<DataSourceSubscriber> {
    void unregister(final String name);
}
