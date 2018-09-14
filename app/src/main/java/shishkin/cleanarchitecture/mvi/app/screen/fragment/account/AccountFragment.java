package shishkin.cleanarchitecture.mvi.app.screen.fragment.account;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import shishkin.cleanarchitecture.mvi.R;
import shishkin.cleanarchitecture.mvi.app.data.Account;
import shishkin.cleanarchitecture.mvi.app.screen.fragment.create_account.CreateAccountModel;
import shishkin.cleanarchitecture.mvi.common.utils.StringUtils;
import shishkin.cleanarchitecture.mvi.sl.ui.AbsContentFragment;

/**
 * Created by Shishkin on 17.03.2018.
 */

public class AccountFragment extends AbsContentFragment<CreateAccountModel> implements AccountView {

    public static AccountFragment newInstance(final Account account) {
        final AccountFragment f = new AccountFragment();
        final Bundle bundle = new Bundle();
        bundle.putParcelable("account", account);
        f.setArguments(bundle);
        return f;
    }

    private Account mAccount;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_account, container, false);
    }

    @Override
    public void onViewCreated(final View view, @Nullable final Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setModel(new AccountModel(this));

        mAccount = getArguments().getParcelable("account");

        ((TextView) findView(R.id.title)).setText(mAccount.getFriendlyName());
        ((TextView) findView(R.id.balanceView)).setText(String.format("%,.0f", mAccount.getBalance()) + " " + mAccount.getCurrency());
        ((TextView) findView(R.id.openDateView)).setText(getString(R.string.fragment_account_open_date_format) + " " + StringUtils.formatDateShortRu(mAccount.getOpenDate()));
    }

    @Override
    public String getName() {
        return AccountFragment.class.getName();
    }

}
