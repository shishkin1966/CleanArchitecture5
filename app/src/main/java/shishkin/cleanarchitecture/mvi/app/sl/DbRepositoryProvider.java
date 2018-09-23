package shishkin.cleanarchitecture.mvi.app.sl;

import shishkin.cleanarchitecture.mvi.app.SLUtil;
import shishkin.cleanarchitecture.mvi.app.data.Account;
import shishkin.cleanarchitecture.mvi.app.request.AddAccountRequest;
import shishkin.cleanarchitecture.mvi.app.request.GetAccountsRequest;
import shishkin.cleanarchitecture.mvi.app.request.GetBalanceRequest;
import shishkin.cleanarchitecture.mvi.app.request.GetCurrencyRequest;
import shishkin.cleanarchitecture.mvi.sl.request.ResponseListener;

public class DbRepositoryProvider implements RepositoryProvider {
    @Override
    public void addAccount(Account account, ResponseListener listener) {
        SLUtil.getDbProvider().request(new AddAccountRequest(account, listener));
    }

    @Override
    public void getAccounts(ResponseListener listener) {
        SLUtil.getDbProvider().request(new GetAccountsRequest(listener));
    }

    @Override
    public void getBalance(ResponseListener listener) {
        SLUtil.getDbProvider().request(new GetBalanceRequest(listener));
    }

    @Override
    public void getCurrency(ResponseListener listener) {
        SLUtil.getDbProvider().request(new GetCurrencyRequest(listener));
    }

    @Override
    public void processing(Object object) {
    }
}
