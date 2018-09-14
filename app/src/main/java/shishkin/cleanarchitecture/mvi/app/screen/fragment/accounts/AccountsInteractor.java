package shishkin.cleanarchitecture.mvi.app.screen.fragment.accounts;

import shishkin.cleanarchitecture.mvi.app.sl.Repository;
import shishkin.cleanarchitecture.mvi.sl.model.ModelInteractor;
import shishkin.cleanarchitecture.mvi.sl.request.ResponseListener;

/**
 * Created by Shishkin on 17.03.2018.
 */

public class AccountsInteractor implements ModelInteractor {

    public void getAccounts(ResponseListener listener) {
        Repository.getAccounts(listener);
    }

    public void getBalance(ResponseListener listener) {
        Repository.getBalance(listener);
    }

    public void getCurrency(ResponseListener listener) {
        Repository.getCurrency(listener);
    }
}
