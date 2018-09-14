package shishkin.cleanarchitecture.mvi.common.recyclerview.choice;

import android.os.Bundle;
import android.support.annotation.IntRange;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.view.ActionMode;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;


import shishkin.cleanarchitecture.mvi.common.collection.LongArrayList;
import shishkin.cleanarchitecture.mvi.common.recyclerview.ActionModeCompat;
import shishkin.cleanarchitecture.mvi.common.recyclerview.ChoiceMode;

/**
 * {@link ChoiceMode} that allows multiple choices in a modal selection mode.
 */
public class MultipleModalChoiceMode extends ChoiceMode {

    private static final String KEY_MULTIPLE_CHOICE_MODE = "multiple_choice_mode";
    private static final String KEY_CHECKED_ID_STATES = "checked_id_states";

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
     * Running state of which IDs are currently checked.
     * If there is a value for a given key, the checked state for that ID is true.
     */
    @NonNull
    private final LongArrayList mCheckedIdStates;

    public MultipleModalChoiceMode(@NonNull final ActionModeCompat actionModeCompat,
                                   @NonNull final ModalChoiceModeListener listener) {
        super();
        mActionModeCompat = actionModeCompat;
        mActionModeCallbacks = new ActionModeCallbacks(listener);
        mCheckedIdStates = new LongArrayList();
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
        final Bundle state = (savedInstanceState != null ?
                savedInstanceState.getBundle(KEY_MULTIPLE_CHOICE_MODE) : null);
        if (state != null) {
            final LongArrayList checkedIdStates = state.getParcelable(KEY_CHECKED_ID_STATES);
            if (checkedIdStates == null) {
                throw new IllegalArgumentException("Did you put checked id states to the saved state?");
            }

            final int count = checkedIdStates.size();
            mCheckedIdStates.clear();
            for (int i = 0; i < count; i++) {
                mCheckedIdStates.add(checkedIdStates.get(i));
            }
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
        if (!mCheckedIdStates.isEmpty()) {
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
    @Override
    public int getCheckedItemCount() {
        return mCheckedIdStates.size();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isItemChecked(final long itemId) {
        return mCheckedIdStates.contains(itemId);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setItemChecked(final long itemId, final boolean checked) {
        if (checked) {
            startActionMode();
        }

        final int indexOf = mCheckedIdStates.indexOf(itemId);
        if (indexOf > -1) {
            mCheckedIdStates.remove(indexOf);
        }
        if (checked) {
            mCheckedIdStates.add(itemId);
        }

        if (mActionMode != null) {
            mActionModeCallbacks.onItemCheckedStateChanged(mActionMode, itemId, checked);
        }
        notifyItemCheckedChanged(itemId);
    }

    /**
     * Returns an unsorted {@link LongArrayList} of checked item ids.
     */
    @NonNull
    public LongArrayList getCheckedItems() {
        return mCheckedIdStates;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void clearChoices() {
        mCheckedIdStates.clear();
        notifyCheckedChanged();
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
    public void finish() {
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
            final int indexOf = mCheckedIdStates.indexOf(itemId);
            if (indexOf > -1) {
                mCheckedIdStates.remove(indexOf);
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
        final int indexOf = mCheckedIdStates.indexOf(itemId);
        if (indexOf > -1) {
            mCheckedIdStates.remove(indexOf);
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
        super.onSaveInstanceState(outState);

        final Bundle state = new Bundle();
        state.putParcelable(KEY_CHECKED_ID_STATES, new LongArrayList(mCheckedIdStates));
        outState.putBundle(KEY_MULTIPLE_CHOICE_MODE, state);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void onDetached(@NonNull final AttachInfo attachInfo) {
        super.onDetached(attachInfo);

        // It's safe to call finishActionMode here because normally {@link onSaveInstanceState} will be
        // called before detach and per-instance state will be saved.
        finish();
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
