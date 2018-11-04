package shishkin.cleanarchitecture.mvi.app.screen.fragment.create_account;

import shishkin.cleanarchitecture.mvi.app.SLUtil;
import shishkin.cleanarchitecture.mvi.app.data.Account;
import shishkin.cleanarchitecture.mvi.app.request.AddAccountRequest;
import shishkin.cleanarchitecture.mvi.sl.model.ModelInteractor;
import shishkin.cleanarchitecture.mvi.sl.request.ResponseListener;

/**
 * Created by Shishkin on 17.03.2018.
 */

class CreateAccountInteractor implements ModelInteractor {

    void addAccount(ResponseListener listener, Account account) {
        SLUtil.getDbProvider().request(new AddAccountRequest(listener, account));
    }
}
