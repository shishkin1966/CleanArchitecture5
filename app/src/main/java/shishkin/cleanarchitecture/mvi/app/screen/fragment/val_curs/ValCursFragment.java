package shishkin.cleanarchitecture.mvi.app.screen.fragment.val_curs;

import android.os.Bundle;
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
import shishkin.cleanarchitecture.mvi.app.adapter.ValCursRecyclerViewAdapter;
import shishkin.cleanarchitecture.mvi.app.viewdata.ValCursViewData;
import shishkin.cleanarchitecture.mvi.sl.ui.AbsContentFragment;

/**
 * Created by Shishkin on 17.03.2018.
 */

public class ValCursFragment extends AbsContentFragment<ValCursModel> implements ValCursView {

    public static ValCursFragment newInstance() {
        return new ValCursFragment();
    }

    private ValCursRecyclerViewAdapter mAdapter;
    private RecyclerView mRecyclerView;
    private SwipeRefreshLayout mSwipeRefreshLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_val_curs, container, false);
    }

    @Override
    public void onViewCreated(final View view, @Nullable final Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mSwipeRefreshLayout = findView(R.id.swipeRefreshLayout);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.blue);
        mSwipeRefreshLayout.setProgressBackgroundColorSchemeResource(R.color.gray_light);
        mSwipeRefreshLayout.setOnRefreshListener(getModel().getPresenter());

        mAdapter = new ValCursRecyclerViewAdapter(getContext());

        mRecyclerView = findView(R.id.list);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    public String getName() {
        return ValCursFragment.class.getName();
    }

    @Override
    public ValCursModel createModel() {
        return new ValCursModel(this);
    }

    @Override
    public void refreshViews(ValCursViewData viewData) {
        if (viewData != null && viewData.getData() != null) {
            mAdapter.setItems(viewData.getData());
        }
    }

    @Override
    public void hideProgressBar() {
        if (validate()) {
            super.hideProgressBar();
            mSwipeRefreshLayout.setRefreshing(false);
        }
    }
}
