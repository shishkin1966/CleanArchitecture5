package shishkin.cleanarchitecture.mvi.common.recyclerview;

import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.ItemTouchHelper;

public class SwipeTouchHelper extends ItemTouchHelper.SimpleCallback {
    private AbstractRecyclerViewAdapter mMovieAdapter;
    private RecyclerViewSwipeListener mListener;

    public SwipeTouchHelper(AbstractRecyclerViewAdapter movieAdapter) {
        super(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT);

        mMovieAdapter = movieAdapter;
    }

    public SwipeTouchHelper(AbstractRecyclerViewAdapter movieAdapter, RecyclerViewSwipeListener listener) {
        this(movieAdapter);

        if (listener != null) {
            mListener = listener;
        }
    }

    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
        return false;
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
        if (mListener != null) {
            mListener.onSwiped(viewHolder, direction);
        } else {
            if (mMovieAdapter != null) {
                mMovieAdapter.remove(viewHolder.getAdapterPosition());
            }
        }
    }

}