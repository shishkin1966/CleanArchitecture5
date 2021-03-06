package shishkin.cleanarchitecture.mvi.app.screen.fragment.accounts;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.google.android.material.bottomsheet.BottomSheetBehavior;


import net.cachapa.expandablelayout.ExpandableLayout;

import java.util.List;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import shishkin.cleanarchitecture.mvi.R;
import shishkin.cleanarchitecture.mvi.app.db.MviDao;
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
import shishkin.cleanarchitecture.mvi.sl.viewaction.ViewAction;

/**
 * Created by Shishkin on 17.03.2018.
 */

public class AccountsFragment extends AbsContentFragment<AccountsModel> implements AccountsView {

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
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_accounts, container, false);
    }

    @Override
    public void onViewCreated(@NonNull final View view, @Nullable final Bundle savedInstanceState) {
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
        mAdapter.setOnItemClickListener((v, position, item) -> getModel().getPresenter().onClickItems(item));
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

    private void refreshBalance(List<MviDao.Balance> list) {
        if (list == null) return;
        mBalanceAdapter.setItems(list);
    }

    private void collapseBottomSheet() {
        mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
    }

    private void refreshViews(AccountsViewData viewData) {
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

    private void hideMessage() {
        mExpandableLayout.collapse();
    }

    @Override
    public void doViewAction(ViewAction action) {
        super.doViewAction(action);

        switch (action.getName()) {
            case "hideMessage":
                hideMessage();
                break;

            case "refreshBalance":
                refreshBalance((List<MviDao.Balance>) action.getValue());
                break;

            case "collapseBottomSheet":
                collapseBottomSheet();
                break;

            case "refreshViews":
                refreshViews((AccountsViewData) action.getValue());
                break;

        }
    }
}
