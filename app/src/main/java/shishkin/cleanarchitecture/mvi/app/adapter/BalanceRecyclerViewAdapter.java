package shishkin.cleanarchitecture.mvi.app.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import shishkin.cleanarchitecture.mvi.R;
import shishkin.cleanarchitecture.mvi.app.db.MviDao;
import shishkin.cleanarchitecture.mvi.common.recyclerview.AbstractRecyclerViewAdapter;
import shishkin.cleanarchitecture.mvi.common.recyclerview.AbstractViewHolder;

public class BalanceRecyclerViewAdapter extends AbstractRecyclerViewAdapter<MviDao.Balance, BalanceRecyclerViewAdapter.ViewHolder> {

    public BalanceRecyclerViewAdapter(@NonNull Context context) {
        super(context);

        setHasStableIds(false);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent, int viewType) {
        final View view = inflater.inflate(R.layout.list_item_balance, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, MviDao.Balance item, int position) {
        holder.bind(getItem(position));
    }

    static class ViewHolder extends AbstractViewHolder {

        private TextView balance;

        ViewHolder(@NonNull final View itemView) {
            super(itemView);

            balance = findView(R.id.balance);
        }

        void bind(@NonNull final MviDao.Balance item) {
            balance.setText(String.format("%,.0f", item.balance) + " " + item.currency);
        }
    }

}
