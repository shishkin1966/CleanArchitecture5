package shishkin.cleanarchitecture.mvi.app.screen.fragment.accounts;

import android.content.DialogInterface;
import android.graphics.drawable.Drawable;


import org.michaelbel.bottomsheet.BottomSheet;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;


import shishkin.cleanarchitecture.mvi.R;
import shishkin.cleanarchitecture.mvi.app.ApplicationController;
import shishkin.cleanarchitecture.mvi.app.SLUtil;
import shishkin.cleanarchitecture.mvi.app.data.Account;
import shishkin.cleanarchitecture.mvi.app.observe.DbObservable;
import shishkin.cleanarchitecture.mvi.app.request.GetAccountsRequest;
import shishkin.cleanarchitecture.mvi.app.request.GetBalanceRequest;
import shishkin.cleanarchitecture.mvi.app.request.GetCurrencyRequest;
import shishkin.cleanarchitecture.mvi.app.viewdata.AccountViewData;
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

    private AccountViewData mViewData = new AccountViewData();
    private DialogInterface mDialogSort;
    private DialogInterface mDialogSelect;
    private Comparator<Account> mComparatorName = (o1, o2) -> o1.getFriendlyName().compareTo(o2.getFriendlyName());
    private Comparator<Account> mComparatorCurrency = (o1, o2) -> o1.getCurrency().compareTo(o2.getCurrency());
    private List<String> mCurrency;

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
        switch (id) {
            case R.id.create_account:
                create_account();
                break;

            case R.id.accounts_transfer:
                accounts_transfer();
                break;

            case R.id.sort_accounts:
                sort_accounts();
                break;

            case R.id.select_accounts:
                select_accounts();
                break;
        }
    }


    private void create_account() {
        getModel().getRouter().createAccount();
    }

    private void accounts_transfer() {
        getModel().getRouter().accountsTransfer();
    }

    private void sort_accounts() {
        final BottomSheet.Builder builder = new BottomSheet.Builder(getModel().getView().getActivity());
        mDialogSort = builder
                .setDividers(true)
                .setTitleTextColorRes(R.color.blue)
                .setTitle(R.string.sort)
                .setMenu(R.menu.sort_menu, this)
                .show();
    }

    private void select_accounts() {
        if (mCurrency != null && mCurrency.size() > 1) {
            final CharSequence[] items = new CharSequence[mCurrency.size() + 1];
            final Drawable[] icons = new Drawable[mCurrency.size() + 1];
            items[0] = ALL;
            for (int i = 0; i < mCurrency.size(); i++) {
                items[i + 1] = mCurrency.get(i);
            }
            final BottomSheet.Builder builder = new BottomSheet.Builder(getModel().getView().getActivity());
            mDialogSelect = builder
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
        getData();
    }

    private void getData() {
        getModel().getView().showProgressBar();
        getModel().getInteractor().getAccounts(this);
        getModel().getInteractor().getBalance(this);
        getModel().getInteractor().getCurrency(this);
    }

    @Override
    public void response(Result result) {
        getModel().getView().hideProgressBar();
        if (!result.hasError()) {
            if (result.getName().equals(GetAccountsRequest.NAME)) {
                mViewData.setData(SafeUtils.cast(result.getData()));
                setData();
            } else if (result.getName().equals(GetBalanceRequest.NAME)) {
                getModel().getView().refreshBalance(SafeUtils.cast(result.getData()));
            } else if (result.getName().equals(GetCurrencyRequest.NAME)) {
                mCurrency = SafeUtils.cast(result.getData());
            }
        } else {
            SLUtil.getActivityUnion().showToast(new ShowMessageEvent(result.getErrorText()).setType(ApplicationUtils.MESSAGE_TYPE_ERROR));
        }
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
        if (dialog.equals(mDialogSort)) {
            mViewData.setSort(which);
        } else if (dialog.equals(mDialogSelect)) {
            mViewData.setFilter(which);
        }
        setData();
    }

    private void setData() {
        final List<Account> list = new ArrayList<>();
        if (mViewData.getFilter() == 0) {
            list.addAll(mViewData.getData());
        } else {
            final String currency = mCurrency.get(mViewData.getFilter() - 1);
            list.addAll(SLUtil.getDataSpecialist().filter(mViewData.getData(), value -> value.getCurrency().equals(currency)).toList());
        }
        switch (mViewData.getSort()) {
            case 0:
                getModel().getView().refreshAccounts(list);
                break;
            case 1:
                getModel().getView().refreshAccounts(SLUtil.getDataSpecialist().sort(list, mComparatorName).toList());
                break;
            case 2:
                getModel().getView().refreshAccounts(SLUtil.getDataSpecialist().sort(list, mComparatorCurrency).toList());
                break;
        }
    }

    public void onClickAccounts(Account account) {
        getModel().getRouter().showAccount(account);
    }
}

