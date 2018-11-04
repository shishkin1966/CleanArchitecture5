package shishkin.cleanarchitecture.mvi.app.screen.fragment.paged_load;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import shishkin.cleanarchitecture.mvi.R;
import shishkin.cleanarchitecture.mvi.app.SLUtil;
import shishkin.cleanarchitecture.mvi.app.screen.fragment.portion_load.PageRecyclerViewAdapter;
import shishkin.cleanarchitecture.mvi.app.screen.fragment.portion_load.PagingViewData;
import shishkin.cleanarchitecture.mvi.sl.paginator.OnPagedScrollListener;
import shishkin.cleanarchitecture.mvi.sl.paginator.Paginator;
import shishkin.cleanarchitecture.mvi.sl.ui.AbsContentFragment;
import shishkin.cleanarchitecture.mvi.sl.viewaction.ViewAction;

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

        final Paginator paginator = SLUtil.getPaginatorUnion().getPaginator(AccountsPaginator.NAME);
        paginator.setListener(getModel().getPresenter().getName()).reset();
        SLUtil.getMessagerUnion().clearMail(getModel().getPresenter());
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
        return getModel().getPresenter().onBackPressed();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        mRecyclerView.setAdapter(null);
    }

    private void onRefresh() {
        SLUtil.getPaginatorUnion().getPaginator(AccountsPaginator.NAME).reset();
        adapter.clear();
        mSwipeRefreshLayout.setRefreshing(false);
    }

    private void refreshViews(PagingViewData viewData) {
        if (viewData != null && viewData.getAccounts() != null) {
            adapter.setItems(viewData.getAccounts());
        }
    }

    @Override
    public void doViewAction(ViewAction action) {
        super.doViewAction(action);

        switch (action.getName()) {
            case "onRefresh":
                onRefresh();
                break;

            case "refreshViews":
                refreshViews((PagingViewData) action.getValue());
                break;

        }
    }
}
