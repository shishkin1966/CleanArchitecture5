package shishkin.cleanarchitecture.mvi.app.screen.fragment.accounts;

import android.Manifest;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import java.util.List;


import shishkin.cleanarchitecture.mvi.R;
import shishkin.cleanarchitecture.mvi.app.ApplicationController;
import shishkin.cleanarchitecture.mvi.app.SLUtil;
import shishkin.cleanarchitecture.mvi.app.adapter.AccountsRecyclerViewAdapter;
import shishkin.cleanarchitecture.mvi.app.adapter.BalanceRecyclerViewAdapter;
import shishkin.cleanarchitecture.mvi.app.db.MviDao;
import shishkin.cleanarchitecture.mvi.app.viewdata.AccountsViewData;
import shishkin.cleanarchitecture.mvi.common.utils.ApplicationUtils;
import shishkin.cleanarchitecture.mvi.common.utils.StringUtils;
import shishkin.cleanarchitecture.mvi.sl.MailSubscriber;
import shishkin.cleanarchitecture.mvi.sl.MailUnionImpl;
import shishkin.cleanarchitecture.mvi.sl.event.DialogResultEvent;
import shishkin.cleanarchitecture.mvi.sl.presenter.OnBackPressedPresenter;
import shishkin.cleanarchitecture.mvi.sl.ui.AbsContentFragment;
import shishkin.cleanarchitecture.mvi.sl.ui.DialogResultListener;
import shishkin.cleanarchitecture.mvi.sl.ui.MaterialDialogExt;

/**
 * Created by Shishkin on 17.03.2018.
 */

public class AccountsFragment extends AbsContentFragment<AccountsModel> implements AccountsView, View.OnClickListener, DialogResultListener, MailSubscriber {

    public static final String NAME = AccountsFragment.class.getName();

    private OnBackPressedPresenter mOnBackPressedPresenter = new OnBackPressedPresenter();
    private RecyclerView mAccountsView;
    private RecyclerView mBalanceView;
    private AccountsRecyclerViewAdapter mAdapter;
    private BalanceRecyclerViewAdapter mBalanceAdapter;
    private BottomSheetBehavior mBottomSheetBehavior;

    public static AccountsFragment newInstance() {
        return new AccountsFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_accounts, container, false);
    }

    @Override
    public void onViewCreated(final View view, @Nullable final Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        addStateObserver(mOnBackPressedPresenter);

        mBottomSheetBehavior = BottomSheetBehavior.from(findView(R.id.bottomSheetContainer));

        findView(R.id.create_account).setOnClickListener(this);
        findView(R.id.map).setOnClickListener(this);
        findView(R.id.accounts_transfer).setOnClickListener(this);
        findView(R.id.sort_accounts).setOnClickListener(this);
        findView(R.id.select_accounts).setOnClickListener(this);
        findView(R.id.select_accounts_all).setOnClickListener(this);

        mAdapter = new AccountsRecyclerViewAdapter(getContext());
        mAccountsView = findView(R.id.list);
        mAccountsView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mAccountsView.setItemAnimator(new DefaultItemAnimator());
        mAdapter.setOnItemClickListener((v, position, item) -> {
            getModel().getPresenter().onClickAccounts(item);
        });
        mAccountsView.setAdapter(mAdapter);

        mBalanceAdapter = new BalanceRecyclerViewAdapter(getContext());
        mBalanceView = findView(R.id.balance_list);
        mBalanceView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mBalanceView.setAdapter(mBalanceAdapter);
    }

    @Override
    public AccountsModel createModel() {
        return new AccountsModel(this);
    }

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public void onResume() {
        super.onResume();

        if (!ApplicationUtils.checkPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION)) {
            grantPermission(NAME, Manifest.permission.ACCESS_FINE_LOCATION, "Право необходимо для показа карты");
        }
    }

    @Override
    public void onPermisionGranted(final String permission) {
        if (Manifest.permission.ACCESS_FINE_LOCATION.equals(permission)) {
            SLUtil.getLocationUnion().start();
        }
    }

    @Override
    public boolean onBackPressed() {
        mOnBackPressedPresenter.onClick();
        return true;
    }

    @Override
    public boolean isTop() {
        return true;
    }

    @Override
    public void onClick(View v) {
        if (v != null && validate()) {
            getModel().getPresenter().onClick(v.getId());
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        mAccountsView.setAdapter(null);
        mBalanceView.setAdapter(null);
    }


    @Override
    public void refreshBalance(List<MviDao.Balance> list) {
        if (list == null) return;
        mBalanceAdapter.setItems(list);
    }

    @Override
    public void collapseBottomSheet() {
        mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
    }

    @Override
    public void refreshViews(AccountsViewData viewData) {
        if (viewData == null || viewData.getData() == null) return;

        mAdapter.setItems(viewData.getData());
        findView(R.id.sort_accounts).setEnabled(viewData.isSortMenuEnabled());
        findView(R.id.select_accounts).setEnabled(viewData.isFilterMenuEnabled());
        if (viewData.isFilterMenuEnabled() && !StringUtils.isNullOrEmpty(viewData.getFilter())) {
            findView(R.id.select_accounts_all_ll).setVisibility(View.VISIBLE);
        } else {
            findView(R.id.select_accounts_all_ll).setVisibility(View.GONE);
        }
        refreshBalance(viewData.getBalance());
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
                startActivity(intent);
            }
        }
    }

    @Override
    public List<String> getSpecialistSubscription() {
        return StringUtils.arrayToList(MailUnionImpl.NAME);
    }
}
