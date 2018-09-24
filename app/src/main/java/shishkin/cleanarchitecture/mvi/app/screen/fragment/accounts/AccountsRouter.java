package shishkin.cleanarchitecture.mvi.app.screen.fragment.accounts;

import shishkin.cleanarchitecture.mvi.app.data.Account;
import shishkin.cleanarchitecture.mvi.app.model.BaseModelRouter;
import shishkin.cleanarchitecture.mvi.app.screen.fragment.account.AccountFragment;
import shishkin.cleanarchitecture.mvi.app.screen.fragment.create_account.CreateAccountFragment;
import shishkin.cleanarchitecture.mvi.app.screen.fragment.digital_currencies.DigitalCurrenciesFragment;
import shishkin.cleanarchitecture.mvi.sl.model.AbsModel;

/**
 * Created by Shishkin on 17.03.2018.
 */

public class AccountsRouter extends BaseModelRouter {

    public AccountsRouter(AbsModel model) {
        super(model);
    }

    public void createAccount() {
        showFragment(CreateAccountFragment.newInstance());
    }

    public void accountsTransfer() {
        showFragment(DigitalCurrenciesFragment.newInstance());
    }

    public void showAccount(Account account) {
        showFragment(AccountFragment.newInstance(account));
    }
}
