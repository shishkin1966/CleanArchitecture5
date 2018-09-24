package shishkin.cleanarchitecture.mvi.app.sl;

import shishkin.cleanarchitecture.mvi.app.data.Account;
import shishkin.cleanarchitecture.mvi.sl.request.ResponseListener;

/**
 * Created by Shishkin on 16.12.2017.
 */

public class Repository {

    private static volatile Repository sInstance;

    public static void instantiate() {
        if (sInstance == null) {
            synchronized (Repository.class) {
                if (sInstance == null) {
                    sInstance = new Repository();
                }
            }
        }
    }

    public static Repository getInstance() {
        if (sInstance == null) {
            instantiate();
        }
        return sInstance;
    }

    private Repository() {
    }

    public void addAccount(Account account, ResponseListener listener) {
        DbRepositoryProvider.addAccount(account, listener);
    }

    public void getAccounts(ResponseListener listener) {
        DbRepositoryProvider.getAccounts(listener);
    }

    public void getBalance(ResponseListener listener) {
        DbRepositoryProvider.getBalance(listener);
    }

    public void getCurrency(ResponseListener listener) {
        DbRepositoryProvider.getCurrency(listener);
    }

    public void getTicker(String listener) {
        NetRepositoryProvider.getTicker(listener);
    }

}
