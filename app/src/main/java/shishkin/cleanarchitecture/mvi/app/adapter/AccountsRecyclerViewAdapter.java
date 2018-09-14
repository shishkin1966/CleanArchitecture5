package shishkin.cleanarchitecture.mvi.app.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import shishkin.cleanarchitecture.mvi.R;
import shishkin.cleanarchitecture.mvi.app.data.Account;
import shishkin.cleanarchitecture.mvi.common.recyclerview.AbstractRecyclerViewAdapter;
import shishkin.cleanarchitecture.mvi.common.recyclerview.AbstractViewHolder;

public class AccountsRecyclerViewAdapter extends AbstractRecyclerViewAdapter<Account, AccountsRecyclerViewAdapter.ViewHolder> {

    public AccountsRecyclerViewAdapter(@NonNull Context context) {
        super(context);

        setHasStableIds(false);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent, int viewType) {
        final View view = inflater.inflate(R.layout.list_item_account, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, Account item, int position) {
        holder.bind(getItem(position));
    }

    static class ViewHolder extends AbstractViewHolder {

        private TextView friendlyNameView;
        private TextView balanceView;

        ViewHolder(@NonNull final View itemView) {
            super(itemView);

            friendlyNameView = findView(R.id.friendlyNameView);
            balanceView = findView(R.id.balanceView);
        }

        void bind(@NonNull final Account item) {
            friendlyNameView.setText(item.getFriendlyName());
            balanceView.setText(String.format("%,.0f", item.getBalance()) + " " + item.getCurrency());
        }
    }

}
