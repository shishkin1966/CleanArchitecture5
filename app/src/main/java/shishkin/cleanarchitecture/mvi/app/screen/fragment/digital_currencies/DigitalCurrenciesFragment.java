package shishkin.cleanarchitecture.mvi.app.screen.fragment.digital_currencies;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;


import java.util.List;


import shishkin.cleanarchitecture.mvi.R;
import shishkin.cleanarchitecture.mvi.app.SLUtil;
import shishkin.cleanarchitecture.mvi.app.adapter.TickerRecyclerViewAdapter;
import shishkin.cleanarchitecture.mvi.app.data.Ticker;
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

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_digital_currencies, container, false);
    }

    @Override
    public void onViewCreated(final View view, @Nullable final Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mAdapter = new TickerRecyclerViewAdapter(getContext());

        setModel(new DigitalCurrenciesModel(this));

        mSearchView = findView(R.id.search);
        mSearchView.setCompoundDrawablesWithIntrinsicBounds(ViewUtils.getVectorDrawable(getContext(), R.drawable.magnify, mSearchView.getContext().getTheme()), null, null, null);
        new EditTextObservable(getModel().getPresenter(), mSearchView);
        mSearchView.setText(SLUtil.getPreferencesSpecialist().getString(DigitalCurrenciesPresenter.FILTER_KEY, null));

        mRecyclerView = findView(R.id.list);
        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    public String getName() {
        return DigitalCurrenciesFragment.class.getName();
    }

    @Override
    public void refreshTickers(List<Ticker> list) {
        if (list == null) return;
        mAdapter.setItems(list);
    }
}
