package shishkin.cleanarchitecture.mvi.common.recyclerview.choice;

import shishkin.cleanarchitecture.mvi.common.recyclerview.ChoiceMode;

/**
 * {@link ChoiceMode} that does not have any choice behavior.
 * It is a default adapter's choice mode.
 */
public class NoneChoiceMode extends ChoiceMode {

    public NoneChoiceMode() {
        super();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isInChoiceMode() {
        return false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getCheckedItemCount() {
        return 0;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isItemChecked(final long itemId) {
        return false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setItemChecked(final long itemId, final boolean checked) {
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void clearChoices() {
    }

}
