package shishkin.cleanarchitecture.mvi.app.screen.fragment.paged_load;

import shishkin.cleanarchitecture.mvi.app.screen.fragment.portion_load.PagingViewData;
import shishkin.cleanarchitecture.mvi.sl.ui.IFragment;

/**
 * Created by Shishkin on 17.03.2018.
 */

public interface PagedView extends IFragment {
    void onRefresh();

    void refreshViews(PagingViewData viewData);
}