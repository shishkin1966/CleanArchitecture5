package shishkin.cleanarchitecture.mvi.app.screen.fragment.paging_google;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import shishkin.cleanarchitecture.mvi.R;
import shishkin.cleanarchitecture.mvi.app.SLUtil;
import shishkin.cleanarchitecture.mvi.app.screen.fragment.paging.PagingRecyclerViewAdapter;
import shishkin.cleanarchitecture.mvi.app.screen.fragment.paging.PagingViewData;
import shishkin.cleanarchitecture.mvi.sl.paged.OnPagedScrollListener;
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
    private PagingRecyclerViewAdapter adapter;
    private AccountsPaginator paginator;

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

        adapter = new PagingRecyclerViewAdapter(getContext());
        mRecyclerView.setAdapter(adapter);
        paginator = new AccountsPaginator(getModel().getPresenter());
        mRecyclerView.addOnScrollListener(new OnPagedScrollListener((LinearLayoutManager) mRecyclerView.getLayoutManager(), paginator));
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

        mRecyclerView.setAdapter(null);
    }

    @Override
    public void onRefresh() {
        paginator.clear();
        adapter.clear();
        mSwipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void refreshViews(PagingViewData viewData) {
        if (viewData != null && viewData.getAccounts() != null) {
            adapter.setItems(viewData.getAccounts());
        }
    }

}
