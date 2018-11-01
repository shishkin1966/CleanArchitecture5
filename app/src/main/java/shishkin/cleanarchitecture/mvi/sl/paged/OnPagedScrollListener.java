package shishkin.cleanarchitecture.mvi.sl.paged;

import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

public class OnPagedScrollListener extends RecyclerView.OnScrollListener {

    private LinearLayoutManager layoutManager;
    private Paginator paginator;
    private int prefetch = 50;

    public OnPagedScrollListener(@NonNull final LinearLayoutManager layoutManager, @NonNull final Paginator paginator) {
        this.layoutManager = layoutManager;
        this.paginator = paginator;
        prefetch = paginator.getPrefetchSize();

        paginator.hasData();
    }

    public OnPagedScrollListener(@NonNull final LinearLayoutManager layoutManager, @NonNull final Paginator paginator, int prefetch) {
        this.layoutManager = layoutManager;
        this.paginator = paginator;
        if (prefetch > 0) {
            this.prefetch = prefetch;
        }

        paginator.hasData();
    }

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        final int visibleItemCount = layoutManager.getChildCount();
        final int totalItemCount = layoutManager.getItemCount();
        final int firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition();
        final int lastVisibleItemPosition = firstVisibleItemPosition + visibleItemCount;
        if ((lastVisibleItemPosition + prefetch) >= totalItemCount) {
            paginator.hasData();
            return;
        }
        if (totalItemCount == visibleItemCount) {
            paginator.hasData();
            return;
        }
    }
}
