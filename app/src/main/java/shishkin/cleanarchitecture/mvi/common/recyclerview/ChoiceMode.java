package shishkin.cleanarchitecture.mvi.common.recyclerview;

import android.os.Bundle;
import android.view.View;


import java.util.Arrays;


import androidx.annotation.IntRange;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

/**
 * Base class for any choice mode that can be used with {@link AbstractRecyclerViewAdapter}.
 */
public abstract class ChoiceMode {

    @Nullable
    private AttachInfo mAttachInfo;

    protected ChoiceMode() {
    }

    /**
     * Called from adapter when the UI component is being re-initialized
     * from a previously saved state, given here in <var>savedInstanceState</var>.
     *
     * @param savedInstanceState the data most recently supplied in {@link #onSaveInstanceState}.
     */
    protected void onRestoreInstanceState(@Nullable final Bundle savedInstanceState) {
    }

    /* package */ void onAttached(@NonNull final RecyclerView recyclerView,
                                  @NonNull final RecyclerView.Adapter<?> adapter) {
        final AttachInfo attachInfo = new AttachInfo(recyclerView, adapter);
        onAttached(attachInfo);
    }

    /**
     * Called when choice mode is attached to adapter and RecyclerView.
     *
     * @param attachInfo Registered information about RecyclerView and adapter.
     */
    protected void onAttached(@NonNull final AttachInfo attachInfo) {
        mAttachInfo = attachInfo;
    }

    /**
     * Check if this choice mode is attached to adapter and RecyclerView.
     */
    protected boolean isAttached() {
        return (mAttachInfo != null);
    }

    /**
     * Returns true if choice mode is active.
     */
    public abstract boolean isInChoiceMode();

    /**
     * Returns the number of items currently selected.
     * <p/>
     * <p>To determine the specific items that are currently selected, use one of
     * the <code>getChecked*</code> methods.
     *
     * @return The number of items currently selected
     */
    @IntRange(from = 0)
    public abstract int getCheckedItemCount();

    /**
     * Returns the checked state of the specified position.
     *
     * @param itemId The item whose checked state to return.
     * @return The item's checked state or <code>false</code>.
     */
    public abstract boolean isItemChecked(final long itemId);

    /**
     * Sets the checked state of the specified position.
     *
     * @param itemId  The item id whose checked state is to be checked.
     * @param checked The new checked state for the item.
     */
    public abstract void setItemChecked(final long itemId, final boolean checked);

    /**
     * Clears any checked items.
     */
    public abstract void clearChoices();

    protected void onPostItemsChanged(@NonNull final RecyclerView.Adapter<?> adapter) {
    }

    protected void onPreItemRemoved(@NonNull final RecyclerView.Adapter<?> adapter,
                                    final int position) {
    }

    /**
     * Handle long item click in choice mode.
     *
     * @param view     The view that was clicked.
     * @param position The position of the view in the adapter.
     * @param itemId   The item id in the adapter.
     * @return True if the choice mode consumed event, false otherwise
     */
    protected boolean dispatchLongItemClick(@NonNull final View view,
                                            @IntRange(from = RecyclerView.NO_POSITION) final int position,
                                            final long itemId) {
        return false;
    }

    /**
     * Handle item click in choice mode.
     *
     * @param view     The view that was clicked.
     * @param position The position of the view in the adapter.
     * @param itemId   The item id in the adapter.
     * @return True if the choice mode consumed event, false otherwise
     */
    protected boolean dispatchItemClick(@NonNull final View view,
                                        @IntRange(from = RecyclerView.NO_POSITION) final int position,
                                        final long itemId) {
        return false;
    }

    /**
     * Called from adapter to retrieve per-instance state before UI component being killed
     * so that the state can be restored {@link #onRestoreInstanceState}
     * (the {@link Bundle} populated by this method will be passed to).
     *
     * @param outState Bundle in which to place your saved state.
     */
    protected void onSaveInstanceState(@NonNull final Bundle outState) {
    }

    /* package */ void onDetached(@NonNull final RecyclerView recyclerView,
                                  @NonNull final RecyclerView.Adapter<?> adapter) {
        if (mAttachInfo != null && mAttachInfo.recyclerView.equals(recyclerView) &&
                mAttachInfo.adapter.equals(adapter)) {
            onDetached(mAttachInfo);
        }
    }

