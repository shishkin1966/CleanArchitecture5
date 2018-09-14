package shishkin.cleanarchitecture.mvi.common.recyclerview;

import android.support.v7.util.DiffUtil;


import java.util.ArrayList;
import java.util.List;

/**
 * Created by Shishkin on 20.12.2017.
 */

public abstract class AbsDiffCallback<T> extends DiffUtil.Callback {
    private final List<T> mOldItems = new ArrayList<>();
    private final List<T> mNewItems = new ArrayList<>();

    public void setItems(List<T> oldItems, List<T> newItems) {
        mOldItems.clear();
        mOldItems.addAll(oldItems);

        mNewItems.clear();
        mNewItems.addAll(newItems);
    }

    @Override
    public int getOldListSize() {
        return mOldItems.size();
    }

    @Override
    public int getNewListSize() {
        return mNewItems.size();
    }

    @Override
    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
        return areItemsTheSame(
                mOldItems.get(oldItemPosition),
                mNewItems.get(newItemPosition)
        );
    }

    public abstract boolean areItemsTheSame(T oldItem, T newItem);

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        return areContentsTheSame(
                mOldItems.get(oldItemPosition),
                mNewItems.get(newItemPosition)
        );
    }

    public abstract boolean areContentsTheSame(T oldItem, T newItem);
}
