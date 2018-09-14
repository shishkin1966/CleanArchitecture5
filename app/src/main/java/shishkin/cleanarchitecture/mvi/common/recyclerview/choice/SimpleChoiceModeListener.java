package shishkin.cleanarchitecture.mvi.common.recyclerview.choice;

/**
 * A SimpleChoiceModeListener receives events for this choice mode.
 * It receives {@link #onItemCheckedStateChanged(long, boolean)} events when the user
 * selects and deselects list items.
 */
public interface SimpleChoiceModeListener {

    /**
     * Called when an item is checked or unchecked during selection mode.
     *
     * @param itemId  The item id that was checked or unchecked.
     * @param checked <code>true</code> if the item is now checked, <code>false</code>
     *                if the item is now unchecked.
     */
    void onItemCheckedStateChanged(final long itemId, final boolean checked);

}
