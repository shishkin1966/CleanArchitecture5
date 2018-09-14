package shishkin.cleanarchitecture.mvi.common.recyclerview;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;

public class MoveTouchHelper extends ItemTouchHelper.SimpleCallback {
    private AbstractRecyclerViewAdapter mMovieAdapter;

    public MoveTouchHelper(AbstractRecyclerViewAdapter movieAdapter) {
        super(ItemTouchHelper.UP | ItemTouchHelper.DOWN, 0);

        mMovieAdapter = movieAdapter;
    }

    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
        mMovieAdapter.move(viewHolder.getAdapterPosition(), target.getAdapterPosition());
        return true;
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
    }

    public AbstractRecyclerViewAdapter getAdapter() {
        return mMovieAdapter;
    }

}