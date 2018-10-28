package shishkin.cleanarchitecture.mvi.app.screen.fragment.accounts;

import shishkin.cleanarchitecture.mvi.sl.model.AbsModel;

/**
 * Created by Shishkin on 17.03.2018.
 */

public class AccountsModel extends AbsModel {

    AccountsModel(AccountsFragment fragment) {
        super(fragment);

        setInteractor(new AccountsInteractor());
        setRouter(new AccountsRouter(this));
        setPresenter(new AccountsPresenter(this));
    }

    @Override
    public AccountsFragment getView() {
        return super.getView();
    }

    @Override
    public AccountsPresenter getPresenter() {
        return super.getPresenter();
    }

    @Override
    public AccountsRouter getRouter() {
        return super.getRouter();
    }

    @Override
    public AccountsInteractor getInteractor() {
        return super.getInteractor();
    }

}
