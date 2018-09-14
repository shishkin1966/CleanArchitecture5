package shishkin.cleanarchitecture.mvi.app.screen.fragment.account;

import shishkin.cleanarchitecture.mvi.sl.model.AbsModel;

/**
 * Created by Shishkin on 17.03.2018.
 */

public class AccountModel extends AbsModel {

    public AccountModel(AccountFragment fragment) {
        super(fragment);

    }

    @Override
    public AccountFragment getView() {
        return super.getView();
    }

}
