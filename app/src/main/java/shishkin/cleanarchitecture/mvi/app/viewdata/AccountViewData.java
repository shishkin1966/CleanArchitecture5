package shishkin.cleanarchitecture.mvi.app.viewdata;

import shishkin.cleanarchitecture.mvi.app.data.Account;

public class AccountViewData extends AbsViewData<Account> {

    @Override
    public String getName() {
        return getClass().getName();
    }
}
