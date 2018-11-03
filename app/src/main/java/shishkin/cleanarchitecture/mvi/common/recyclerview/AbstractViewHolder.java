package shishkin.cleanarchitecture.mvi.common.recyclerview;

import android.content.Context;
import android.content.res.Resources;
import android.os.Build;
import android.view.View;
import android.widget.Checkable;


import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Base {@link RecyclerView.ViewHolder} class that works in conjunction
 * with {@link AbstractRecyclerViewAdapter}.
 */
public abstract class AbstractViewHolder extends RecyclerView.ViewHolder {

    /* package */ interface OnViewHolderClickListener {

        void onViewHolderClick(@NonNull final View view, final int position);

        boolean onViewHolderLongClick(@NonNull final View view, final int position);

    }

    @IdRes
    public static final int DEFAULT_CLICKABLE_VIEW_ID = View.NO_ID;

    @IdRes
    public static final int[] DEFAULT_CLICKABLE_VIEW_IDS = new int[]{};

    @Nullable
    private OnViewHolderClickListener mOnViewHolderClickListener;

    private boolean mInChoiceMode;

    public AbstractViewHolder(@NonNull final View itemView) {
        super(itemView);
    }

    /**
     * Return interface to global information about an application environment.
     */
    @NonNull
    public Context getContext() {
        return itemView.getContext();
    }

    /**
     * Return a Resources instance for your application's package.
     */
    @NonNull
    public Resources getResources() {
        return itemView.getResources();
    }

    /**
     * Look for a child view in the {@link #itemView) with the given id.
     * If provided view has the given id, return this view.
     *
     * @param id The id to search for.
     * @return The view that has the given id in the hierarchy or null
     */
    @SuppressWarnings("unchecked")
    protected <V extends View> V findView(@IdRes final int id) {
        return (V) itemView.findViewById(id);
    }

    /**
     * Called by {@link AbstractRecyclerViewAdapter} to enable view and set click listener.
     */
    /* package */
    final void setEnabled(final boolean enabled, final boolean isLongClickable,
                          @IdRes final int defaultClickableViewId,
                          @IdRes final int[] clickableViewIds) {
        setClickable(enabled, isLongClickable, defaultClickableViewId);
        for (final int clickableViewId : clickableViewIds) {
            setClickable(enabled, false, clickableViewId);
        }
    }

    private void setClickable(final boolean enabled, final boolean isLongClickable,
                              @IdRes final int viewId) {
        final View clickableView;
        if (viewId == DEFAULT_CLICKABLE_VIEW_ID) {
            clickableView = itemView;
            clickableView.setOnLongClickListener(mOnLongClickListener);
        } else {
            clickableView = findView(viewId);
        }

        if (clickableView != null) {
            clickableView.setEnabled(enabled);
            clickableView.setOnClickListener(enabled ? mOnClickListener : null);
            clickableView.setClickable(enabled);
            clickableView.setOnLongClickListener(isLongClickable && enabled ? mOnLongClickListener : null);
            clickableView.setLongClickable(isLongClickable && enabled);
        }
    }

    /**
     * Called by {@link AbstractRecyclerViewAdapter} to notify
     * that view can be selected by choice mode.
     */
    /* package */
    final void setInChoiceMode(final boolean isInChoiceMode) {
        mInChoiceMode = isInChoiceMode;
    }

    /**
     * Return true if choice mode is activated.
     */
    protected boolean isInChoiceMode() {
        return mInChoiceMode;
    }

    /**
     * Called by {@link AbstractRecyclerViewAdapter} to check or activate view.
     */
    /* package */
    final void setChecked(final boolean activated) {
        if (Checkable.class.isInstance(itemView)) {
            ((Checkable) itemView).setChecked(activated);
        } else if (shouldUseActivated()) {
            itemView.setActivated(activated);
        }
    }

    /**
     * Return true if choice mode is activated and item represented by this view holder is checked.
     */
    protected boolean isChecked() {
        if (Checkable.class.isInstance(itemView)) {
            return ((Checkable) itemView).isChecked();
        } else {
            return (shouldUseActivated() && itemView.isActivated());
        }
    }

    private boolean shouldUseActivated() {
        final int targetSdkVersion = getContext().getApplicationInfo().targetSdkVersion;
        return (targetSdkVersion >= Build.VERSION_CODES.HONEYCOMB);
    }

    /**
     * Called by {@link AbstractRecyclerViewAdapter} to register on click callback.
     */
    /* package */
    final void setOnViewHolderClickListener(@Nullable final OnViewHolderClickListener l) {
        mOnViewHolderClickListener = l;
    }

    private final View.OnClickListener mOnClickListener = new View.OnClickListener() {

        @Override
        public void onClick(final View v) {
            if (mOnViewHolderClickListener != null) {
                mOnViewHolderClickListener.onViewHolderClick(v, getAdapterPosition());
            }
        }

    };

    private final View.OnLongClickListener mOnLongClickListener = new View.OnLongClickListener() {

        @Override
        public boolean onLongClick(final View v) {
            return (mOnViewHolderClickListener != null &&
                    mOnViewHolderClickListener.onViewHolderLongClick(v, getAdapterPosition()));
        }

    };

    /**
     * Called by {@link AbstractRecyclerViewAdapter} when view has been recycled.
     * <p/>
     * If an item view has large or expensive data bound to it such as large bitmaps,
     * this may be a good place to release those resources.
     */
    protected void onViewRecycled() {
    }

}