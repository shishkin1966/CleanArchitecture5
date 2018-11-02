package shishkin.cleanarchitecture.mvi.common.recyclerview;

import androidx.annotation.NonNull;
import android.view.View;

/**
 * Interface definition for a callback to be invoked when an item in this
 * AdapterView has been clicked.
 */
public interface OnItemClickListener<E> {

    /**
     * Callback method to be invoked when an item in this AdapterView has
     * been clicked.
     *
     * @param view     the view that was clicked (this will be a view provided by the adapter)
     * @param position the position of the data entity in the adapter.
     * @param item     the data entity that was clicked.
     */
    void onItemClick(@NonNull final View view, final int position, final E item);

}
