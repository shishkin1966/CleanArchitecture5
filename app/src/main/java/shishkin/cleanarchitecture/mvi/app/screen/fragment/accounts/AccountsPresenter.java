package shishkin.cleanarchitecture.mvi.app.screen.fragment.accounts;

import android.content.DialogInterface;
import android.graphics.drawable.Drawable;


import org.michaelbel.bottomsheet.BottomSheet;

import java.util.List;


import shishkin.cleanarchitecture.mvi.R;
import shishkin.cleanarchitecture.mvi.app.ApplicationController;
import shishkin.cleanarchitecture.mvi.app.SLUtil;
import shishkin.cleanarchitecture.mvi.app.data.Account;
import shishkin.cleanarchitecture.mvi.app.db.MviDao;
import shishkin.cleanarchitecture.mvi.app.observe.DbObservable;
import shishkin.cleanarchitecture.mvi.app.request.GetAccountsRequest;
import shishkin.cleanarchitecture.mvi.app.request.GetBalanceRequest;
import shishkin.cleanarchitecture.mvi.app.request.GetCurrencyRequest;
import shishkin.cleanarchitecture.mvi.app.viewdata.AccountsViewData;
import shishkin.cleanarchitecture.mvi.common.utils.ApplicationUtils;
import shishkin.cleanarchitecture.mvi.common.utils.SafeUtils;
import shishkin.cleanarchitecture.mvi.common.utils.StringUtils;
import shishkin.cleanarchitecture.mvi.sl.DbObservableSubscriber;
import shishkin.cleanarchitecture.mvi.sl.ObservableUnionImpl;
import shishkin.cleanarchitecture.mvi.sl.data.Result;
import shishkin.cleanarchitecture.mvi.sl.event.ShowMessageEvent;
import shishkin.cleanarchitecture.mvi.sl.presenter.AbsPresenter;
import shishkin.cleanarchitecture.mvi.sl.request.ResponseListener;

/**
 * Created by Shishkin on 17.03.2018.
 */

public class AccountsPresenter extends AbsPresenter<AccountsModel> implements DbObservableSubscriber, ResponseListener, DialogInterface.OnClickListener {

    public static final String NAME = AccountsPresenter.class.getName();
    private static final String ALL = ApplicationController.getInstance().getString(R.string.all);

    private DialogInterface sortDialog;
    private DialogInterface filterDialog;
    private AccountsViewData accountsViewData = new AccountsViewData();

    public AccountsPresenter(AccountsModel model) {
        super(model);
    }

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public boolean isRegister() {
        return true;
    }

    public void onClick(int id) {
        getModel().getView().collapseBottomSheet();
        switch (id) {
            case R.id.create_account:
                getModel().getRouter().createAccount();
                break;

            case R.id.map:
                getModel().getRouter().showMap();
                break;

            case R.id.accounts_transfer:
                getModel().getRouter().accountsTransfer();
                break;

            case R.id.sort_accounts:
                sort_accounts();
                break;

            case R.id.select_accounts:
                select_accounts();
                break;
        }
    }

    private void sort_accounts() {
        final BottomSheet.Builder builder = new BottomSheet.Builder(getModel().getView().getActivity());
        sortDialog = builder
                .setDividers(true)
                .setTitleTextColorRes(R.color.blue)
                .setTitle(R.string.sort)
                .setMenu(R.menu.sort_menu, this)
                .show();
    }

    private void select_accounts() {
        if (accountsViewData.getCurrencies() != null && accountsViewData.getCurrencies().size() > 1) {
            final CharSequence[] items = new CharSequence[accountsViewData.getCurrencies().size() + 1];
            final Drawable[] icons = new Drawable[accountsViewData.getCurrencies().size() + 1];
            items[0] = ALL;
            for (int i = 0; i < accountsViewData.getCurrencies().size(); i++) {
                items[i + 1] = accountsViewData.getCurrencies().get(i);
            }
            final BottomSheet.Builder builder = new BottomSheet.Builder(getModel().getView().getActivity());
            filterDialog = builder
                    .setDividers(true)
                    .setTitleTextColorRes(R.color.blue)
                    .setTitle(R.string.select)
                    .setItems(items, icons, this)
                    .show();
        }
    }

    @Override
    public List<String> getSpecialistSubscription() {
        return StringUtils.arrayToList(
                super.getSpecialistSubscription(),
                ObservableUnionImpl.NAME
        );
    }

    @Override
    public List<String> getTables() {
        return StringUtils.arrayToList(Account.TABLE);
    }

    @Override
    public List<String> getObservable() {
        return StringUtils.arrayToList(DbObservable.NAME);
    }

    @Override
    public void onChange(Object object) {
        getData();
    }

    @Override
    public void onStart() {
        accountsViewData = SLUtil.getCacheSpecialist().get(AccountsViewData.NAME, AccountsViewData.class);
        if (accountsViewData == null) {
            accountsViewData = new AccountsViewData();
        }
        getModel().getView().refreshViews(accountsViewData);
        getData();
    }

    private void getData() {
        getModel().getView().showProgressBar();
        getModel().getInteractor().getCurrency(this);
        getModel().getInteractor().getAccounts(this);
        getModel().getInteractor().getBalance(this);
    }

    @Override
    public void response(Result result) {
        if (!validate()) return;

        getModel().getView().hideProgressBar();
        if (!result.hasError()) {
            if (result.getName().equals(GetAccountsRequest.NAME)) {
                accountsViewData.setAccounts(SafeUtils.cast(result.getData()));
                getModel().getView().refreshViews(accountsViewData);
            } else if (result.getName().equals(GetBalanceRequest.NAME)) {
                final List<MviDao.Balance> list = SafeUtils.cast(result.getData());
                getModel().getView().refreshBalance(list);
            } else if (result.getName().equals(GetCurrencyRequest.NAME)) {
                accountsViewData.setCurrencies(SafeUtils.cast(result.getData()));
            }
        } else {
            SLUtil.getActivityUnion().showMessage(new ShowMessageEvent(result.getErrorText()).setType(ApplicationUtils.MESSAGE_TYPE_ERROR));
        }
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
        if (dialog.equals(sortDialog)) {
            accountsViewData.setSort(which);
        } else if (dialog.equals(filterDialog)) {
            if (which == 0) {
                accountsViewData.setFilter(null);
            } else {
                accountsViewData.setFilter(accountsViewData.getCurrencies().get(which - 1));
            }
        }
        getModel().getView().refreshViews(accountsViewData);
    }

    public void onClickAccounts(Account account) {
        getModel().getRouter().showAccount(account);
    }

    @Override
    public void onDestroyView() {
        SLUtil.getCacheSpecialist().put(AccountsViewData.NAME, accountsViewData);

        super.onDestroyView();
    }

}

