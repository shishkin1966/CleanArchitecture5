package shishkin.cleanarchitecture.mvi.common.recyclerview;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.SoundEffectConstants;
import android.view.View;
import android.view.ViewGroup;
import android.view.accessibility.AccessibilityEvent;


import java.util.ArrayList;
import java.util.List;


import shishkin.cleanarchitecture.mvi.common.recyclerview.choice.NoneChoiceMode;

/**
 * Base adapter class that simplifies working with {@link RecyclerView}.
 * Class provides ability to animate data changes, handle clicks on items.
 */
public abstract class AbstractRecyclerViewAdapter<E, VH extends AbstractViewHolder>
        extends RecyclerView.Adapter<VH> implements AbstractViewHolder.OnViewHolderClickListener {

    @NonNull
    private final LayoutInflater mLayoutInflater;

    @Nullable
    private OnItemClickListener<E> mOnItemClickListener;

    @Nullable
    private OnLongItemClickListener<E> mOnLongItemClickListener;

    @NonNull
    private final List<E> mItems = new ArrayList<>();

    @NonNull
    private ChoiceModeDispatcher mChoiceModeDispatcher;

    public AbstractRecyclerViewAdapter(@NonNull final Context context) {
        super();

        mLayoutInflater = LayoutInflater.from(context);
        mChoiceModeDispatcher = new ChoiceModeDispatcher(this);
    }


    /**
     * Register a callback to be invoked when an item in this adapter has
     * been clicked.
     *
     * @param onItemClickListener the callback that will be invoked.
     */
    public void setOnItemClickListener(@Nullable final OnItemClickListener<E> onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }

    /**
     * Register a callback to be invoked when an item in this adapter has
     * been long clicked.
     *
     * @param onLongItemClickListener the callback that will be invoked.
     */
    public void setOnLongItemClickListener(@Nullable final OnLongItemClickListener<E> onLongItemClickListener) {
        mOnLongItemClickListener = onLongItemClickListener;
    }

    /**
     * Defines the choice behavior for the adapter. By default, adapters do not have any choice behavior
     * ({@link NoneChoiceMode}). By setting the choiceMode to {@link NoneChoiceMode}, the
     * adapter allows up to one item to be in a chosen state. By setting the choiceMode to
     * {link MultipleModalChoiceMode}, the adapter allows any number of items to be chosen.
     *
     * @param choiceMode One of {@link NoneChoiceMode}, or {link MultipleModalChoiceMode},
     *                   or {link SingleModalChoiceMode}.
     */
    public void setChoiceMode(@NonNull final ChoiceMode choiceMode) {
        mChoiceModeDispatcher.setChoiceMode(choiceMode);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onAttachedToRecyclerView(final RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        mChoiceModeDispatcher.dispatchAttachedToRecyclerView(recyclerView);
    }

    /**
     * This method should be called when the activity or fragment is
     * being re-initialized from a previously saved state, given here in
     * <var>savedInstanceState</var>.
     *
     * @param savedInstanceState the data most recently supplied in {@link #onSaveInstanceState}.
     * @see android.app.Activity#onCreate(Bundle)
     * @see android.app.Activity#onRestoreInstanceState(Bundle)
     * @see android.support.v4.app.Fragment#onCreateView(LayoutInflater, ViewGroup, Bundle)
     * @see android.support.v4.app.Fragment#onViewCreated(View, Bundle)
     * @see android.support.v4.app.Fragment#onViewStateRestored(Bundle)
     */
    public void onRestoreInstanceState(@Nullable final Bundle savedInstanceState) {
        mChoiceModeDispatcher.dispatchRestoreInstanceState(savedInstanceState);
    }

    /**
     * Should be called to retrieve per-instance state from an activity or fragment
     * before being killed so that the state can be restored {@link #onRestoreInstanceState}
     * (the {@link Bundle} populated by this method will be passed to).
     * <p>
     * <p>This method is called before an activity or fragment may be killed so that when it
     * comes back some time in the future it can restore its state. For example,
     * if activity B is launched in front of activity A, and at some point activity
     * A is killed to reclaim resources, activity A will have a chance to save the
     * current state of its user interface via this method so that when the user
     * returns to activity A, the state of the user interface can be restored
     * via {@link #onRestoreInstanceState}.
     *
     * @param outState Bundle in which to place your saved state.
     * @see android.app.Activity#onSaveInstanceState(Bundle)
     * @see android.support.v4.app.Fragment#onSaveInstanceState(Bundle)
     */
    public void onSaveInstanceState(@NonNull final Bundle outState) {
        mChoiceModeDispatcher.dispatchSaveInstanceState(outState);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onDetachedFromRecyclerView(final RecyclerView recyclerView) {
        super.onDetachedFromRecyclerView(recyclerView);
        mChoiceModeDispatcher.dispatchDetachedFromRecyclerView(recyclerView);
    }

    /**
     * Set entities when new data is loaded.
     *
     * @param items a non-null {@link List} of entities.
     */
    public void setItems(@NonNull final List<E> items) {
        mItems.clear();
        mItems.addAll(items);
        mChoiceModeDispatcher.dispatchPostItemsChanged();
        notifyDataSetChanged();
    }

    /**
     * Adds a data entity to the end of the adapter.
     *
     * @param e a data entity
     */
    public void add(@NonNull final E e) {
        if (mItems.add(e)) {
            final int position = mItems.size() - 1;
            notifyItemInserted(position);
        }
    }

    /**
     * Adds a data entity to a given position.
     *
     * @param position an index in the data set.
     * @param e        a data entity
     */
    public void add(final int position, @NonNull final E e) {
        mItems.add(position, e);
        notifyItemInserted(position);
    }

    /**
     * Adds all data entities to the end of adapter
     *
     * @param items a non-null {@link List} of data entities.
     */
    public void addAll(@NonNull final List<E> items) {
        final int positionStart = mItems.size();
        if (mItems.addAll(items)) {
            notifyItemRangeInserted(positionStart, items.size());
        }
    }

    /**
     * Moves entity from one position to another with animation.
     *
     * @param fromPosition an initial index
     * @param toPosition   a new index
     */
    public void move(final int fromPosition, final int toPosition) {
        final E e = mItems.remove(fromPosition);
        mItems.add(toPosition, e);
        notifyItemMoved(fromPosition, toPosition);
    }

    /**
     * Removes entity at a given position and animates removal.
     *
     * @param position an index in the list of entities.
     * @return removed entity.
     */
    @NonNull
    public E remove(final int position) {
        mChoiceModeDispatcher.dispatchPreItemRemoved(position);
        final E e = mItems.remove(position);
        notifyItemRemoved(position);
        return e;
    }

    /**
     * Removes all entities from adapter.
     */
    public void clear() {
        final int itemCount = mItems.size();
        mItems.clear();
        notifyItemRangeRemoved(0, itemCount);
    }

    /**
     * Returns the total number of items in the data set hold by the adapter.
     *
     * @return The total number of items in this adapter.
     */
    @Override
    public int getItemCount() {
        return (mItems == null ? 0 : mItems.size());
    }

    /**
     * Returns true if there are no items in the dataset, false otherwise.
     */
    public boolean isEmpty() {
        return mItems.isEmpty();
    }

    /**
     * Get the data item associated with the specified position in the data set.
     *
     * @param position position of the item whose data we want within the adapter's
     *                 data set.
     * @return The data entity at the specified position.
     */
    @NonNull
    public E getItem(final int position) {
        return mItems.get(position);
    }

    public List<E> getItems() {
        return mItems;
    }

    /**
     * Indicates whether all the items in this adapter are enabled. If the
     * value returned by this method changes over time, there is no guarantee
     * it will take effect.  If true, it means all items are selectable and
     * clickable (there is no separator.)
     *
     * @return True if all items are enabled, false otherwise.
     * @see #isEnabled(int)
     */
    public boolean areAllItemsEnabled() {
        return true;
    }

    /**
     * Returns true if the item at the specified position is clickable.
     * <p/>
     * The result is unspecified if position is invalid. An {@link ArrayIndexOutOfBoundsException}
     * should be thrown in that case for fast failure.
     *
     * @param position an index of the item
     * @return {@code true} if item is clickable, {@code false} otherwise.
     * @see #areAllItemsEnabled()
     */
    public boolean isEnabled(final int position) {
        return true;
    }

    /**
     * Returns identifier of default item view.
     * Use {@link AbstractViewHolder#DEFAULT_CLICKABLE_VIEW_ID} to make root view clickable.
     *
     * @return a view identifier or {@link AbstractViewHolder#DEFAULT_CLICKABLE_VIEW_ID}
     */
    @IdRes
    public int getDefaultClickableViewId() {
        return AbstractViewHolder.DEFAULT_CLICKABLE_VIEW_ID;
    }

    /**
     * Returns array of clickable views ids at a given position.
     * Use {@link AbstractViewHolder#DEFAULT_CLICKABLE_VIEW_ID} to make root view clickable.
     *
     * @param position an index in the data set.
     * @return an array of view ids. Use {@link AbstractViewHolder#DEFAULT_CLICKABLE_VIEW_IDS}
     * if only root view should be clickable.
     */
    @IdRes
    public int[] getClickableViewIds(final int position) {
        return AbstractViewHolder.DEFAULT_CLICKABLE_VIEW_IDS;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @NonNull
    public final VH onCreateViewHolder(final ViewGroup parent, final int viewType) {
        final VH holder = onCreateViewHolder(mLayoutInflater, parent, viewType);
        return holder;
    }

    /**
     * Called when RecyclerView needs a new {@link RecyclerView.ViewHolder} of the given type to represent
     * an item.
     * <p>
     * This new ViewHolder should be constructed with a new View that can represent the items
     * of the given type. You can either create a new View manually or inflate it from an XML
     * layout file.
     * <p>
     * The new ViewHolder will be used to display items of the adapter using
     * {@link #onBindViewHolder(RecyclerView.ViewHolder, int, List)}. Since it will be re-used to display
     * different items in the data set, it is a good idea to cache references to sub views of
     * the View to avoid unnecessary {@link View#findViewById(int)} calls.
     *
     * @param inflater {@link LayoutInflater} to inflate views from XML.
     * @param parent   The ViewGroup into which the new View will be added after it is bound to
     *                 an adapter position.
     * @param viewType The view type of the new View.
     * @return A new ViewHolder that holds a View of the given view type.
     * @see #getItemViewType(int)
     * @see #onBindViewHolder(RecyclerView.ViewHolder, int)
     */
    @NonNull
    public abstract VH onCreateViewHolder(@NonNull final LayoutInflater inflater,
                                          @NonNull final ViewGroup parent, final int viewType);

    /**
     * {@inheritDoc}
     */
    @Override
    public void onBindViewHolder(@NonNull final VH holder, final int position) {
        makeViewHolderClickable(holder, position);
        onBindViewHolder(holder, mItems.get(position), position);
    }

    private void makeViewHolderClickable(@NonNull final VH holder, final int position) {
        final ChoiceMode choiceMode = mChoiceModeDispatcher.getChoiceMode();

        final boolean isItemEnabled = (areAllItemsEnabled() || isEnabled(position));
        final boolean isLongClickable = !choiceMode.isInChoiceMode();
        final int defaultClickableViewId = getDefaultClickableViewId();
        final int[] defaultClickableViewIds = getClickableViewIds(position);
        holder.setEnabled(isItemEnabled, isLongClickable,
                defaultClickableViewId, defaultClickableViewIds);

        holder.setOnViewHolderClickListener(this);
        holder.setInChoiceMode(choiceMode.isInChoiceMode());
        holder.setChecked(choiceMode.isItemChecked(getItemId(position)));
    }

    /**
     * Called by RecyclerView to display the data at the specified position. This method should
     * update the contents of the {@link RecyclerView.ViewHolder#itemView} to reflect the item at the given
     * position.
     * <p>
     * Note that unlike {@link android.widget.ListView}, RecyclerView will not call this method
     * again if the position of the item changes in the data set unless the item itself is
     * invalidated or the new position cannot be determined. For this reason, you should only
     * use the <code>position</code> parameter while acquiring the related data item inside
     * this method and should not keep a copy of it. If you need the position of an item later
     * on (e.g. in a click listener), use {@link RecyclerView.ViewHolder#getAdapterPosition()} which will
     * have the updated adapter position.
     *
     * @param holder   The ViewHolder which should be updated to represent the contents of the
     *                 item at the given position in the data set.
     * @param item     The item at the given position in the data set.
     * @param position The position of the item within the adapter's data set.
     */
    public abstract void onBindViewHolder(@NonNull final VH holder, final E item, final int position);

    /**
     * An {@link AbstractViewHolder} callback.
     *
     * @param position an index in the data set.
     */
    @Override
    public boolean onViewHolderLongClick(@NonNull final View view, final int position) {
        // The position check below is a possible solution for the bug which causes
        // ViewHolder.getAdapterPosition to return NO_POSITION for a visible item.
        return (position >= 0 && position < mItems.size() &&
                (mChoiceModeDispatcher.dispatchLongItemClick(view, position, getItemId(position)) ||
                        performLongItemClick(view, position)));
    }

    /**
     * Call the OnLongItemClickListener, if it is defined. Performs all normal
     * actions associated with long clicking: reporting accessibility event, etc.
     *
     * @param view     The view within the AdapterView that was clicked and held.
     * @param position The position of the view in the adapter.
     * @return True if there was an assigned OnLongItemClickListener that was
     * consumed the long click, false otherwise is returned.
     */
    private boolean performLongItemClick(@NonNull final View view, final int position) {
        final boolean result = (mOnLongItemClickListener != null &&
                mOnLongItemClickListener.onLongItemClick(view, position, getItem(position)));
        view.sendAccessibilityEvent(AccessibilityEvent.TYPE_VIEW_LONG_CLICKED);
        return result;
    }

    /**
     * An {@link AbstractViewHolder} callback.
     *
     * @param view     a view that was clicked.
     * @param position an index in the data set.
     */
    @Override
    public void onViewHolderClick(@NonNull final View view, final int position) {
        // The position check below is a possible solution for the bug which causes
        // ViewHolder.getAdapterPosition to return NO_POSITION for a visible item.
        if (position >= 0 && position < mItems.size()) {
            if (!mChoiceModeDispatcher.dispatchItemClick(view, position, getItemId(position))) {
                performItemClick(view, position);
            }
        }
    }

    /**
     * Call the OnItemClickListener, if it is defined. Performs all normal
     * actions associated with clicking: reporting accessibility event, playing
     * a sound, etc.
     *
     * @param view     The view within the AdapterView that was clicked.
     * @param position The position of the view in the adapter.
     * @return True if there was an assigned OnItemClickListener that was
     * called, false otherwise is returned.
     */
    private boolean performItemClick(@NonNull final View view, final int position) {
        final boolean result;
        if (mOnItemClickListener != null) {
            view.playSoundEffect(SoundEffectConstants.CLICK);
            mOnItemClickListener.onItemClick(view, position, getItem(position));
            result = true;
        } else {
            result = false;
        }
        view.sendAccessibilityEvent(AccessibilityEvent.TYPE_VIEW_CLICKED);
        return result;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onViewRecycled(@NonNull final VH holder) {
        super.onViewRecycled(holder);

        holder.onViewRecycled();

        holder.setEnabled(false, false, getDefaultClickableViewId(),
                getClickableViewIds(holder.getAdapterPosition()));
        holder.setOnViewHolderClickListener(null);
        holder.setInChoiceMode(false);
        holder.setChecked(false);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean onFailedToRecycleView(@NonNull final VH holder) {
        return super.onFailedToRecycleView(holder);
    }

}