    /**
     * Called when RecyclerView stops observing attached adapter.
     *
     * @param attachInfo Registered information about RecyclerView and adapter.
     * @see #onAttached(AttachInfo)
     */
    protected void onDetached(@NonNull final AttachInfo attachInfo) {
        mAttachInfo = null;
    }

    protected void notifyItemCheckedChanged(@IntRange(from = RecyclerView.NO_ID) final long itemId) {
        if (mAttachInfo != null) {
            mAttachInfo.notifyItemCheckedChanged(itemId);
        }
    }

    protected void notifyCheckedChanged() {
        if (mAttachInfo != null) {
            mAttachInfo.notifyCheckedChanged();
        }
    }

    protected static class AttachInfo {

        private static final String TAG = "ChoiceMode.AttachInfo";

        public final RecyclerView recyclerView;
        public final RecyclerView.Adapter<?> adapter;

        private AttachInfo(@NonNull final RecyclerView recyclerView,
                           @NonNull final RecyclerView.Adapter<?> adapter) {
            this.recyclerView = recyclerView;
            this.adapter = adapter;
        }

        /* package */ void notifyItemCheckedChanged(@IntRange(from = RecyclerView.NO_ID) final long itemId) {
            final int position = findPositionByItemId(itemId);
            if (position > RecyclerView.NO_POSITION) {
                adapter.notifyItemChanged(position);
            }
        }

        /* package */ void notifyCheckedChanged() {
            adapter.notifyDataSetChanged();
        }

        @IntRange(from = RecyclerView.NO_POSITION)
        private int findPositionByItemId(@IntRange(from = RecyclerView.NO_ID) final long itemId) {
            if (itemId == RecyclerView.NO_ID) {
                return RecyclerView.NO_POSITION;
            }

            final RecyclerView.LayoutManager lm = recyclerView.getLayoutManager();
            if (LinearLayoutManager.class.isInstance(lm)) {
                final LinearLayoutManager llm = (LinearLayoutManager) lm;
                return findPositionByItemId(llm, itemId);

            } else if (StaggeredGridLayoutManager.class.isInstance(lm)) {
                final StaggeredGridLayoutManager sglm = (StaggeredGridLayoutManager) lm;
                return findPositionByItemId(sglm, itemId);

            } else {
                return RecyclerView.NO_POSITION;

            }
        }

        @IntRange(from = RecyclerView.NO_POSITION)
        private int findPositionByItemId(@NonNull final LinearLayoutManager llm,
                                         @IntRange(from = RecyclerView.NO_ID) final long itemId) {
            final int start = llm.findFirstVisibleItemPosition();
            final int end = llm.findLastVisibleItemPosition();
            return findPositionByItemIdInRange(start, end, itemId);
        }

        @IntRange(from = RecyclerView.NO_POSITION)
        private int findPositionByItemId(@NonNull final StaggeredGridLayoutManager sglm,
                                         @IntRange(from = RecyclerView.NO_ID) final long itemId) {
            final int[] starts = sglm.findFirstVisibleItemPositions(null);
            final int[] ends = sglm.findLastVisibleItemPositions(null);
            if (starts.length == 0 || ends.length == 0) {
                return RecyclerView.NO_POSITION;
            }

            Arrays.sort(starts);
            Arrays.sort(ends);
            final int start = starts[0];
            final int end = ends[ends.length - 1];
            return findPositionByItemIdInRange(start, end, itemId);
        }

        @IntRange(from = RecyclerView.NO_POSITION)
        private int findPositionByItemIdInRange(@IntRange(from = RecyclerView.NO_POSITION) final int start,
                                                @IntRange(from = RecyclerView.NO_POSITION) final int end,
                                                @IntRange(from = RecyclerView.NO_ID) final long itemId) {
            if (start < 0 || end >= adapter.getItemCount()) {
                return RecyclerView.NO_POSITION;
            }

            for (int position = start; position <= end; position++) {
                if (adapter.getItemId(position) == itemId) {
                    return position;
                }
            }

            return RecyclerView.NO_POSITION;
        }

    }

}