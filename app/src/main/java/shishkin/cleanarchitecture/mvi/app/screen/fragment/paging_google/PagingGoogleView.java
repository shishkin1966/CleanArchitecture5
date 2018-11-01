package shishkin.cleanarchitecture.mvi.app.screen.fragment.paging_google;

import shishkin.cleanarchitecture.mvi.app.screen.fragment.paging.PagingViewData;
import shishkin.cleanarchitecture.mvi.sl.ui.IFragment;

/**
 * Created by Shishkin on 17.03.2018.
 */

public interface PagingGoogleView extends IFragment {
    void onRefresh();

    void refreshViews(PagingViewData viewData);
}
