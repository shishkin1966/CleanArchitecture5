package shishkin.cleanarchitecture.mvi.app.screen.fragment.sidemenu;

import android.view.View;


import java.util.List;


import shishkin.cleanarchitecture.mvi.R;
import shishkin.cleanarchitecture.mvi.app.SLUtil;
import shishkin.cleanarchitecture.mvi.app.db.MviDao;
import shishkin.cleanarchitecture.mvi.app.observe.AccountsBalanceListener;
import shishkin.cleanarchitecture.mvi.app.screen.activity.main.MainPresenter;
import shishkin.cleanarchitecture.mvi.common.utils.SafeUtils;
import shishkin.cleanarchitecture.mvi.sl.data.Result;
import shishkin.cleanarchitecture.mvi.sl.presenter.AbsPresenter;
import shishkin.cleanarchitecture.mvi.sl.request.ResponseListener;
import shishkin.cleanarchitecture.mvi.sl.viewaction.ViewAction;

public class SideMenuPresenter extends AbsPresenter<SideMenuModel> implements AccountsBalanceListener, View.OnClickListener, ResponseListener {

    public static final String NAME = SideMenuPresenter.class.getName();

    private SideMenuViewData viewData;

    SideMenuPresenter(SideMenuModel model) {
        super(model);

        viewData = SLUtil.getCacheSpecialist().get(SideMenuViewData.NAME, SideMenuViewData.class);
        if (viewData == null) {
            viewData = new SideMenuViewData();
        }
    }

    @Override
    public void onStart() {
        if (viewData.getBalance() == null) {
            getModel().getInteractor().getBalance(this);
        } else {
            showAccountsBalance(viewData.getBalance());
        }
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
        viewData.setBalance(list);
        getModel().getView().doViewAction(new ViewAction("accountsChanged", list));
    }

    @Override
    public void onClick(View v) {
        final int id = v.getId();
        final MainPresenter presenter = SLUtil.getPresenterUnion().getPresenter(MainPresenter.NAME);
        if (presenter != null) {
            presenter.hideSideMenu();
        }
        switch (id) {
            case R.id.accounts:
                getModel().getRouter().switchToTop();
                break;

            case R.id.exchange_rates:
                getModel().getRouter().showValCurs();
                break;

            case R.id.exchange_cryptorates:
                getModel().getRouter().showDigitalCurrencies();
                break;

            case R.id.address:
                getModel().getRouter().showMap();
                break;

            case R.id.setting:
                getModel().getRouter().showSetting();
                break;

            case R.id.scanner:
                getModel().getRouter().showScanner();
                break;

            case R.id.calc:
                getModel().getRouter().showCalc();
                break;

            case R.id.paging:
                getModel().getRouter().showPaging();
                break;

            case R.id.paging_google:
                getModel().getRouter().showPagingGoogle();
                break;

            case R.id.contact:
                getModel().getRouter().showContact();
                break;

        }
    }

    @Override
    public void onDestroyView() {
        SLUtil.getCacheSpecialist().put(SideMenuViewData.NAME, viewData);

        super.onDestroyView();
    }

    @Override
    public void response(Result result) {
        if (!result.hasError()) {
            viewData.setBalance(SafeUtils.cast(result.getData()));
            getModel().getView().doViewAction(new ViewAction("accountsChanged", viewData.getBalance()));
        } else {
            SLUtil.onError(NAME, result.getErrorText(), true);
        }

    }
}
