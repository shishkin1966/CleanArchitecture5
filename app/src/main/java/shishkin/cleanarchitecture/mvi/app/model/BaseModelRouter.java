package shishkin.cleanarchitecture.mvi.app.model;

import shishkin.cleanarchitecture.mvi.app.screen.fragment.accounts.AccountsFragment;
import shishkin.cleanarchitecture.mvi.sl.model.AbsModel;
import shishkin.cleanarchitecture.mvi.sl.model.AbsModelRouter;

/**
 * Created by Shishkin on 26.01.2018.
 */

public class BaseModelRouter extends AbsModelRouter {

    public BaseModelRouter(AbsModel model) {
        super(model);
    }

    @Override
    public void showMainFragment() {
        showFragment(AccountsFragment.newInstance(), true);
    }

}
