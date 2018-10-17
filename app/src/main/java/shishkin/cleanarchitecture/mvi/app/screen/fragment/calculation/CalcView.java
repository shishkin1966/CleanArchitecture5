package shishkin.cleanarchitecture.mvi.app.screen.fragment.calculation;

import shishkin.cleanarchitecture.mvi.sl.ui.IFragment;

/**
 * Created by Shishkin on 17.03.2018.
 */

public interface CalcView extends IFragment {
    void refreshViews(CalcViewData viewData);

}
