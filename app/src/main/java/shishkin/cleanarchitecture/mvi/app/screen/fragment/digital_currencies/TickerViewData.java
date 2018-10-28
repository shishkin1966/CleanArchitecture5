package shishkin.cleanarchitecture.mvi.app.screen.fragment.digital_currencies;

import android.os.Parcel;
import android.os.Parcelable;


import java.util.List;


import shishkin.cleanarchitecture.mvi.app.SLUtil;
import shishkin.cleanarchitecture.mvi.app.data.Ticker;
import shishkin.cleanarchitecture.mvi.common.utils.StringUtils;

public class TickerViewData implements Parcelable {

    public static final String NAME = TickerViewData.class.getName();

    private List<Ticker> tickers;
    private String filter;

    TickerViewData() {
    }

    public List<Ticker> getTickers() {
        return tickers;
    }

    void setTickers(List<Ticker> tickers) {
        this.tickers = tickers;
    }

    String getFilter() {
        return filter;
    }

    void setFilter(String filter) {
        this.filter = filter;
    }

    public List<Ticker> getData() {
        if (tickers == null) return null;

        if (StringUtils.isNullOrEmpty(filter)) {
            return SLUtil.getDataSpecialist().sort(tickers, (o1, o2) -> o1.getSymbol().compareTo(o2.getSymbol())).toList();
        } else {
            return (SLUtil.getDataSpecialist().sort(SLUtil.getDataSpecialist().filter(tickers, item -> StringUtils.containsIgnoreCase(item.getName(), filter)).toList(), (o1, o2) -> o1.getSymbol().compareTo(o2.getSymbol())).toList());
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(this.tickers);
        dest.writeString(this.filter);
    }

    private TickerViewData(Parcel in) {
        this.tickers = in.createTypedArrayList(Ticker.CREATOR);
        this.filter = in.readString();
    }

    public static final Parcelable.Creator<TickerViewData> CREATOR = new Parcelable.Creator<TickerViewData>() {
        @Override
        public TickerViewData createFromParcel(Parcel source) {
            return new TickerViewData(source);
        }

        @Override
        public TickerViewData[] newArray(int size) {
            return new TickerViewData[size];
        }
    };
}
