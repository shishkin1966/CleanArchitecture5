package shishkin.cleanarchitecture.mvi.app.screen.fragment.digital_currencies;

import shishkin.cleanarchitecture.mvi.app.viewdata.TickerViewData;
import shishkin.cleanarchitecture.mvi.sl.ui.IFragment;

/**
 * Created by Shishkin on 17.03.2018.
 */

public interface DigitalCurrenciesView extends IFragment {

    void refreshViews(TickerViewData viewData);

}
