package shishkin.cleanarchitecture.mvi.app.screen.fragment.sidemenu;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import java.util.List;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import shishkin.cleanarchitecture.mvi.R;
import shishkin.cleanarchitecture.mvi.app.db.MviDao;
import shishkin.cleanarchitecture.mvi.app.screen.adapter.BalanceRecyclerViewAdapter;
import shishkin.cleanarchitecture.mvi.sl.ui.AbsFragment;
import shishkin.cleanarchitecture.mvi.sl.viewaction.ViewAction;

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
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_sidemenu, container, false);
    }

    @Override
    public void onViewCreated(@NonNull final View view, @Nullable final Bundle savedInstanceState) {
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
        findView(R.id.paging_google).setOnClickListener(getModel().getPresenter());
        findView(R.id.contact).setOnClickListener(getModel().getPresenter());
    }

    @Override
    public SideMenuModel createModel() {
        return new SideMenuModel(this);
    }

    private void accountsChanged(List<MviDao.Balance> list) {
        if (list == null) return;

        mBalanceAdapter.setItems(list);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        mBalanceView.setAdapter(null);
    }

    @Override
    public void doViewAction(ViewAction action) {
        super.doViewAction(action);

        switch (action.getName()) {
            case "accountsChanged":
                accountsChanged((List<MviDao.Balance>) action.getValue());
                break;

        }
    }

}
