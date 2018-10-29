package shishkin.cleanarchitecture.mvi.app.screen.fragment.paging_google;

import android.arch.paging.PagedListAdapter;
import android.support.annotation.NonNull;
import android.support.v7.util.DiffUtil;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import shishkin.cleanarchitecture.mvi.R;
import shishkin.cleanarchitecture.mvi.app.data.Account;
import shishkin.cleanarchitecture.mvi.common.recyclerview.AbstractViewHolder;

public class AccountsPagedListAdapter extends PagedListAdapter<Account, AccountsPagedListAdapter.ViewHolder> {

    protected AccountsPagedListAdapter(DiffUtil.ItemCallback<Account> diffUtilCallback) {
        super(diffUtilCallback);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_valute, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(getItem(position));
    }

    static class ViewHolder extends AbstractViewHolder {

        private TextView name;
        private TextView value;

        ViewHolder(@NonNull final View itemView) {
            super(itemView);

            name = findView(R.id.name);
            value = findView(R.id.value);
        }

        void bind(@NonNull final Account item) {
            name.setText(item.getFriendlyName());
            value.setText(item.getBalance().toString());
        }
    }

}
