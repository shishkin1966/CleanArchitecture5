package shishkin.cleanarchitecture.mvi.app.screen.fragment.accounts;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;


import net.cachapa.expandablelayout.ExpandableLayout;

import java.util.List;


import shishkin.cleanarchitecture.mvi.R;
import shishkin.cleanarchitecture.mvi.app.db.MviDao;
import shishkin.cleanarchitecture.mvi.app.screen.adapter.AccountsRecyclerViewAdapter;
import shishkin.cleanarchitecture.mvi.app.screen.adapter.BalanceRecyclerViewAdapter;
import shishkin.cleanarchitecture.mvi.common.LinearLayoutBehavior;
import shishkin.cleanarchitecture.mvi.common.OnSwipeTouchListener;
import shishkin.cleanarchitecture.mvi.common.RippleTextView;
import shishkin.cleanarchitecture.mvi.common.utils.ApplicationUtils;
import shishkin.cleanarchitecture.mvi.common.utils.StringUtils;
import shishkin.cleanarchitecture.mvi.common.utils.ViewUtils;
import shishkin.cleanarchitecture.mvi.sl.event.ShowMessageEvent;
import shishkin.cleanarchitecture.mvi.sl.presenter.OnBackPressedPresenter;
import shishkin.cleanarchitecture.mvi.sl.ui.AbsContentFragment;

/**
 * Created by Shishkin on 17.03.2018.
 */

public class AccountsFragment extends AbsContentFragment<AccountsModel> implements AccountsView, View.OnClickListener {

    public static final String NAME = AccountsFragment.class.getName();

    private OnBackPressedPresenter mOnBackPressedPresenter = new OnBackPressedPresenter();
    private RecyclerView mAccountsView;
    private RecyclerView mBalanceView;
    private AccountsRecyclerViewAdapter mAdapter;
    private BalanceRecyclerViewAdapter mBalanceAdapter;
    private BottomSheetBehavior mBottomSheetBehavior;
    private ExpandableLayout mExpandableLayout;
    private RippleTextView mMessage;

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

        mBottomSheetBehavior = LinearLayoutBehavior.from(findView(R.id.bottomSheetContainer));

        findView(R.id.create_account).setOnClickListener(this);
        findView(R.id.sort_accounts).setOnClickListener(this);
        findView(R.id.select_accounts).setOnClickListener(this);
        findView(R.id.select_accounts_all).setOnClickListener(this);
        findView(R.id.stop).setOnClickListener(this);
        findView(R.id.start).setOnClickListener(this);
        findView(R.id.pause).setOnClickListener(this);
        mExpandableLayout = findView(R.id.expandable_layout);
        mMessage = findView(R.id.message);
        mMessage.setOnClickListener(this);
        mMessage.setOnTouchListener(new OnSwipeTouchListener(mMessage.getContext()) {
            @Override
            public void onSwipeRight() {
                final Animation animation = AnimationUtils.loadAnimation(mMessage.getContext(), R.anim.slide);
                mMessage.startAnimation(animation);
                mMessage.postDelayed(() -> getModel().getPresenter().hideMessage(), 200);
            }
        });

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
        if (viewData.isShowMessage()) {
            showMessage(new ShowMessageEvent(viewData.getMessage(), viewData.getMessageType()));
        } else {
            mExpandableLayout.collapse();
        }
    }

    @Override
    public void showMessage(ShowMessageEvent event) {
        if (event == null) return;

        mMessage.setText(event.getMessage());
        if (event.getType() == ApplicationUtils.MESSAGE_TYPE_ERROR) {
            mMessage.setBackgroundDrawable(ViewUtils.getDrawable(mMessage.getContext(), R.color.orange));
        } else {
            mMessage.setBackgroundDrawable(ViewUtils.getDrawable(mMessage.getContext(), R.drawable.rectangle_gray));
        }
        mExpandableLayout.expand();
    }

    @Override
    public void hideMessage() {
        mExpandableLayout.collapse();
    }
}
