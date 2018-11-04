package shishkin.cleanarchitecture.mvi.app.screen.fragment.digital_currencies;

import shishkin.cleanarchitecture.mvi.app.SLUtil;
import shishkin.cleanarchitecture.mvi.app.request.GetTickerRequest;
import shishkin.cleanarchitecture.mvi.sl.model.ModelInteractor;

/**
 * Created by Shishkin on 17.03.2018.
 */

class DigitalCurrenciesInteractor implements ModelInteractor {

    void getTicker(String listener) {
        SLUtil.getNetProvider().request(new GetTickerRequest(listener));
    }
}
