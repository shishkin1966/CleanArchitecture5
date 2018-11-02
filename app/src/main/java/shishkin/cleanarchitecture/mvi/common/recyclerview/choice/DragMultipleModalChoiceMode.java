package shishkin.cleanarchitecture.mvi.common.recyclerview.choice;

import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import androidx.annotation.IntRange;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.view.ActionMode;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;


import shishkin.cleanarchitecture.mvi.R;
import shishkin.cleanarchitecture.mvi.common.collection.LongArrayList;
import shishkin.cleanarchitecture.mvi.common.recyclerview.ActionModeCompat;
import shishkin.cleanarchitecture.mvi.common.recyclerview.ChoiceMode;
import shishkin.cleanarchitecture.mvi.common.recyclerview.ObservableRecyclerView;

/**
 * {@link ChoiceMode} that allows any number of items to be chosen
 * in a modal selection mode by dragging finger from one to another item.
 * Based on https://github.com/afollestad/drag-select-recyclerview
 */
public class DragMultipleModalChoiceMode extends ChoiceMode {

    private static final String TAG = "DragChoiceMode";

    private static final int HEIGHT_UNSPECIFIED = -1;
    private static final int POSITION_UNDEFINED = -2;
    private static final long AUTO_SCROLL_DELAY = 20L;

    @NonNull
    private final MultipleModalChoiceMode mImpl;

    @NonNull
    private final Handler mHandler;

    @NonNull
    private final AutoScroller mAutoScroller = new AutoScroller();

    @NonNull
    private final RecyclerMeasureListener mRecyclerMeasureListener = new RecyclerMeasureListener();

    @NonNull
    private final RecyclerTouchInterceptor mRecyclerTouchInterceptor = new RecyclerTouchInterceptor();

    private int mLastDraggedIndex = RecyclerView.NO_POSITION;
    private int mInitialPosition;
    private boolean mDragSelectActive;
    private int mMinReached;
    private int mMaxReached;

    private boolean mAutoScrollEnabled = true;
    private int mHotspotHeight;
    private int mHotspotOffsetTop;
    private int mHotspotOffsetBottom;

    private int mDefaultHotspotHeight;
    private int mDefaultHotspotOffsetTop;
    private int mDefaultHotspotOffsetBottom;

    private int mHotspotTopBoundStart;
    private int mHotspotTopBoundEnd;
    private int mHotspotBottomBoundStart;
    private int mHotspotBottomBoundEnd;

    private int mAutoScrollVelocity;
    private boolean mInTopHotspot;
    private boolean mInBottomHotspot;

    public DragMultipleModalChoiceMode(@NonNull final ActionModeCompat actionModeCompat,
                                       @NonNull final ModalChoiceModeListener listener) {
        super();
        mImpl = new MultipleModalChoiceMode(actionModeCompat, new ActionModeCallbacks(listener));
        mHandler = new Handler(Looper.getMainLooper());
    }

    /**
     * Allows to start modal choice on single tap.
     * By default long tap is required.
     */
    public void setStartOnSingleTapEnabled(final boolean enabled) {
        mImpl.setStartOnSingleTapEnabled(enabled);
    }

    /**
     * Allows to stay action mode be opened when no items are checked.
     */
    public void setFinishActionModeOnClearEnabled(final boolean enabled) {
        mImpl.setFinishActionModeOnClearEnabled(enabled);
    }

    public void setAutoScrollEnabled(final boolean autoScrollEnabled) {
        mAutoScrollEnabled = autoScrollEnabled;
        if (isAttached()) {
            onAutoScrollEnabledChanged();
        }
    }

