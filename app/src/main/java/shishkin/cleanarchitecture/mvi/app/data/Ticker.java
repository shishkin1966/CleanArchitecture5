package shishkin.cleanarchitecture.mvi.app.data;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;


import java.util.Comparator;


import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.PrimaryKey;
import shishkin.cleanarchitecture.mvi.sl.data.AbsEntity;

/**
 * Created by Shishkin on 15.12.2017.
 */

public class Ticker extends AbsEntity implements Parcelable {

    public Ticker() {
    }

    @ColumnInfo(name = "id")
    @PrimaryKey
    @SerializedName("id")
    @NonNull
    private String id;

    @ColumnInfo(name = "name")
    @SerializedName("name")
    private String name;

    @ColumnInfo(name = "symbol")
    @SerializedName("symbol")
    private String symbol;

    @ColumnInfo(name = "rank")
    @SerializedName("rank")
    private String rank;

    @ColumnInfo(name = "priceUsd")
    @SerializedName("price_usd")
    private String priceUsd;

    @ColumnInfo(name = "priceBtc")
    @SerializedName("price_btc")
    private String priceBtc;

    @ColumnInfo(name = "volumeUsd")
    @SerializedName("24h_volume_usd")
    private String volumeUsd;

    @ColumnInfo(name = "marketCapUsd")
    @SerializedName("market_cap_usd")
    private String marketCapUsd;

    @ColumnInfo(name = "availableSupply")
    @SerializedName("available_supply")
    private String availableSupply;

    @ColumnInfo(name = "totalSupply")
    @SerializedName("total_supply")
    private String totalSupply;

    @ColumnInfo(name = "maxSupply")
    @SerializedName("max_supply")
    private String maxSupply;

    @ColumnInfo(name = "percentChange1h")
    @SerializedName("percent_change_1h")
    private String percentChange1h;

    @ColumnInfo(name = "percentChange24h")
    @SerializedName("percent_change_24h")
    private String percentChange24h;

    @ColumnInfo(name = "percentChange7d")
    @SerializedName("percent_change_7d")
    private String percentChange7d;

    @ColumnInfo(name = "lastUpdated")
    @SerializedName("last_updated")
    private String lastUpdated;

    @ColumnInfo(name = "favorite")
    @SerializedName("favorite")
    private int favorite;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public String getRank() {
        return rank;
    }

    public void setRank(String rank) {
        this.rank = rank;
    }

    public String getPriceUsd() {
        return priceUsd;
    }

    public void setPriceUsd(String priceUsd) {
        this.priceUsd = priceUsd;
    }

    public String getPriceBtc() {
        return priceBtc;
    }

    public void setPriceBtc(String priceBtc) {
        this.priceBtc = priceBtc;
    }

    public String getVolumeUsd() {
        return volumeUsd;
    }

    public void setVolumeUsd(String volumeUsd) {
        this.volumeUsd = volumeUsd;
    }

    public String getMarketCapUsd() {
        return marketCapUsd;
    }

    public void setMarketCapUsd(String marketCapUsd) {
        this.marketCapUsd = marketCapUsd;
    }

    public String getAvailableSupply() {
        return availableSupply;
    }

    public void setAvailableSupply(String availableSupply) {
        this.availableSupply = availableSupply;
    }

    public String getTotalSupply() {
        return totalSupply;
    }

    public void setTotalSupply(String totalSupply) {
        this.totalSupply = totalSupply;
    }

    public String getMaxSupply() {
        return maxSupply;
    }

    public void setMaxSupply(String maxSupply) {
        this.maxSupply = maxSupply;
    }

    public String getPercentChange1h() {
        return percentChange1h;
    }

    public void setPercentChange1h(String percentChange1h) {
        this.percentChange1h = percentChange1h;
    }

    public String getPercentChange24h() {
        return percentChange24h;
    }

    public void setPercentChange24h(String percentChange24h) {
        this.percentChange24h = percentChange24h;
    }

    public String getPercentChange7d() {
        return percentChange7d;
    }

    public void setPercentChange7d(String percentChange7d) {
        this.percentChange7d = percentChange7d;
    }

    public String getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(String lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    public int getFavorite() {
        return favorite;
    }

    public void setFavorite(int favorite) {
        this.favorite = favorite;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.name);
        dest.writeString(this.symbol);
        dest.writeString(this.rank);
        dest.writeString(this.priceUsd);
        dest.writeString(this.priceBtc);
        dest.writeString(this.volumeUsd);
        dest.writeString(this.marketCapUsd);
        dest.writeString(this.availableSupply);
        dest.writeString(this.totalSupply);
        dest.writeString(this.maxSupply);
        dest.writeString(this.percentChange1h);
        dest.writeString(this.percentChange24h);
        dest.writeString(this.percentChange7d);
        dest.writeString(this.lastUpdated);
        dest.writeInt(this.favorite);
    }

    protected Ticker(Parcel in) {
        this.id = in.readString();
        this.name = in.readString();
        this.symbol = in.readString();
        this.rank = in.readString();
        this.priceUsd = in.readString();
        this.priceBtc = in.readString();
        this.volumeUsd = in.readString();
        this.marketCapUsd = in.readString();
        this.availableSupply = in.readString();
        this.totalSupply = in.readString();
        this.maxSupply = in.readString();
        this.percentChange1h = in.readString();
        this.percentChange24h = in.readString();
        this.percentChange7d = in.readString();
        this.lastUpdated = in.readString();
        this.favorite = in.readInt();
    }

    public static final Parcelable.Creator<Ticker> CREATOR = new Parcelable.Creator<Ticker>() {
        @Override
        public Ticker createFromParcel(Parcel source) {
            return new Ticker(source);
        }

        @Override
        public Ticker[] newArray(int size) {
            return new Ticker[size];
        }
    };

    public static Comparator<Ticker> getComparator() {
        return (x, y) -> {
            int comp = (x.favorite < y.favorite) ? 1 : (x.favorite == y.favorite) ? 0 : -1;
            return comp == 0 ? x.symbol.compareTo(y.symbol) : comp;
        };
    }

}
