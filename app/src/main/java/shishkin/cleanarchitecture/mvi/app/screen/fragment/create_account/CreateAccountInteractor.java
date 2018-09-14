package shishkin.cleanarchitecture.mvi.app.screen.fragment.create_account;

import shishkin.cleanarchitecture.mvi.app.data.Account;
import shishkin.cleanarchitecture.mvi.app.sl.Repository;
import shishkin.cleanarchitecture.mvi.sl.model.ModelInteractor;
import shishkin.cleanarchitecture.mvi.sl.request.ResponseListener;

/**
 * Created by Shishkin on 17.03.2018.
 */

public class CreateAccountInteractor implements ModelInteractor {

    public void addAccount(Account account, ResponseListener listener) {
        Repository.addAccount(account, listener);
    }
}
