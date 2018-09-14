package shishkin.cleanarchitecture.mvi.app.screen.fragment.create_account;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.jaredrummler.materialspinner.MaterialSpinner;


import shishkin.cleanarchitecture.mvi.R;
import shishkin.cleanarchitecture.mvi.app.data.Account;
import shishkin.cleanarchitecture.mvi.app.data.Currency;
import shishkin.cleanarchitecture.mvi.common.RippleTextView;
import shishkin.cleanarchitecture.mvi.common.utils.StringUtils;
import shishkin.cleanarchitecture.mvi.sl.observe.EditTextObservable;
import shishkin.cleanarchitecture.mvi.sl.ui.AbsContentFragment;

/**
 * Created by Shishkin on 17.03.2018.
 */

public class CreateAccountFragment extends AbsContentFragment<CreateAccountModel> implements CreateAccountView, View.OnClickListener {

    public static CreateAccountFragment newInstance() {
        return new CreateAccountFragment();
    }

    private MaterialSpinner mSpinner;
    private EditText mFriendlyNameView;
    private EditText mBalanceValueView;
    private RippleTextView mOpenAccountButton;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_open_account, container, false);
    }

    @Override
    public void onViewCreated(final View view, @Nullable final Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setModel(new CreateAccountModel(this));

        mSpinner = findView(R.id.balanceCurrencySpinner);
        mSpinner.setItems(Currency.RUR, Currency.USD, Currency.EUR, Currency.GBP, Currency.CHF);
        mSpinner.setSelectedIndex(0);

        mOpenAccountButton = findView(R.id.openAccountButton);
        mOpenAccountButton.setOnClickListener(this);

        mFriendlyNameView = findView(R.id.friendlyNameView);
        mBalanceValueView = findView(R.id.balanceValueView);

        new EditTextObservable(getModel().getPresenter(), mFriendlyNameView);
        new EditTextObservable(getModel().getPresenter(), mBalanceValueView);
    }

    @Override
    public String getName() {
        return CreateAccountFragment.class.getName();
    }

    @Override
    public void refresh() {
        if (!StringUtils.isNullOrEmpty(mFriendlyNameView.getText().toString()) && !StringUtils.isNullOrEmpty(mBalanceValueView.getText().toString())) {
            mOpenAccountButton.setEnabled(true);
        } else {
            mOpenAccountButton.setEnabled(false);
        }
    }

    @Override
    public void onClick(View v) {
        final Account account = new Account();
        account.setFriendlyName(StringUtils.allTrim(mFriendlyNameView.getText().toString()));
        account.setBalance(StringUtils.toDouble(mBalanceValueView.getText().toString()));
        account.setCurrency(mSpinner.getText().toString());
        getModel().getPresenter().createAccount(account);
    }
}
