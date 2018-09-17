package shishkin.cleanarchitecture.mvi.app.sl;

import shishkin.cleanarchitecture.mvi.app.data.Account;
import shishkin.cleanarchitecture.mvi.sl.delegate.Delegating;
import shishkin.cleanarchitecture.mvi.sl.request.ResponseListener;

public interface RepositoryProvider extends Delegating {
    void addAccount(Account account, ResponseListener listener);

    void getAccounts(ResponseListener listener);

    void getBalance(ResponseListener listener);

    void getCurrency(ResponseListener listener);
}
