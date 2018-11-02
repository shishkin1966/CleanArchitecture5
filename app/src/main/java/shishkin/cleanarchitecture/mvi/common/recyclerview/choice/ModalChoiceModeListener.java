package shishkin.cleanarchitecture.mvi.common.recyclerview.choice;

import androidx.annotation.NonNull;
import androidx.appcompat.view.ActionMode;

/**
 * A ModalChoiceModeListener receives events for this choice mode.
 * It acts as the {@link ActionMode.Callback} for the selection mode and also receives
 * {@link #onItemCheckedStateChanged(ActionMode, long, boolean)} events when the user
 * selects and deselects list items.
 */
public interface ModalChoiceModeListener extends ActionMode.Callback {

    /**
     * Called when an item is checked or unchecked during selection mode.
     *
     * @param mode    The {@link ActionMode} providing the selection mode.
     * @param itemId  The item id that was checked or unchecked.
     * @param checked <code>true</code> if the item is now checked, <code>false</code>
     *                if the item is now unchecked.
     */
    void onItemCheckedStateChanged(@NonNull final ActionMode mode,
                                   final long itemId, boolean checked);

}