package shishkin.cleanarchitecture.mvi.app.screen.fragment.digital_currencies;

import java.util.List;


import shishkin.cleanarchitecture.mvi.app.data.Ticker;
import shishkin.cleanarchitecture.mvi.sl.ui.IFragment;

/**
 * Created by Shishkin on 17.03.2018.
 */

public interface DigitalCurrenciesView extends IFragment {

    void refreshTickers(List<Ticker> list);

}
