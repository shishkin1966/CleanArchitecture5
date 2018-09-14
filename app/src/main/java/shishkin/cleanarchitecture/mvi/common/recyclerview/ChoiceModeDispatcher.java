package shishkin.cleanarchitecture.mvi.common.recyclerview;

import android.os.Bundle;
import android.support.annotation.IntRange;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.View;


import java.lang.ref.WeakReference;


import shishkin.cleanarchitecture.mvi.common.recyclerview.choice.NoneChoiceMode;

/**
 * Choice mode lifecycle events dispatcher.
 */
/* package */ class ChoiceModeDispatcher {

    private static final NoneChoiceMode DEFAULT_CHOICE_MODE = new NoneChoiceMode();

    @NonNull
    private final RecyclerView.Adapter<?> mAdapter;

    @Nullable
    private WeakReference<RecyclerView> mRecyclerView;

    @NonNull
    private ChoiceMode mChoiceMode = DEFAULT_CHOICE_MODE;

    /* package */ ChoiceModeDispatcher(@NonNull final RecyclerView.Adapter<?> adapter) {
        mAdapter = adapter;
    }

    /* package */ void setChoiceMode(@Nullable final ChoiceMode choiceMode) {
        if (mRecyclerView != null && mRecyclerView.get() != null) {
            mChoiceMode.onDetached(mRecyclerView.get(), mAdapter);
        }

        mChoiceMode = (choiceMode != null ? choiceMode : DEFAULT_CHOICE_MODE);
        if (mRecyclerView != null) {
            mChoiceMode.onAttached(mRecyclerView.get(), mAdapter);
        }
    }

    @NonNull
        /* package */ ChoiceMode getChoiceMode() {
        return mChoiceMode;
    }

    /* package */ void dispatchRestoreInstanceState(@Nullable final Bundle savedInstanceState) {
        mChoiceMode.onRestoreInstanceState(savedInstanceState);
    }

    /* package */ void dispatchAttachedToRecyclerView(@NonNull final RecyclerView recyclerView) {
        if (mRecyclerView == null) {
            mRecyclerView = new WeakReference<RecyclerView>(recyclerView);
            mChoiceMode.onAttached(mRecyclerView.get(), mAdapter);
        } else {
            throw new IllegalStateException("Did you try to use the same adapter " +
                    "instance in two different RecyclerViews?");
        }
    }

    /* package */ void dispatchPostItemsChanged() {
        mChoiceMode.onPostItemsChanged(mAdapter);
    }

    /* package */ void dispatchPreItemRemoved(final int position) {
        mChoiceMode.onPreItemRemoved(mAdapter, position);
    }

    /* package */ boolean dispatchLongItemClick(@NonNull final View view,
                                                @IntRange(from = RecyclerView.NO_POSITION) final int position,
                                                @IntRange(from = RecyclerView.NO_ID) final long itemId) {
        return mChoiceMode.dispatchLongItemClick(view, position, itemId);
    }

    /* package */ boolean dispatchItemClick(@NonNull final View view,
                                            @IntRange(from = RecyclerView.NO_POSITION) final int position,
                                            @IntRange(from = RecyclerView.NO_ID) final long itemId) {
        return mChoiceMode.dispatchItemClick(view, position, itemId);
    }

    /* package */ void dispatchSaveInstanceState(@NonNull final Bundle outState) {
        mChoiceMode.onSaveInstanceState(outState);
    }

    /* package */ void dispatchDetachedFromRecyclerView(@NonNull final RecyclerView recyclerView) {
        if (mRecyclerView != null && mRecyclerView.get() != null && mRecyclerView.get() == recyclerView) {
            mChoiceMode.onDetached(mRecyclerView.get(), mAdapter);
        }
    }

}