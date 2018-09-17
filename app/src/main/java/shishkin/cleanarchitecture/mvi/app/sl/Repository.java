package shishkin.cleanarchitecture.mvi.app.sl;

import shishkin.cleanarchitecture.mvi.app.data.Account;
import shishkin.cleanarchitecture.mvi.sl.request.ResponseListener;

/**
 * Created by Shishkin on 16.12.2017.
 */

public class Repository {

    private static volatile Repository sInstance;

    private ProviderDelegate mProviderDelegate;
    private Contract mContract;

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
        mProviderDelegate = new ProviderDelegate();
        mContract = new Contract();
    }

    public void addAccount(Account account, ResponseListener listener) {
        getProvider().addAccount(account, listener);
    }

    public void getAccounts(ResponseListener listener) {
        getProvider().getAccounts(listener);
    }

    public void getBalance(ResponseListener listener) {
        getProvider().getBalance(listener);
    }

    public void getCurrency(ResponseListener listener) {
        getProvider().getCurrency(listener);
    }

    private RepositoryProvider getProvider() {
        return mProviderDelegate.get(mContract);
    }
}
