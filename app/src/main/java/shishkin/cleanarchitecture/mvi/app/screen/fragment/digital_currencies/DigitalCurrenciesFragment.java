package shishkin.cleanarchitecture.mvi.app.screen.fragment.digital_currencies;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;


import shishkin.cleanarchitecture.mvi.R;
import shishkin.cleanarchitecture.mvi.app.SLUtil;
import shishkin.cleanarchitecture.mvi.app.adapter.TickerRecyclerViewAdapter;
import shishkin.cleanarchitecture.mvi.common.utils.ViewUtils;
import shishkin.cleanarchitecture.mvi.sl.observe.EditTextObservable;
import shishkin.cleanarchitecture.mvi.sl.ui.AbsContentFragment;

/**
 * Created by Shishkin on 17.03.2018.
 */

public class DigitalCurrenciesFragment extends AbsContentFragment<DigitalCurrenciesModel> implements DigitalCurrenciesView {

    public static DigitalCurrenciesFragment newInstance() {
        return new DigitalCurrenciesFragment();
    }

    private RecyclerView mRecyclerView;
    private EditText mSearchView;
    private TickerRecyclerViewAdapter mAdapter;
    private SwipeRefreshLayout mSwipeRefreshLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_digital_currencies, container, false);
    }

    @Override
    public void onViewCreated(final View view, @Nullable final Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mSwipeRefreshLayout = findView(R.id.swipeRefreshLayout);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.blue);
        mSwipeRefreshLayout.setProgressBackgroundColorSchemeResource(R.color.gray_light);
        mSwipeRefreshLayout.setOnRefreshListener(getModel().getPresenter());

        mAdapter = new TickerRecyclerViewAdapter(getContext());

        mSearchView = findView(R.id.search);
        mSearchView.setCompoundDrawablesWithIntrinsicBounds(ViewUtils.getVectorDrawable(getContext(), R.drawable.magnify, mSearchView.getContext().getTheme()), null, null, null);

        mRecyclerView = findView(R.id.list);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setAdapter(mAdapter);

        new EditTextObservable(getModel().getPresenter(), mSearchView);
        mSearchView.setText(getModel().getPresenter().getViewData().getFilter());
    }

    @Override
    public DigitalCurrenciesModel createModel() {
        return new DigitalCurrenciesModel(this);
    }

    @Override
    public String getName() {
        return DigitalCurrenciesFragment.class.getName();
    }

    @Override
    public void refreshViews(TickerViewData viewData) {
        if (viewData == null || viewData.getData() == null) return;
        mAdapter.setItems(viewData.getData());
    }

    @Override
    public void hideProgressBar() {
        if (validate()) {
            super.hideProgressBar();
            mSwipeRefreshLayout.setRefreshing(false);
        }
    }

    @Override
    public boolean onBackPressed() {
        SLUtil.getActivityUnion().switchToTopFragment();
        return true;
    }
}
