package shishkin.cleanarchitecture.mvi.app.screen.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import shishkin.cleanarchitecture.mvi.R;
import shishkin.cleanarchitecture.mvi.app.SLUtil;
import shishkin.cleanarchitecture.mvi.app.data.Ticker;
import shishkin.cleanarchitecture.mvi.common.recyclerview.AbstractRecyclerViewAdapter;
import shishkin.cleanarchitecture.mvi.common.recyclerview.AbstractViewHolder;
import shishkin.cleanarchitecture.mvi.common.utils.StringUtils;
import shishkin.cleanarchitecture.mvi.common.utils.ViewUtils;

public class TickerRecyclerViewAdapter extends AbstractRecyclerViewAdapter<Ticker, TickerRecyclerViewAdapter.ViewHolder> {

    public TickerRecyclerViewAdapter(@NonNull Context context) {
        super(context);

        setHasStableIds(false);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent, int viewType) {
        final View view = inflater.inflate(R.layout.list_item_ticker, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, Ticker item, int position) {
        holder.bind(getItem(position));
    }

    static class ViewHolder extends AbstractViewHolder {

        private TextView symbol;
        private TextView name;
        private TextView money;
        private TextView hours;
        private TextView days;

        ViewHolder(@NonNull final View itemView) {
            super(itemView);

            symbol = findView(R.id.symbol);
            name = findView(R.id.name);
            money = findView(R.id.money);
            hours = findView(R.id.hours);
            days = findView(R.id.days);
        }

        void bind(@NonNull final Ticker item) {
            symbol.setText(item.getSymbol());
            name.setText(item.getName());
            money.setText(item.getPriceUsd() + "$");

            if (!StringUtils.isNullOrEmpty(item.getPercentChange24h())) {
                final Spannable s24h = new SpannableString("24h: " + item.getPercentChange24h() + "%");
                if (StringUtils.toFloat(item.getPercentChange24h()) > 0) {
                    s24h.setSpan(new ForegroundColorSpan(ViewUtils.getColor(SLUtil.getContext(), R.color.green)),
                            5, 6 + item.getPercentChange24h().length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                } else {
                    s24h.setSpan(new ForegroundColorSpan(ViewUtils.getColor(SLUtil.getContext(), R.color.red)),
                            5, 6 + item.getPercentChange24h().length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                }
                hours.setText(s24h);
            }

            if (!StringUtils.isNullOrEmpty(item.getPercentChange7d())) {
                final Spannable s7d = new SpannableString("7d: " + item.getPercentChange7d() + "%");
                if (StringUtils.toFloat(item.getPercentChange7d()) > 0) {
                    s7d.setSpan(new ForegroundColorSpan(ViewUtils.getColor(SLUtil.getContext(), R.color.green)),
                            4, 5 + item.getPercentChange7d().length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                } else {
                    s7d.setSpan(new ForegroundColorSpan(ViewUtils.getColor(SLUtil.getContext(), R.color.red)),
                            4, 5 + item.getPercentChange7d().length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                }
                days.setText(s7d);
            }
        }
    }

}
