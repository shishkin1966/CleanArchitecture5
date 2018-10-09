package shishkin.cleanarchitecture.mvi.app.screen.fragment.val_curs;

import shishkin.cleanarchitecture.mvi.app.viewdata.ValCursViewData;
import shishkin.cleanarchitecture.mvi.sl.ui.IFragment;

/**
 * Created by Shishkin on 17.03.2018.
 */

public interface ValCursView extends IFragment {
    void refreshViews(ValCursViewData viewData);

}
