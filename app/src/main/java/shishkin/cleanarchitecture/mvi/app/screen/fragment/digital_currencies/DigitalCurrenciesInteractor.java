package shishkin.cleanarchitecture.mvi.app.screen.fragment.digital_currencies;

import shishkin.cleanarchitecture.mvi.app.sl.Repository;
import shishkin.cleanarchitecture.mvi.sl.model.ModelInteractor;

/**
 * Created by Shishkin on 17.03.2018.
 */

public class DigitalCurrenciesInteractor implements ModelInteractor {

    public void getTicker(String listener) {
        Repository.getInstance().getTicker(listener);
    }
}
