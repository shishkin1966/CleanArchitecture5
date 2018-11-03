package shishkin.cleanarchitecture.mvi.app.data;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;


import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import shishkin.cleanarchitecture.mvi.sl.data.AbsEntity;

/**
 * Created by Shishkin on 10.01.2018.
 */

@Entity(tableName = Account.TABLE)
public class Account extends AbsEntity implements Parcelable {

    public static final String TABLE = "Account";

    public interface Columns {
        String id = "id";
        String openDate = "openDate";
        String friendlyName = "friendlyName";
        String balance = "balance";
        String currency = "currency";
    }

    public static final String[] PROJECTION = {
            Columns.id,
            Columns.openDate,
            Columns.friendlyName,
            Columns.balance,
            Columns.currency
    };

    @ColumnInfo(name = "id")
    @PrimaryKey(autoGenerate = true)
    @SerializedName("id")
    @NonNull
    private Long mId;

    @ColumnInfo(name = Columns.openDate)
    @SerializedName(Columns.openDate)
    @NonNull
    private Long mOpenDate;

    @ColumnInfo(name = Columns.friendlyName)
    @SerializedName(Columns.friendlyName)
    private String mFriendlyName;

    @ColumnInfo(name = Columns.balance)
    @SerializedName(Columns.balance)
    private Double mBalance;

    @ColumnInfo(name = Columns.currency)
    @SerializedName(Columns.currency)
    private String mCurrency;

    public Account() {
        mBalance = 0.00d;
        mCurrency = Currency.RUR;
    }

    @NonNull
    public Long getId() {
        return mId;
    }

    public void setId(@NonNull Long id) {
        this.mId = id;
    }

    public Long getOpenDate() {
        return mOpenDate;
    }

    public void setOpenDate(@NonNull Long openDate) {
        this.mOpenDate = openDate;
    }

    public String getFriendlyName() {
        return mFriendlyName;
    }

    public void setFriendlyName(String friendlyName) {
        this.mFriendlyName = friendlyName;
    }

    public Double getBalance() {
        return mBalance;
    }

    public void setBalance(Double balance) {
        this.mBalance = balance;
    }

    public String getCurrency() {
        return mCurrency;
    }

    public void setCurrency(String currency) {
        this.mCurrency = currency;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(this.mId);
        dest.writeValue(this.mOpenDate);
        dest.writeString(this.mFriendlyName);
        dest.writeValue(this.mBalance);
        dest.writeString(this.mCurrency);
    }

    protected Account(Parcel in) {
        this.mId = (Long) in.readValue(Long.class.getClassLoader());
        this.mOpenDate = (Long) in.readValue(Long.class.getClassLoader());
        this.mFriendlyName = in.readString();
        this.mBalance = (Double) in.readValue(Double.class.getClassLoader());
        this.mCurrency = in.readString();
    }

    public static final Parcelable.Creator<Account> CREATOR = new Parcelable.Creator<Account>() {
        @Override
        public Account createFromParcel(Parcel source) {
            return new Account(source);
        }

        @Override
        public Account[] newArray(int size) {
            return new Account[size];
        }
    };
}
