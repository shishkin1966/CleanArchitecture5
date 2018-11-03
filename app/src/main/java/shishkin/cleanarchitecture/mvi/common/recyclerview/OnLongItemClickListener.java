package shishkin.cleanarchitecture.mvi.common.recyclerview;

import android.view.View;


import androidx.annotation.NonNull;

/**
 * Interface definition for a callback to be invoked when an item in this
 * AdapterView has been clicked and held.
 */
public interface OnLongItemClickListener<E> {

    /**
     * Callback method to be invoked when an item in this AdapterView has
     * been clicked and held.
     *
     * @param view     the view that was clicked and held(this will be a view provided by the adapter)
     * @param position the position of the data entity in the adapter.
     * @param item     the data entity that was clicked and held.
     * @return true if the callback consumed the long click, false otherwise.
     */
    boolean onLongItemClick(@NonNull final View view, final int position, final E item);

}
