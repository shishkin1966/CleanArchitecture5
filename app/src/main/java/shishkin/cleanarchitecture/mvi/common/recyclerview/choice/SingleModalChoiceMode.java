package shishkin.cleanarchitecture.mvi.common.recyclerview.choice;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;


import androidx.annotation.IntRange;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.view.ActionMode;
import androidx.recyclerview.widget.RecyclerView;
import shishkin.cleanarchitecture.mvi.common.recyclerview.ActionModeCompat;
import shishkin.cleanarchitecture.mvi.common.recyclerview.ChoiceMode;

/**
 * {@link ChoiceMode} that allows single choice in a modal selection mode.
 */
public class SingleModalChoiceMode extends ChoiceMode {

    private static final String KEY_SINGLE_MODAL_CHOICE_MODE = "single_modal_choice_mode";
    private static final String KEY_CHECKED_ID = "checked_id";

    @NonNull
    private final ActionModeCompat mActionModeCompat;

    @NonNull
    private final ActionModeCallbacks mActionModeCallbacks;

    private boolean mStartOnSingleTapEnabled = false;
    private boolean mFinishActionModeOnClearEnabled = true;

    /**
     * Controls choice mode modal. Null when inactive.
     */
    @Nullable
    private ActionMode mActionMode;

    /**
     * Running state of which ID are currently checked.
     */
    private long mCheckedId = RecyclerView.NO_ID;

    public SingleModalChoiceMode(@NonNull final ActionModeCompat actionModeCompat,
                                 @NonNull final ModalChoiceModeListener listener) {
        super();
        mActionModeCompat = actionModeCompat;
        mActionModeCallbacks = new ActionModeCallbacks(listener);
    }

    /**
     * Allows to start modal choice on single tap.
     * By default long tap is required.
     */
    public void setStartOnSingleTapEnabled(final boolean enabled) {
        mStartOnSingleTapEnabled = enabled;
    }

    /**
     * Allows to stay action mode be opened when no items are checked.
     */
    public void setFinishActionModeOnClearEnabled(final boolean enabled) {
        mFinishActionModeOnClearEnabled = enabled;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void onRestoreInstanceState(@Nullable final Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        final Bundle state = (savedInstanceState != null ?
                savedInstanceState.getBundle(KEY_SINGLE_MODAL_CHOICE_MODE) : null);
        if (state != null) {
            mCheckedId = state.getLong(KEY_CHECKED_ID, RecyclerView.NO_ID);
        }

        if (isAttached()) {
            restoreActionMode();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void onAttached(@NonNull final AttachInfo attachInfo) {
        super.onAttached(attachInfo);

        final RecyclerView.Adapter<?> adapter = attachInfo.adapter;
        if (!adapter.hasStableIds()) {
            throw new IllegalStateException("Adapter should have stable ids.");
        }

        restoreActionMode();
    }

    private void restoreActionMode() {
        if (mCheckedId != RecyclerView.NO_ID) {
            startActionMode();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isInChoiceMode() {
        return (mActionMode != null);
    }

    /**
     * {@inheritDoc}
     */
    @IntRange(from = 0, to = 1)
    @Override
    public int getCheckedItemCount() {
        return (mCheckedId != RecyclerView.NO_ID ? 1 : 0);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isItemChecked(final long itemId) {
        return (mCheckedId == itemId);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setItemChecked(final long itemId, final boolean checked) {
        if (checked) {
            startActionMode();
        }

        final long checkedId = mCheckedId;
        mCheckedId = (checked ? itemId : RecyclerView.NO_ID);
        notifyItemCheckedChanged(checkedId);

        if (mActionMode != null) {
            mActionModeCallbacks.onItemCheckedStateChanged(mActionMode, itemId, checked);
        }
        notifyItemCheckedChanged(mCheckedId);
    }

    /**
     * Returns an identifier of checked item or
     * {@link RecyclerView#NO_ID} if no item is checked.
     */
    public long getCheckedItem() {
        return mCheckedId;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void clearChoices() {
        final long itemId = mCheckedId;
        mCheckedId = RecyclerView.NO_ID;
        notifyItemCheckedChanged(itemId);
    }

    /**
     * Start {@link ActionMode} for this choice mode.
     */
    public void startActionMode() {
        if (mActionMode == null) {
            mActionMode = mActionModeCompat.startActionMode(mActionModeCallbacks);
            notifyCheckedChanged();
        }
    }

    /**
     * Finish and close this action mode. The action mode's {@link ModalChoiceModeListener} will
     * have its {@link ModalChoiceModeListener#onDestroyActionMode(ActionMode)} method called.
     */
    public void finishActionMode() {
        clearChoices();
        if (finishActionModeInternal()) {
            notifyCheckedChanged();
        }
    }

    private boolean finishActionModeInternal() {
        if (mActionMode == null) {
            return false;
        }

        mActionMode.finish();
        mActionMode = null;
        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void onPostItemsChanged(@NonNull final RecyclerView.Adapter<?> adapter) {
        final int itemCount = adapter.getItemCount();
        for (int position = 0; position < itemCount; position++) {
            final long itemId = adapter.getItemId(position);
            if (mCheckedId == itemId) {
                mCheckedId = RecyclerView.NO_ID;
                return;
            }
        }

        if (getCheckedItemCount() == 0 && mFinishActionModeOnClearEnabled) {
            finishActionModeInternal();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void onPreItemRemoved(@NonNull final RecyclerView.Adapter<?> adapter,
                                    final int position) {
        final long itemId = adapter.getItemId(position);
        if (mCheckedId == itemId) {
            mCheckedId = RecyclerView.NO_ID;
        }

        if (getCheckedItemCount() == 0 && mFinishActionModeOnClearEnabled) {
            finishActionModeInternal();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected boolean dispatchLongItemClick(@NonNull final View view,
                                            @IntRange(from = RecyclerView.NO_POSITION) final int position,
                                            final long itemId) {
        setItemChecked(itemId, true);
        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected boolean dispatchItemClick(@NonNull final View view,
                                        @IntRange(from = RecyclerView.NO_POSITION) final int position,
                                        final long itemId) {
        if (mActionMode == null && mStartOnSingleTapEnabled) {
            setItemChecked(itemId, true);
            return true;

        } else if (mActionMode != null) {
            final boolean checked = !isItemChecked(itemId);
            setItemChecked(itemId, checked);
            return true;

        } else {
            return false;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void onSaveInstanceState(@NonNull final Bundle outState) {
        final Bundle state = new Bundle();
        state.putLong(KEY_CHECKED_ID, mCheckedId);
        outState.putBundle(KEY_SINGLE_MODAL_CHOICE_MODE, state);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void onDetached(@NonNull final AttachInfo attachInfo) {
        super.onDetached(attachInfo);

        // It's safe to call finishActionMode here because normally {@link onSaveInstanceState} will be
        // called before detach and per-instance state will be saved.
        finishActionMode();
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
            // Ending selection mode means deselecting everything.
            clearChoices();

            mModalChoiceModeListener.onDestroyActionMode(mode);
            mActionMode = null;
            notifyCheckedChanged();
        }

        @Override
        public void onItemCheckedStateChanged(@NonNull final ActionMode mode, final long itemId,
                                              final boolean checked) {
            mode.invalidate();
            mModalChoiceModeListener.onItemCheckedStateChanged(mode, itemId, checked);

            // If there are no items selected we no longer need the selection mode.
            if (mFinishActionModeOnClearEnabled && getCheckedItemCount() == 0) {
                mode.finish();
            }
        }

    }

}
