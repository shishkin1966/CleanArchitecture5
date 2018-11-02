package shishkin.cleanarchitecture.mvi.common.recyclerview;

import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by Shishkin on 06.12.2017.
 */

public interface RecyclerViewScrollListener {

    void idle(RecyclerView recyclerView);

    void scrolled(RecyclerView recyclerView);
}
