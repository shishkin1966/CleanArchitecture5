package shishkin.cleanarchitecture.mvi.common.recyclerview.choice;

import android.os.Bundle;
import android.view.View;


import androidx.annotation.IntRange;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;
import shishkin.cleanarchitecture.mvi.common.recyclerview.ChoiceMode;

/**
 * {@link ChoiceMode} that allows up to one choice.
 */
public class SingleChoiceMode extends ChoiceMode {

    private static final String KEY_SINGLE_CHOICE_MODE = "single_choice_mode";
    private static final String KEY_CHECKED_ID = "checked_id";

    /**
     * Single choice mode callback.
     */
    @Nullable
    private SimpleChoiceModeListener mChoiceModeListener;

    /**
     * Running state of which ID are currently checked.
     */
    private long mCheckedId = RecyclerView.NO_ID;

    public SingleChoiceMode() {
        super();
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
        super.onRestoreInstanceState(savedInstanceState);
        final Bundle state = (savedInstanceState != null ?
                savedInstanceState.getBundle(KEY_SINGLE_CHOICE_MODE) : null);
        if (state != null) {
            mCheckedId = state.getLong(KEY_CHECKED_ID, RecyclerView.NO_ID);
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
        final long checkedId = mCheckedId;
        mCheckedId = (checked ? itemId : RecyclerView.NO_ID);

        if (mChoiceModeListener != null) {
            mChoiceModeListener.onItemCheckedStateChanged(itemId, checked);
        }

        notifyItemCheckedChanged(checkedId);
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
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected boolean dispatchLongItemClick(@NonNull final View view,
                                            @IntRange(from = RecyclerView.NO_POSITION) final int position,
                                            @IntRange(from = RecyclerView.NO_ID) final long itemId) {
        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected boolean dispatchItemClick(@NonNull final View view,
                                        @IntRange(from = RecyclerView.NO_POSITION) final int position,
                                        @IntRange(from = RecyclerView.NO_ID) final long itemId) {
        if (isItemChecked(itemId)) {
            // Selection already here - just consume click
            return true;
        }

        setItemChecked(itemId, true);
        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void onSaveInstanceState(@NonNull final Bundle outState) {
        super.onSaveInstanceState(outState);

        final Bundle state = new Bundle();
        state.putLong(KEY_CHECKED_ID, mCheckedId);
        outState.putBundle(KEY_SINGLE_CHOICE_MODE, state);
    }

}
