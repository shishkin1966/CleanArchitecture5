package shishkin.cleanarchitecture.mvi.sl.datasource;

import android.arch.paging.DataSource;
import android.arch.paging.PositionalDataSource;


import shishkin.cleanarchitecture.mvi.app.SLUtil;
import shishkin.cleanarchitecture.mvi.sl.usecase.DataSourceSubscriber;

public abstract class AbsPositionalDataSource<T> extends PositionalDataSource<T> implements DataSource.InvalidatedCallback, DataSourceSubscriber {

    public AbsPositionalDataSource() {
        this.addInvalidatedCallback(this);
    }

    @Override
    public void onInvalidated() {
        SLUtil.getDataSourceUnion().unregister(this);
    }
}
