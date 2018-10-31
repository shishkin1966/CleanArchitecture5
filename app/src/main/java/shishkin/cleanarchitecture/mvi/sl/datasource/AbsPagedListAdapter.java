package shishkin.cleanarchitecture.mvi.sl.datasource;

import android.arch.paging.DataSource;
import android.arch.paging.PagedList;
import android.arch.paging.PagedListAdapter;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;


import shishkin.cleanarchitecture.mvi.app.SLUtil;
import shishkin.cleanarchitecture.mvi.sl.task.MainThreadExecutor;

public abstract class AbsPagedListAdapter<T, VH extends RecyclerView.ViewHolder> extends PagedListAdapter<T, VH> {

    private int pageSize = 50;

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
        return new PagedList.Builder<Integer, T>(createDataSource(), getConfig())
                .setNotifyExecutor(new MainThreadExecutor())
                .setFetchExecutor(SLUtil.getRequestSpecialist().getExecutor(this))
                .build();
    }

    public abstract String getDataSourceName();

    public PagedList.Config getConfig() {
        return new PagedList.Config.Builder()
                .setEnablePlaceholders(false)
                .setPrefetchDistance(pageSize)
                .setInitialLoadSizeHint(pageSize / 5)
                .setPageSize(pageSize)
                .build();
    }

    public void refresh() {
        invalidate();
        submitList(getPagedList());
    }

    public void invalidate() {
        this.getPagedList().getDataSource().invalidate();
    }

    public DataSource createDataSource() {
        return DataSourceFactory.create(getDataSourceName());
    }
}
