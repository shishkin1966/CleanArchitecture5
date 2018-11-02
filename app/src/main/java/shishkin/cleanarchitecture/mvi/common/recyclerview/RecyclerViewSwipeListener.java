package shishkin.cleanarchitecture.mvi.common.recyclerview;

import androidx.recyclerview.widget.RecyclerView;

public interface RecyclerViewSwipeListener {

    void onSwiped(RecyclerView.ViewHolder viewHolder, int direction);

}
