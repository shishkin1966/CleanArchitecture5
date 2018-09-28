package shishkin.cleanarchitecture.mvi.app.viewdata;

import android.os.Parcel;
import android.os.Parcelable;


import java.util.List;


import shishkin.cleanarchitecture.mvi.app.data.Ticker;

public class TickerViewData implements Parcelable {

    public static final String NAME = TickerViewData.class.getName();

    private List<Ticker> tickers;
    private String filter;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(this.tickers);
        dest.writeString(this.filter);
    }

    public TickerViewData() {
    }

    public List<Ticker> getTickers() {
        return tickers;
    }

    public void setTickers(List<Ticker> tickers) {
        this.tickers = tickers;
    }

    public String getFilter() {
        return filter;
    }

    public void setFilter(String filter) {
        this.filter = filter;
    }

    protected TickerViewData(Parcel in) {
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
