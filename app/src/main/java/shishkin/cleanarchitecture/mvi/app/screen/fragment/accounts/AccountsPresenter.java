package shishkin.cleanarchitecture.mvi.app.screen.fragment.accounts;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;


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
import shishkin.cleanarchitecture.mvi.sl.event.DialogResultEvent;
import shishkin.cleanarchitecture.mvi.sl.event.ShowMessageEvent;
import shishkin.cleanarchitecture.mvi.sl.presenter.AbsPresenter;
import shishkin.cleanarchitecture.mvi.sl.request.ResponseListener;
import shishkin.cleanarchitecture.mvi.sl.ui.DialogResultListener;
import shishkin.cleanarchitecture.mvi.sl.ui.MaterialDialogExt;
import shishkin.cleanarchitecture.mvi.sl.ui.Messager;

/**
 * Created by Shishkin on 17.03.2018.
 */

public class AccountsPresenter extends AbsPresenter<AccountsModel> implements DbObservableSubscriber, ResponseListener, DialogInterface.OnClickListener, DialogResultListener, Messager {

    public static final String NAME = AccountsPresenter.class.getName();
    private static final String ALL = ApplicationController.getInstance().getString(R.string.all);

    private DialogInterface sortDialog;
    private DialogInterface filterDialog;
    private AccountsViewData accountsViewData;

    public AccountsPresenter(AccountsModel model) {
        super(model);

        accountsViewData = SLUtil.getCacheSpecialist().get(AccountsViewData.NAME, AccountsViewData.class);
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

            case R.id.select_accounts_all:
                select_accounts_all();
                break;

            case R.id.start:
                if (SLUtil.getMediaSpecialist().isStop()) {
                    SLUtil.getMediaSpecialist().play(R.raw.music);
                } else {
                    SLUtil.getMediaSpecialist().resume();
                }
                break;

            case R.id.pause:
                SLUtil.getMediaSpecialist().pause();
                break;

            case R.id.stop:
                SLUtil.getMediaSpecialist().stop();
                break;

            case R.id.message:
                getModel().getView().hideMessage();
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
        if (getViewData().getCurrencies() != null && getViewData().getCurrencies().size() > 1) {
            final CharSequence[] items = new CharSequence[getViewData().getCurrencies().size() + 1];
            final Drawable[] icons = new Drawable[getViewData().getCurrencies().size() + 1];
            items[0] = ALL;
            for (int i = 0; i < getViewData().getCurrencies().size(); i++) {
                items[i + 1] = getViewData().getCurrencies().get(i);
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

    private void select_accounts_all() {
        getViewData().setFilter(null);
        getModel().getView().refreshViews(getViewData());
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
        accountsViewData = getViewData();
        if (!accountsViewData.isShowPermissionDialog() && !ApplicationUtils.checkPermission(SLUtil.getContext(), Manifest.permission.ACCESS_FINE_LOCATION)) {
            accountsViewData.setShowPermissionDialog(true);
            SLUtil.getCacheSpecialist().put(AccountsViewData.NAME, getViewData());
            getModel().getView().grantPermission(NAME, Manifest.permission.ACCESS_FINE_LOCATION, "Право необходимо для показа карты");
        }

        getModel().getView().refreshViews(getViewData());
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
                getViewData().setAccounts(SafeUtils.cast(result.getData()));
                getModel().getView().refreshViews(getViewData());
            } else if (result.getName().equals(GetBalanceRequest.NAME)) {
                final List<MviDao.Balance> list = SafeUtils.cast(result.getData());
                getViewData().setBalance(list);
                getModel().getView().refreshBalance(list);
            } else if (result.getName().equals(GetCurrencyRequest.NAME)) {
                getViewData().setCurrencies(SafeUtils.cast(result.getData()));
            }
        } else {
            SLUtil.getActivityUnion().showMessage(new ShowMessageEvent(result.getErrorText()).setType(ApplicationUtils.MESSAGE_TYPE_ERROR));
        }
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
        if (dialog.equals(sortDialog)) {
            getViewData().setSort(which);
        } else if (dialog.equals(filterDialog)) {
            if (which == 0) {
                getViewData().setFilter(null);
            } else {
                getViewData().setFilter(getViewData().getCurrencies().get(which - 1));
            }
        }
        getModel().getView().refreshViews(getViewData());
    }

    public void onClickAccounts(Account account) {
        getModel().getRouter().showAccount(account);
    }

    @Override
    public void onDestroyView() {
        SLUtil.getCacheSpecialist().put(AccountsViewData.NAME, getViewData());

        super.onDestroyView();
    }

    @Override
    public void onDialogResult(DialogResultEvent event) {
        final Bundle bundle = event.getResult();
        if (bundle != null && bundle.getInt("id", -1) == R.id.dialog_request_permissions) {
            final String button = bundle.getString(MaterialDialogExt.BUTTON);
            if (button != null && button.equalsIgnoreCase(MaterialDialogExt.POSITIVE)) {
                final Intent intent = new Intent();
                final String packageName = ApplicationController.getInstance().getPackageName();
                intent.setAction(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                intent.setData(Uri.parse("package:" + packageName));
                getModel().getView().startActivity(intent);
            }
        }
    }

    private AccountsViewData getViewData() {
        if (accountsViewData == null) {
            accountsViewData = new AccountsViewData();
        }
        return accountsViewData;
    }

    @Override
    public void showMessage(ShowMessageEvent event) {
        getModel().getView().showMessage(event);
    }
}

