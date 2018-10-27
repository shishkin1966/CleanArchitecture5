package shishkin.cleanarchitecture.mvi.app.screen.fragment.val_curs;

import java.util.List;


import shishkin.cleanarchitecture.mvi.app.data.Valute;
import shishkin.cleanarchitecture.mvi.sl.ui.IFragment;

/**
 * Created by Shishkin on 17.03.2018.
 */

public interface ValCursView extends IFragment {
    void refreshViews(ValCursViewData viewData);

    void refreshSelected(ValCursViewData viewData);

    void refreshBottomNavigation(ValCursViewData viewData);

    void removeItems(List<Valute> items);
}
