package shishkin.cleanarchitecture.mvi.app.specialist.repository;

import shishkin.cleanarchitecture.mvi.app.SLUtil;
import shishkin.cleanarchitecture.mvi.app.data.Account;
import shishkin.cleanarchitecture.mvi.app.request.AddAccountRequest;
import shishkin.cleanarchitecture.mvi.app.request.GetAccountsRequest;
import shishkin.cleanarchitecture.mvi.app.request.GetBalanceRequest;
import shishkin.cleanarchitecture.mvi.app.request.GetCurrencyRequest;
import shishkin.cleanarchitecture.mvi.sl.request.ResponseListener;

public class DbRepositoryProvider {
    public static void addAccount(ResponseListener listener, Account account) {
        SLUtil.getDbProvider().request(new AddAccountRequest(listener, account));
    }

    public static void getAccounts(ResponseListener listener) {
        SLUtil.getDbProvider().request(new GetAccountsRequest(listener));
    }

    public static void getBalance(ResponseListener listener) {
        SLUtil.getDbProvider().request(new GetBalanceRequest(listener));
    }

    public static void getCurrency(ResponseListener listener) {
        SLUtil.getDbProvider().request(new GetCurrencyRequest(listener));
    }

}
