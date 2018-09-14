package shishkin.cleanarchitecture.mvi.common.recyclerview.choice;

import android.os.Bundle;
import android.support.annotation.IntRange;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.View;


import shishkin.cleanarchitecture.mvi.common.collection.LongArrayList;
import shishkin.cleanarchitecture.mvi.common.recyclerview.ChoiceMode;

/**
 * {@link ChoiceMode} that allows any number of items to be chosen
 */
public class MultipleChoiceMode extends ChoiceMode {

    private static final String KEY_MULTIPLE_CHOICE_MODE = "multiple_choice_mode";
    private static final String KEY_CHECKED_ID_STATES = "checked_id_states";

    /**
     * Multiple choice mode callback.
     */
    @Nullable
    private SimpleChoiceModeListener mChoiceModeListener;

    /**
     * Running state of which IDs are currently checked.
     * If there is a value for a given key, the checked state for that ID is true.
     */
    @NonNull
    private final LongArrayList mCheckedIdStates;

    public MultipleChoiceMode() {
        super();
        mCheckedIdStates = new LongArrayList();
    }

    /**
     * Set single choice mode callback.
     */
    public void setChoiceModeListener(@Nullable final SimpleChoiceModeListener listener) {
        mChoiceModeListener = listener;
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
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isInChoiceMode() {
        return true;
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
        final int indexOf = mCheckedIdStates.indexOf(itemId);
        if (indexOf > -1) {
            mCheckedIdStates.remove(indexOf);
        }
        if (checked) {
            mCheckedIdStates.add(itemId);
        }

        if (mChoiceModeListener != null) {
            mChoiceModeListener.onItemCheckedStateChanged(itemId, checked);
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
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected boolean dispatchLongItemClick(@NonNull final View view,
                                            @IntRange(from = RecyclerView.NO_POSITION) final int position,
                                            final long itemId) {
        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected boolean dispatchItemClick(@NonNull final View view,
                                        @IntRange(from = RecyclerView.NO_POSITION) final int position,
                                        final long itemId) {
        final boolean checked = !isItemChecked(itemId);
        setItemChecked(itemId, checked);
        return true;
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

}
