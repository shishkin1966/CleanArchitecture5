package shishkin.cleanarchitecture.mvi.app.screen.fragment.accounts;

import shishkin.cleanarchitecture.mvi.app.SLUtil;
import shishkin.cleanarchitecture.mvi.app.data.Account;
import shishkin.cleanarchitecture.mvi.app.model.BaseModelRouter;
import shishkin.cleanarchitecture.mvi.app.screen.fragment.account.AccountFragment;
import shishkin.cleanarchitecture.mvi.app.screen.fragment.create_account.CreateAccountFragment;
import shishkin.cleanarchitecture.mvi.common.utils.ApplicationUtils;
import shishkin.cleanarchitecture.mvi.sl.model.AbsModel;

/**
 * Created by Shishkin on 17.03.2018.
 */

class AccountsRouter extends BaseModelRouter {

    AccountsRouter(AbsModel model) {
        super(model);
    }

    void createAccount() {
        showFragment(CreateAccountFragment.newInstance());
    }

    void showAccount(Account account) {
        showFragment(AccountFragment.newInstance(account));
    }

    void showUrl() {
        ApplicationUtils.showUrl(SLUtil.getActivity(), "https://github.com/shishkin1966/CleanArchitecture5");
    }
}
