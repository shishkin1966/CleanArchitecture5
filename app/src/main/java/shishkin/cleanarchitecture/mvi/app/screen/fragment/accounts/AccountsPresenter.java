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
import shishkin.cleanarchitecture.mvi.app.observe.AccountsBalanceListener;
import shishkin.cleanarchitecture.mvi.app.request.GetAccountsRequest;
import shishkin.cleanarchitecture.mvi.app.request.GetCurrencyRequest;
import shishkin.cleanarchitecture.mvi.common.utils.ApplicationUtils;
import shishkin.cleanarchitecture.mvi.common.utils.SafeUtils;
import shishkin.cleanarchitecture.mvi.sl.data.Result;
import shishkin.cleanarchitecture.mvi.sl.event.DialogResultEvent;
import shishkin.cleanarchitecture.mvi.sl.event.ShowMessageEvent;
import shishkin.cleanarchitecture.mvi.sl.presenter.AbsPresenter;
import shishkin.cleanarchitecture.mvi.sl.request.ResponseListener;
import shishkin.cleanarchitecture.mvi.sl.ui.DialogResultListener;
import shishkin.cleanarchitecture.mvi.sl.ui.MaterialDialogExt;
import shishkin.cleanarchitecture.mvi.sl.ui.Messager;
import shishkin.cleanarchitecture.mvi.sl.viewaction.ViewAction;

/**
 * Created by Shishkin on 17.03.2018.
 */

public class AccountsPresenter extends AbsPresenter<AccountsModel> implements ResponseListener, DialogInterface.OnClickListener, DialogResultListener, Messager, AccountsBalanceListener {

    public static final String NAME = AccountsPresenter.class.getName();
    private static final String ALL = ApplicationController.getInstance().getString(R.string.all);

    private DialogInterface sortDialog;
    private DialogInterface filterDialog;
    private AccountsViewData viewData;

    AccountsPresenter(AccountsModel model) {
        super(model);

        viewData = SLUtil.getCacheSpecialist().get(AccountsViewData.NAME, AccountsViewData.class);
        if (viewData == null) {
            viewData = new AccountsViewData();
        }
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
        getModel().getView().doViewAction(new ViewAction("collapseBottomSheet"));
        switch (id) {
            case R.id.create_account:
                getModel().getRouter().createAccount();
                break;

            case R.id.sort_accounts:
                sortAccounts();
                break;

            case R.id.select_accounts:
                selectAccounts();
                break;

            case R.id.select_accounts_all:
                selectAccountsAll();
                break;

            case R.id.start:
                if (SLUtil.getMediaSpecialist().isStopMedia()) {
                    SLUtil.getMediaSpecialist().playMedia(R.raw.music);
                } else {
                    SLUtil.getMediaSpecialist().resumeMedia();
                }
                break;

            case R.id.pause:
                SLUtil.getMediaSpecialist().pauseMedia();
                break;

            case R.id.stop:
                SLUtil.getMediaSpecialist().stop();
                break;

            case R.id.message:
                getModel().getRouter().showUrl();
                break;

        }
    }

    private void sortAccounts() {
        if (getModel().getView().getActivity() != null) {
            final BottomSheet.Builder builder = new BottomSheet.Builder(getModel().getView().getActivity());
            sortDialog = builder
                    .setDividers(true)
                    .setTitleTextColorRes(R.color.blue)
                    .setTitle(R.string.sort)
                    .setMenu(R.menu.sort_menu, this)
                    .show();
        }
    }

    private void selectAccounts() {
        if (viewData.getCurrencies() != null && viewData.getCurrencies().size() > 1) {
            final CharSequence[] items = new CharSequence[viewData.getCurrencies().size() + 1];
            final Drawable[] icons = new Drawable[viewData.getCurrencies().size() + 1];
            items[0] = ALL;
            for (int i = 0; i < viewData.getCurrencies().size(); i++) {
                items[i + 1] = viewData.getCurrencies().get(i);
            }
            if (getModel().getView().getActivity() != null) {
                final BottomSheet.Builder builder = new BottomSheet.Builder(getModel().getView().getActivity());
                filterDialog = builder
                        .setDividers(true)
                        .setTitleTextColorRes(R.color.blue)
                        .setTitle(R.string.select)
                        .setItems(items, icons, this)
                        .show();
            }
        }
    }

    private void selectAccountsAll() {
        viewData.setFilter(null);
        getModel().getView().doViewAction(new ViewAction("refreshViews", viewData));
    }

    @Override
    public void onStart() {
        getModel().getView().doViewAction(new ViewAction("refreshViews", viewData));
        getData();
    }

    @Override
    public void onResumeView() {
        super.onResumeView();

        if (!ApplicationUtils.checkPermission(SLUtil.getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            SLUtil.getViewUnion().grantPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
    }

    private void getData() {
        getModel().getView().showProgressBar();
        getModel().getInteractor().getCurrency(this);
        getModel().getInteractor().getAccounts(this);
    }

    @Override
    public void response(Result result) {
        if (!validate()) return;

        getModel().getView().hideProgressBar();
        if (!result.hasError()) {
            if (result.getName().equals(GetAccountsRequest.NAME)) {
                viewData.setAccounts(SafeUtils.cast(result.getData()));
                getModel().getView().doViewAction(new ViewAction("refreshViews", viewData));
            } else if (result.getName().equals(GetCurrencyRequest.NAME)) {
                viewData.setCurrencies(SafeUtils.cast(result.getData()));
            }
        } else {
            SLUtil.onError(NAME, result.getErrorText(), true);
        }
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
        if (dialog.equals(sortDialog)) {
            viewData.setSort(which);
        } else if (dialog.equals(filterDialog)) {
            if (which == 0) {
                viewData.setFilter(null);
            } else {
                viewData.setFilter(viewData.getCurrencies().get(which - 1));
            }
        }
        getModel().getView().doViewAction(new ViewAction("refreshViews", viewData));
    }

    void onClickItems(Account item) {
        getModel().getRouter().showAccount(item);
    }

    @Override
    public void onStop() {
        SLUtil.getCacheSpecialist().put(AccountsViewData.NAME, viewData);
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

    @Override
    public void showMessage(ShowMessageEvent event) {
        viewData.setMessage(event.getMessage());
        viewData.setMessageType(event.getType());
        viewData.setShowMessage(true);
        getModel().getView().doViewAction(new ViewAction("showMessage", event));
    }

    void hideMessage() {
        viewData.setShowMessage(false);
        getModel().getView().doViewAction(new ViewAction("hideMessage"));
    }

    @Override
    public void showAccountsBalance(List<MviDao.Balance> list) {
        viewData.setBalance(list);
        getModel().getView().doViewAction(new ViewAction("refreshBalance", list));
    }
}