    private void onAutoScrollEnabledChanged() {
        if (!mAutoScrollEnabled) {
            mHotspotHeight = HEIGHT_UNSPECIFIED;
            mHotspotOffsetTop = HEIGHT_UNSPECIFIED;
            mHotspotOffsetBottom = HEIGHT_UNSPECIFIED;

        } else {
            mHotspotHeight = mDefaultHotspotHeight;
            mHotspotOffsetTop = mDefaultHotspotOffsetTop;
            mHotspotOffsetBottom = mDefaultHotspotOffsetBottom;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void onRestoreInstanceState(@Nullable final Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        mImpl.onRestoreInstanceState(savedInstanceState);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void onAttached(@NonNull final AttachInfo attachInfo) {
        super.onAttached(attachInfo);
        mImpl.onAttached(attachInfo);

        if (!ObservableRecyclerView.class.isInstance(attachInfo.recyclerView)) {
            throw new IllegalStateException("DragMultipleModalChoiceMode can be used only with ObservableRecyclerView");
        }

        final ObservableRecyclerView recyclerView = (ObservableRecyclerView) attachInfo.recyclerView;
        checkLayoutManagerOrientationVertical(recyclerView);

        final Resources resources = recyclerView.getResources();
        mDefaultHotspotHeight = resources.getDimensionPixelSize(R.dimen.recycler_view_default_hotspot_height);
        mDefaultHotspotOffsetTop = resources.getDimensionPixelSize(R.dimen.recycler_view_default_hotspot_offset_top);
        mDefaultHotspotOffsetBottom = resources.getDimensionPixelSize(R.dimen.recycler_view_default_hotspot_offset_bottom);
        onAutoScrollEnabledChanged();

        mRecyclerMeasureListener.attachToRecyclerView(recyclerView);
        mRecyclerTouchInterceptor.attachToRecyclerView(recyclerView);
        mAutoScroller.attachToRecyclerView(recyclerView);
    }

    private void checkLayoutManagerOrientationVertical(@NonNull final RecyclerView recyclerView) {
        final RecyclerView.LayoutManager lm = recyclerView.getLayoutManager();
        if (LinearLayoutManager.class.isInstance(lm)) {
            final LinearLayoutManager llm = (LinearLayoutManager) lm;
            if (llm.getOrientation() != LinearLayoutManager.VERTICAL) {
                throw new IllegalStateException("RecyclerView.LayoutManager should have vertical orientation");
            }

        } else if (StaggeredGridLayoutManager.class.isInstance(lm)) {
            final StaggeredGridLayoutManager sglm = (StaggeredGridLayoutManager) lm;
            if (sglm.getOrientation() != LinearLayoutManager.VERTICAL) {
                throw new IllegalStateException("RecyclerView.LayoutManager should have vertical orientation");
            }

        } else {
            throw new IllegalStateException("Unable to detect RecyclerView.LayoutManager orientation.");
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isInChoiceMode() {
        return mImpl.isInChoiceMode();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getCheckedItemCount() {
        return mImpl.getCheckedItemCount();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isItemChecked(final long itemId) {
        return mImpl.isItemChecked(itemId);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setItemChecked(final long itemId, final boolean checked) {
        mImpl.setItemChecked(itemId, checked);
    }

    /**
     * Returns an unsorted {@link LongArrayList} of checked item ids.
     */
    @NonNull
    public LongArrayList getCheckedItems() {
        return mImpl.getCheckedItems();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void clearChoices() {
        mImpl.clearChoices();
    }

    /**
     * Start {@link ActionMode} for this choice mode.
     */
    public void startActionMode() {
        mImpl.startActionMode();
    }

    /**
     * Finish and close this action mode. The action mode's {@link ModalChoiceModeListener} will
     * have its {@link ModalChoiceModeListener#onDestroyActionMode(ActionMode)} method called.
     */
    public void finish() {
        mImpl.finish();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void onPostItemsChanged(@NonNull final RecyclerView.Adapter<?> adapter) {
        mImpl.onPostItemsChanged(adapter);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void onPreItemRemoved(@NonNull final RecyclerView.Adapter<?> adapter,
                                    final int position) {
        mImpl.onPreItemRemoved(adapter, position);
    }

    /**
     * Callback method to be invoked when the view tree is about to be drawn. At this point, all
     * views in the tree have been measured and given a frame
     *
     * @param rv The RecyclerView
     */
    private void onRecyclerViewMeasured(@NonNull final RecyclerView rv) {
        if (mHotspotHeight > HEIGHT_UNSPECIFIED) {
            final int measuredHeight = rv.getMeasuredHeight();
            mHotspotTopBoundStart = mHotspotOffsetTop;
            mHotspotTopBoundEnd = (mHotspotOffsetTop + mHotspotHeight);
            mHotspotBottomBoundStart = (measuredHeight - mHotspotHeight - mHotspotOffsetBottom);
            mHotspotBottomBoundEnd = (measuredHeight - mHotspotOffsetBottom);
            //d("RecyclerView height = %d", measuredHeight);
            //d("Hotspot top bound = %d to %d", mHotspotTopBoundStart, mHotspotTopBoundEnd);
            //d("Hotspot bottom bound = %d to %d", mHotspotBottomBoundStart, mHotspotBottomBoundEnd);
        }
    }

    /**
     * Silently observe and/or take over touch events sent to the RecyclerView
     * before they are handled by either the RecyclerView itself or its child views.
     *
     * @param rv The RecyclerView that dispatches this event.
     * @param e  MotionEvent describing the touch event. All coordinates are in
     *           the RecyclerView's coordinate system.
     * @return true if this choice mode wishes to begin intercepting touch events, false
     * to continue with the current behavior and continue observing future events in
     * the gesture.
     */
    private boolean onRecyclerViewDispatchTouchEvent(@NonNull final RecyclerView rv,
                                                     @NonNull final MotionEvent e) {
        final RecyclerView.Adapter adapter = rv.getAdapter();
        if (adapter.getItemCount() == 0) {
            return false;
        }

        if (mDragSelectActive) {
            final int action = e.getAction();
            if (action == MotionEvent.ACTION_UP) {
                mDragSelectActive = false;
                mInTopHotspot = false;
                mInBottomHotspot = false;
                mHandler.removeCallbacks(mAutoScroller);
                return true;

            } else if (action == MotionEvent.ACTION_MOVE) {
                // Check for auto-scroll hotspot
                if (mHotspotHeight > HEIGHT_UNSPECIFIED) {
                    if (e.getY() >= mHotspotTopBoundStart && e.getY() <= mHotspotTopBoundEnd) {
                        mInBottomHotspot = false;
                        if (!mInTopHotspot) {
                            mInTopHotspot = true;
                            mHandler.removeCallbacks(mAutoScroller);
                            mHandler.postDelayed(mAutoScroller, AUTO_SCROLL_DELAY);
                        }

                        final float simulatedFactor = mHotspotTopBoundEnd - mHotspotTopBoundStart;
                        final float simulatedY = e.getY() - mHotspotTopBoundStart;
                        mAutoScrollVelocity = (int) (simulatedFactor - simulatedY) / 2;

                    } else if (e.getY() >= mHotspotBottomBoundStart && e.getY() <= mHotspotBottomBoundEnd) {
                        mInTopHotspot = false;
                        if (!mInBottomHotspot) {
                            mInBottomHotspot = true;
                            mHandler.removeCallbacks(mAutoScroller);
                            mHandler.postDelayed(mAutoScroller, AUTO_SCROLL_DELAY);
                        }

                        final float simulatedY = e.getY() + mHotspotBottomBoundEnd;
                        final float simulatedFactor = mHotspotBottomBoundStart + mHotspotBottomBoundEnd;
                        mAutoScrollVelocity = (int) (simulatedY - simulatedFactor) / 2;

                    } else if (mInTopHotspot || mInBottomHotspot) {
                        mHandler.removeCallbacks(mAutoScroller);
                        mInTopHotspot = false;
                        mInBottomHotspot = false;
                    }
                }

                // Drag selection logic
                final int itemPosition = getItemPosition(rv, e);
                if (itemPosition != POSITION_UNDEFINED && mLastDraggedIndex != itemPosition) {
                    mLastDraggedIndex = itemPosition;
                    if (mMinReached == RecyclerView.NO_POSITION) {
                        mMinReached = mLastDraggedIndex;
                    }
                    if (mMaxReached == RecyclerView.NO_POSITION) {
                        mMaxReached = mLastDraggedIndex;
                    }
                    if (mLastDraggedIndex > mMaxReached) {
                        mMaxReached = mLastDraggedIndex;
                    }
                    if (mLastDraggedIndex < mMinReached) {
                        mMinReached = mLastDraggedIndex;
                    }

                    selectRange(adapter, mInitialPosition, mLastDraggedIndex, mMinReached, mMaxReached);

                    if (mInitialPosition == mLastDraggedIndex) {
                        mMinReached = mLastDraggedIndex;
                        mMaxReached = mLastDraggedIndex;
                    }
                }
                return true;
            }
        }

        return false;
    }

    @IntRange(from = POSITION_UNDEFINED)
    private int getItemPosition(@NonNull final RecyclerView rv, @NonNull final MotionEvent e) {
        final View v = rv.findChildViewUnder(e.getX(), e.getY());
        if (v == null) {
            return POSITION_UNDEFINED;
        }

        final ViewGroup.LayoutParams lp = v.getLayoutParams();
        if (RecyclerView.LayoutParams.class.isInstance(lp)) {
            final RecyclerView.LayoutParams rvlp = (RecyclerView.LayoutParams) lp;
            return rvlp.getViewAdapterPosition();
        }

        throw new IllegalStateException("ViewHolder.itemView's layout params are not " +
                "instance of RecyclerView.LayoutParams.");
    }

    private void selectRange(@NonNull final RecyclerView.Adapter adapter,
                             final int from, final int to,
                             final int min, final int max) {
        if (from == to) {
            // Finger is back on the initial item, unselect everything else
            for (int position = min; position <= max; position++) {
                if (position == from) {
                    continue;
                }
                final long itemId = adapter.getItemId(position);
                setItemChecked(itemId, false);
            }
            return;
        }

        if (to < from) {
            // When selecting from one to previous items
            for (int position = to; position <= from; position++) {
                final long itemId = adapter.getItemId(position);
                setItemChecked(itemId, true);
            }

            if (min > -1 && min < to) {
                // Unselect items that were selected during this drag but no longer are
                for (int position = min; position < to; position++) {
                    if (position == from) {
                        continue;
                    }
                    final long itemId = adapter.getItemId(position);
                    setItemChecked(itemId, false);
                }
            }

            if (max > -1) {
                for (int position = from + 1; position <= max; position++) {
                    final long itemId = adapter.getItemId(position);
                    setItemChecked(itemId, false);
                }
            }

        } else {
            // When selecting from one to next items
            for (int position = from; position <= to; position++) {
                final long itemId = adapter.getItemId(position);
                setItemChecked(itemId, true);
            }

            if (max > -1 && max > to) {
                // Unselect items that were selected during this drag but no longer are
                for (int position = to + 1; position <= max; position++) {
                    if (position == from) {
                        continue;
                    }
                    final long itemId = adapter.getItemId(position);
                    setItemChecked(itemId, false);
                }
            }

            if (min > -1) {
                for (int position = min; position < from; position++) {
                    final long itemId = adapter.getItemId(position);
                    setItemChecked(itemId, false);
                }
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected boolean dispatchLongItemClick(@NonNull final View view,
                                            @IntRange(from = RecyclerView.NO_POSITION) final int position,
                                            @IntRange(from = RecyclerView.NO_ID) final long itemId) {
        if (mDragSelectActive) {
            return true;
        }

        if (position == RecyclerView.NO_POSITION) {
            return false;
        }

        mLastDraggedIndex = RecyclerView.NO_POSITION;
        mMinReached = RecyclerView.NO_POSITION;
        mMaxReached = RecyclerView.NO_POSITION;

        setItemChecked(itemId, true);

        mDragSelectActive = true;
        mInitialPosition = position;
        mLastDraggedIndex = position;
        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected boolean dispatchItemClick(@NonNull final View view,
                                        @IntRange(from = RecyclerView.NO_POSITION) final int position,
                                        @IntRange(from = RecyclerView.NO_ID) final long itemId) {
        return mImpl.dispatchItemClick(view, position, itemId);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void onSaveInstanceState(@NonNull final Bundle outState) {
        super.onSaveInstanceState(outState);
        mImpl.onSaveInstanceState(outState);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void onDetached(@NonNull final AttachInfo attachInfo) {
        super.onDetached(attachInfo);

        if (!ObservableRecyclerView.class.isInstance(attachInfo.recyclerView)) {
            throw new IllegalStateException("DragMultipleModalChoiceMode can be used only with ObservableRecyclerView");
        }

        final ObservableRecyclerView recyclerView = (ObservableRecyclerView) attachInfo.recyclerView;
        mRecyclerMeasureListener.detachFromRecyclerView(recyclerView);
        mRecyclerTouchInterceptor.detachFromRecyclerView(recyclerView);
        mAutoScroller.detachFromRecyclerView(recyclerView);

        mImpl.onDetached(attachInfo);
    }

    private class ActionModeCallbacks implements ModalChoiceModeListener {

        @NonNull
        private ModalChoiceModeListener mModalChoiceModeListener;

        /* package */ ActionModeCallbacks(@NonNull final ModalChoiceModeListener listener) {
            mModalChoiceModeListener = listener;
        }

        @Override
        public boolean onCreateActionMode(final ActionMode mode, final Menu menu) {
            return mModalChoiceModeListener.onCreateActionMode(mode, menu);
        }

        @Override
        public boolean onPrepareActionMode(final ActionMode mode, final Menu menu) {
            return mModalChoiceModeListener.onPrepareActionMode(mode, menu);
        }

        @Override
        public boolean onActionItemClicked(final ActionMode mode, final MenuItem item) {
            return mModalChoiceModeListener.onActionItemClicked(mode, item);
        }

        @Override
        public void onDestroyActionMode(final ActionMode mode) {
            mModalChoiceModeListener.onDestroyActionMode(mode);
            mDragSelectActive = false;
        }

        @Override
        public void onItemCheckedStateChanged(@NonNull final ActionMode mode, final long itemId,
                                              final boolean checked) {
            mModalChoiceModeListener.onItemCheckedStateChanged(mode, itemId, checked);
        }

    }

    private class RecyclerTouchInterceptor implements ObservableRecyclerView.DispatchTouchEventListener {

        /* package */ RecyclerTouchInterceptor() {
        }

        /* package */ void attachToRecyclerView(@NonNull final ObservableRecyclerView recyclerView) {
            recyclerView.addDispatchTouchEventListener(this);
        }

        @Override
        public boolean dispatchTouchEvent(@NonNull final ObservableRecyclerView rv,
                                          @NonNull final MotionEvent e) {
            return onRecyclerViewDispatchTouchEvent(rv, e);
        }

        /* package */ void detachFromRecyclerView(@NonNull final ObservableRecyclerView recyclerView) {
            recyclerView.removeDispatchTouchEventListener(this);
        }

    }

    private class RecyclerMeasureListener implements ObservableRecyclerView.OnMeasureListener {

        /* package */ RecyclerMeasureListener() {
        }

        /* package */ void attachToRecyclerView(@NonNull final ObservableRecyclerView recyclerView) {
            recyclerView.addOnMeasureListener(this);
        }

        @Override
        public void onMeasured(@NonNull final ObservableRecyclerView rv) {
            onRecyclerViewMeasured(rv);
        }

        /* package */ void detachFromRecyclerView(@NonNull final ObservableRecyclerView recyclerView) {
            recyclerView.removeOnMeasureListener(this);
        }

    }

    private class AutoScroller implements Runnable {

        @Nullable
        private RecyclerView mRecyclerView;

        /* package */ AutoScroller() {
        }

        /* package */ void attachToRecyclerView(@NonNull final RecyclerView recyclerView) {
            mRecyclerView = recyclerView;
        }

        @Override
        public void run() {
            if (mRecyclerView == null) {
                return;
            }

            if (mInTopHotspot) {
                mRecyclerView.scrollBy(0, -mAutoScrollVelocity);
                mHandler.postDelayed(this, AUTO_SCROLL_DELAY);

            } else if (mInBottomHotspot) {
                mRecyclerView.scrollBy(0, mAutoScrollVelocity);
                mHandler.postDelayed(this, AUTO_SCROLL_DELAY);
            }
        }

        /* package */ void detachFromRecyclerView(@NonNull final RecyclerView recyclerView) {
            mRecyclerView = null;
        }

    }

}
