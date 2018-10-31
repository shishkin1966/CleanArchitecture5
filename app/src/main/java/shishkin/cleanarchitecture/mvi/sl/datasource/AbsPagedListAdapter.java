package shishkin.cleanarchitecture.mvi.sl.datasource;

import android.arch.paging.DataSource;
import android.arch.paging.PagedList;
import android.arch.paging.PagedListAdapter;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;


import java.util.concurrent.Executors;


import shishkin.cleanarchitecture.mvi.app.SLUtil;
import shishkin.cleanarchitecture.mvi.sl.DataSourceUnion;
import shishkin.cleanarchitecture.mvi.sl.task.MainThreadExecutor;
import shishkin.cleanarchitecture.mvi.sl.usecase.DataSourceSubscriber;

public abstract class AbsPagedListAdapter<T, VH extends RecyclerView.ViewHolder> extends PagedListAdapter<T, VH> {

    private int pageSize = 30;

    public AbsPagedListAdapter(DiffUtil.ItemCallback<T> callback) {
        super(callback);

        submitList(getPagedList());
    }

    public AbsPagedListAdapter(DiffUtil.ItemCallback<T> callback, int pageSize) {
        super(callback);

        this.pageSize = pageSize;

        submitList(getPagedList());
    }

    public PagedList<T> getPagedList() {
        final DataSourceUnion union = SLUtil.getDataSourceUnion();
        if (union != null) {
            final DataSourceSubscriber subscriber = getDataSource();
            if (subscriber != null) {
                return new PagedList.Builder<>((DataSource<Integer, T>) subscriber, getConfig())
                        .setNotifyExecutor(new MainThreadExecutor())
                        .setFetchExecutor(Executors.newSingleThreadExecutor())
                        .build();
            }
        }
        return null;
    }

    public abstract String getDataSourceName();

    public PagedList.Config getConfig() {
        return new PagedList.Config.Builder()
                .setEnablePlaceholders(false)
                .setPrefetchDistance(pageSize)
                .setInitialLoadSizeHint(pageSize / 3)
                .setPageSize(pageSize)
                .build();
    }

    public void refresh() {
        getDataSource().refresh();
        submitList(getPagedList());
    }

    public void invalidate() {
        final DataSourceUnion union = SLUtil.getDataSourceUnion();
        if (union != null) {
            if (union.hasSubscriber(getDataSourceName())) {
                getDataSource().invalidate();
            }
        }
    }

    public DataSourceSubscriber getDataSource() {
        final DataSourceUnion union = SLUtil.getDataSourceUnion();
        if (union != null) {
            if (union.hasSubscriber(getDataSourceName())) {
                return union.getSubscriber(getDataSourceName());
            } else {
                return union.createSubscriber(getDataSourceName());
            }
        }
        return null;
    }
}
