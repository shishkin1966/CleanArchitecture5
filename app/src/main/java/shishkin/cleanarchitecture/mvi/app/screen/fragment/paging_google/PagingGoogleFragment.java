package shishkin.cleanarchitecture.mvi.app.screen.fragment.paging_google;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import shishkin.cleanarchitecture.mvi.R;
import shishkin.cleanarchitecture.mvi.app.SLUtil;
import shishkin.cleanarchitecture.mvi.app.data.Account;
import shishkin.cleanarchitecture.mvi.sl.ui.AbsContentFragment;

/**
 * Created by Shishkin on 17.03.2018.
 */

public class PagingGoogleFragment extends AbsContentFragment<PagingGoogleModel> implements PagingGoogleView {

    public static final String NAME = PagingGoogleFragment.class.getName();

    public static PagingGoogleFragment newInstance() {
        return new PagingGoogleFragment();
    }

    private RecyclerView mRecyclerView;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private AccountsPagedListAdapter adapter;
    private DiffUtil.ItemCallback<Account> diffUtilCallback = new DiffUtil.ItemCallback<Account>() {
        @Override
        public boolean areItemsTheSame(Account oldItem, Account newItem) {
            return oldItem.equals(newItem);
        }

        @Override
        public boolean areContentsTheSame(Account oldItem, Account newItem) {
            return oldItem.getFriendlyName().equals(newItem.getFriendlyName());
        }
    };

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_paging_google_accounts, container, false);
    }

    @Override
    public void onViewCreated(@NonNull final View view, @Nullable final Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mSwipeRefreshLayout = findView(R.id.swipeRefreshLayout);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.blue);
        mSwipeRefreshLayout.setProgressBackgroundColorSchemeResource(R.color.gray_light);
        mSwipeRefreshLayout.setOnRefreshListener(getModel().getPresenter());

        mRecyclerView = findView(R.id.list);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());

        adapter = new AccountsPagedListAdapter(diffUtilCallback);
        mRecyclerView.setAdapter(adapter);
    }

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public PagingGoogleModel createModel() {
        return new PagingGoogleModel(this);
    }

    @Override
    public boolean onBackPressed() {
        SLUtil.getViewUnion().switchToTopFragment();
        return true;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        adapter.invalidate();
        mRecyclerView.setAdapter(null);
    }

    @Override
    public void onRefresh() {
        adapter.refresh();
        mSwipeRefreshLayout.setRefreshing(false);
    }
}
