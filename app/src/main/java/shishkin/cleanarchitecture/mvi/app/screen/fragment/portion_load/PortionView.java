package shishkin.cleanarchitecture.mvi.app.screen.fragment.portion_load;

import shishkin.cleanarchitecture.mvi.sl.ui.IFragment;

/**
 * Created by Shishkin on 17.03.2018.
 */

public interface PortionView extends IFragment {
    void refreshViews(PagingViewData viewData);

}