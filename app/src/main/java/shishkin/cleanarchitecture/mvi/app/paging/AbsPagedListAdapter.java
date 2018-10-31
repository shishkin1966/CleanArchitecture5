package shishkin.cleanarchitecture.mvi.app.paging;

import android.arch.paging.DataSource;
import android.arch.paging.PagedList;
import android.arch.paging.PagedListAdapter;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;


import java.util.concurrent.Executors;


import shishkin.cleanarchitecture.mvi.sl.task.MainThreadExecutor;

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
        return new PagedList.Builder<>(getDataSource(), getConfig())
                .setNotifyExecutor(new MainThreadExecutor())
                .setFetchExecutor(Executors.newSingleThreadExecutor())
                .build();
    }

    public abstract DataSource<Integer, T> getDataSource();

    public PagedList.Config getConfig() {
        return new PagedList.Config.Builder()
                .setEnablePlaceholders(false)
                .setPrefetchDistance(pageSize)
                .setInitialLoadSizeHint(pageSize / 3)
                .setPageSize(pageSize)
                .build();
    }

    public void invalidate() {
        getDataSource().invalidate();
    }

    public void refresh() {
        getDataSource().invalidate();
        submitList(getPagedList());
    }
}
