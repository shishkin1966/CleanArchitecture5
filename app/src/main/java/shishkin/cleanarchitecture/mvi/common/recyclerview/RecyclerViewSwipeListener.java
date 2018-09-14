package shishkin.cleanarchitecture.mvi.common.recyclerview;

import android.support.v7.widget.RecyclerView;

public interface RecyclerViewSwipeListener {

    void onSwiped(RecyclerView.ViewHolder viewHolder, int direction);

}
