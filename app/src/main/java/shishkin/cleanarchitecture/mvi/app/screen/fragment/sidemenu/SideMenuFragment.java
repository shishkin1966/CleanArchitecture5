package shishkin.cleanarchitecture.mvi.app.screen.fragment.sidemenu;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import java.util.List;


import shishkin.cleanarchitecture.mvi.R;
import shishkin.cleanarchitecture.mvi.app.adapter.BalanceRecyclerViewAdapter;
import shishkin.cleanarchitecture.mvi.app.db.MviDao;
import shishkin.cleanarchitecture.mvi.sl.ui.AbsFragment;

public class SideMenuFragment extends AbsFragment<SideMenuModel> implements SideMenuView {

    public static SideMenuFragment newInstance() {
        return new SideMenuFragment();
    }

    private RecyclerView mBalanceView;
    private BalanceRecyclerViewAdapter mBalanceAdapter;

    @Override
    public String getName() {
        return this.getClass().getName();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_sidemenu, container, false);
    }

    @Override
    public void onViewCreated(final View view, @Nullable final Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mBalanceAdapter = new BalanceRecyclerViewAdapter(getContext());
        mBalanceView = findView(R.id.accounts_balance_list);
        mBalanceView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mBalanceView.setAdapter(mBalanceAdapter);

        findView(R.id.exchange_rates).setOnClickListener(getModel().getPresenter());
        findView(R.id.exchange_cryptorates).setOnClickListener(getModel().getPresenter());
        findView(R.id.address).setOnClickListener(getModel().getPresenter());
        findView(R.id.setting).setOnClickListener(getModel().getPresenter());
        findView(R.id.accounts).setOnClickListener(getModel().getPresenter());
        findView(R.id.scanner).setOnClickListener(getModel().getPresenter());
        findView(R.id.calc).setOnClickListener(getModel().getPresenter());
        findView(R.id.paging).setOnClickListener(getModel().getPresenter());
        findView(R.id.contact).setOnClickListener(getModel().getPresenter());
    }

    @Override
    public SideMenuModel createModel() {
        return new SideMenuModel(this);
    }

    @Override
    public void accountsChanged(List<MviDao.Balance> list) {
        if (list == null) return;

        mBalanceAdapter.setItems(list);
    }
}
