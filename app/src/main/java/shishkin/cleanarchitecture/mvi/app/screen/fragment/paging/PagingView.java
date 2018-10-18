package shishkin.cleanarchitecture.mvi.app.screen.fragment.paging;

import shishkin.cleanarchitecture.mvi.sl.ui.IFragment;

/**
 * Created by Shishkin on 17.03.2018.
 */

public interface PagingView extends IFragment {
    void refreshViews(PagingViewData viewData);

}
