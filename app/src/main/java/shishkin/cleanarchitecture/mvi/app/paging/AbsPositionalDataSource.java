package shishkin.cleanarchitecture.mvi.app.paging;

import android.arch.paging.DataSource;
import android.arch.paging.PositionalDataSource;


import shishkin.cleanarchitecture.mvi.sl.usecase.DataSourceSubscriber;

public abstract class AbsPositionalDataSource<T> extends PositionalDataSource<T> implements DataSource.InvalidatedCallback, DataSourceSubscriber {

    public AbsPositionalDataSource() {
        addInvalidatedCallback(this);
    }

    @Override
    public void onInvalidated() {
    }
}
