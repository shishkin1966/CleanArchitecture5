package shishkin.cleanarchitecture.mvi.common.recyclerview;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

public class OnScrollListener extends RecyclerView.OnScrollListener {

    private RecyclerViewScrollListener mRecyclerViewScrollListener;

    public OnScrollListener(final RecyclerViewScrollListener listener) {
        mRecyclerViewScrollListener = listener;
    }

    @Override
    public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
        if (recyclerView == null) {
            return;
        }

        if (newState == RecyclerView.SCROLL_STATE_IDLE) {
            if (mRecyclerViewScrollListener != null) {
                mRecyclerViewScrollListener.idle(recyclerView);
            }
        } else {
            if (!(((LinearLayoutManager) recyclerView.getLayoutManager()).findLastCompletelyVisibleItemPosition() == recyclerView.getAdapter().getItemCount() - 1)) {
                mRecyclerViewScrollListener.scrolled(recyclerView);
            }
        }
    }

}
