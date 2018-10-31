package shishkin.cleanarchitecture.mvi.sl.datasource;

import android.arch.paging.DataSource;
import android.arch.paging.PositionalDataSource;

public abstract class AbsPositionalDataSource<T> extends PositionalDataSource<T> implements DataSource.InvalidatedCallback {

    public AbsPositionalDataSource() {
        this.addInvalidatedCallback(this);
    }

    @Override
    public void onInvalidated() {
    }
}
