package shishkin.cleanarchitecture.mvi.app.screen.fragment.sidemenu;

import java.util.List;


import shishkin.cleanarchitecture.mvi.app.db.MviDao;
import shishkin.cleanarchitecture.mvi.app.observe.AccountsBalanceListener;
import shishkin.cleanarchitecture.mvi.sl.presenter.AbsPresenter;

public class SideMenuPresenter extends AbsPresenter<SideMenuModel> implements AccountsBalanceListener {

    public static final String NAME = SideMenuPresenter.class.getName();

    public SideMenuPresenter(SideMenuModel model) {
        super(model);
    }

    @Override
    public boolean isRegister() {
        return true;
    }

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public void showAccountsBalance(List<MviDao.Balance> list) {
        getModel().getView().accountsChanged(list);
    }
}
