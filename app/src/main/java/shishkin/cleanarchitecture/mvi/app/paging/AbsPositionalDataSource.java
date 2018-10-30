package shishkin.cleanarchitecture.mvi.app.paging;

import android.arch.paging.DataSource;
import android.arch.paging.PositionalDataSource;

public abstract class AbsPositionalDataSource<T> extends PositionalDataSource<T> implements DataSource.InvalidatedCallback {

    public AbsPositionalDataSource() {
        addInvalidatedCallback(this);
    }

    @Override
    public void onInvalidated() {
    }
}
