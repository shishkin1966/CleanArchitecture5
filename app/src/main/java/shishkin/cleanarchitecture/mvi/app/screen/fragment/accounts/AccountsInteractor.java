package shishkin.cleanarchitecture.mvi.app.screen.fragment.accounts;

import shishkin.cleanarchitecture.mvi.app.SLUtil;
import shishkin.cleanarchitecture.mvi.sl.model.ModelInteractor;
import shishkin.cleanarchitecture.mvi.sl.request.ResponseListener;

/**
 * Created by Shishkin on 17.03.2018.
 */

public class AccountsInteractor implements ModelInteractor {

    public void getAccounts(ResponseListener listener) {
        SLUtil.getRepository().getAccounts(listener);
    }

    public void getCurrency(ResponseListener listener) {
        SLUtil.getRepository().getCurrency(listener);
    }
}
