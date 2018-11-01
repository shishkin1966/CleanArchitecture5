package shishkin.cleanarchitecture.mvi.app.screen.fragment.paged_load;

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
import shishkin.cleanarchitecture.mvi.app.screen.fragment.portion_load.PageRecyclerViewAdapter;
import shishkin.cleanarchitecture.mvi.app.screen.fragment.portion_load.PagingViewData;
import shishkin.cleanarchitecture.mvi.sl.paginator.OnPagedScrollListener;
import shishkin.cleanarchitecture.mvi.sl.ui.AbsContentFragment;

/**
 * Created by Shishkin on 17.03.2018.
 */

public class PagedFragment extends AbsContentFragment<PagedModel> implements PagedView {

    public static final String NAME = PagedFragment.class.getName();

    public static PagedFragment newInstance() {
        return new PagedFragment();
    }

    private RecyclerView mRecyclerView;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private PageRecyclerViewAdapter adapter;
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

        adapter = new PageRecyclerViewAdapter(getContext());
        mRecyclerView.setAdapter(adapter);
        paginator = new AccountsPaginator(getModel().getPresenter());
        mRecyclerView.addOnScrollListener(new OnPagedScrollListener((LinearLayoutManager) mRecyclerView.getLayoutManager(), paginator));
    }

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public PagedModel createModel() {
        return new PagedModel(this);
    }

    @Override
    public boolean onBackPressed() {
        SLUtil.getViewUnion().switchToTopFragment();
        return true;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        paginator.stop();
        mRecyclerView.setAdapter(null);
    }

    @Override
    public void onRefresh() {
        paginator.reset();
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
