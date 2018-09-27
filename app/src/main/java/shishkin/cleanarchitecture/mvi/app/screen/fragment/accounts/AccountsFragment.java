package shishkin.cleanarchitecture.mvi.app.screen.fragment.accounts;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import java.util.List;


import shishkin.cleanarchitecture.mvi.R;
import shishkin.cleanarchitecture.mvi.app.adapter.AccountsRecyclerViewAdapter;
import shishkin.cleanarchitecture.mvi.app.adapter.BalanceRecyclerViewAdapter;
import shishkin.cleanarchitecture.mvi.app.data.Account;
import shishkin.cleanarchitecture.mvi.app.db.MviDao;
import shishkin.cleanarchitecture.mvi.sl.presenter.OnBackPressedPresenter;
import shishkin.cleanarchitecture.mvi.sl.ui.AbsContentFragment;

/**
 * Created by Shishkin on 17.03.2018.
 */

public class AccountsFragment extends AbsContentFragment<AccountsModel> implements AccountsView, View.OnClickListener {

    private OnBackPressedPresenter mOnBackPressedPresenter = new OnBackPressedPresenter();
    private RecyclerView mAccountsView;
    private RecyclerView mBalanceView;
    private AccountsRecyclerViewAdapter mAdapter;
    private BalanceRecyclerViewAdapter mBalanceAdapter;

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

        setModel(new AccountsModel(this));

        addStateObserver(mOnBackPressedPresenter);

        findView(R.id.create_account).setOnClickListener(this);
        findView(R.id.map).setOnClickListener(this);
        findView(R.id.accounts_transfer).setOnClickListener(this);
        findView(R.id.sort_accounts).setOnClickListener(this);
        findView(R.id.select_accounts).setOnClickListener(this);

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
    public String getName() {
        return AccountsFragment.class.getName();
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
    public void refreshAccounts(List<Account> list) {
        mAdapter.setItems(list);
    }

    @Override
    public void refreshBalance(List<MviDao.Balance> list) {
        mBalanceAdapter.setItems(list);
    }

}
