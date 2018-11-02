package shishkin.cleanarchitecture.mvi.app.screen.fragment.portion_load;

import android.content.Context;
import androidx.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import shishkin.cleanarchitecture.mvi.R;
import shishkin.cleanarchitecture.mvi.app.data.Account;
import shishkin.cleanarchitecture.mvi.common.recyclerview.AbstractRecyclerViewAdapter;
import shishkin.cleanarchitecture.mvi.common.recyclerview.AbstractViewHolder;

public class PageRecyclerViewAdapter extends AbstractRecyclerViewAdapter<Account, PageRecyclerViewAdapter.ViewHolder> {

    public PageRecyclerViewAdapter(@NonNull Context context) {
        super(context);

        setHasStableIds(false);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent, int viewType) {
        final View view = inflater.inflate(R.layout.list_item_valute, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, Account item, int position) {
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
