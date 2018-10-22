package shishkin.cleanarchitecture.mvi.app.screen.fragment.digital_currencies;

import shishkin.cleanarchitecture.mvi.app.SLUtil;
import shishkin.cleanarchitecture.mvi.sl.model.ModelInteractor;

/**
 * Created by Shishkin on 17.03.2018.
 */

public class DigitalCurrenciesInteractor implements ModelInteractor {

    public void getTicker(String listener) {
        SLUtil.getRepository().getTicker(listener);
    }
}
