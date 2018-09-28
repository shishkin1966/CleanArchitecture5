package shishkin.cleanarchitecture.mvi.app.viewdata;

import android.os.Parcel;
import android.os.Parcelable;


import java.util.List;


import shishkin.cleanarchitecture.mvi.app.data.Account;

public class AccountsViewData implements Parcelable {

    public static final String NAME = AccountsViewData.class.getName();

    private int mSort = 0;
    private String mFilter;
    private List<Account> mAccounts;
    private List<String> mCurrency;

    public int getSort() {
        return mSort;
    }

    public void setSort(int sort) {
        this.mSort = sort;
    }

    public String getFilter() {
        return mFilter;
    }

    public void setFilter(String filter) {
        this.mFilter = filter;
    }

    public List<Account> getAccounts() {
        return mAccounts;
    }

    public void setAccounts(List<Account> accounts) {
        this.mAccounts = accounts;
    }

    public List<String> getCurrency() {
        return mCurrency;
    }

    public void setCurrency(List<String> currency) {
        this.mCurrency = currency;
    }

    public boolean isSortMenuEnabled() {
        return (mAccounts != null && mAccounts.size() > 1);
    }

    public boolean isFilterMenuEnabled() {
        return (mAccounts != null && mAccounts.size() > 1);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.mSort);
        dest.writeString(this.mFilter);
        dest.writeTypedList(this.mAccounts);
        dest.writeStringList(this.mCurrency);
    }

    public AccountsViewData() {
    }

    protected AccountsViewData(Parcel in) {
        this.mSort = in.readInt();
        this.mFilter = in.readString();
        this.mAccounts = in.createTypedArrayList(Account.CREATOR);
        this.mCurrency = in.createStringArrayList();
    }

    public static final Parcelable.Creator<AccountsViewData> CREATOR = new Parcelable.Creator<AccountsViewData>() {
        @Override
        public AccountsViewData createFromParcel(Parcel source) {
            return new AccountsViewData(source);
        }

        @Override
        public AccountsViewData[] newArray(int size) {
            return new AccountsViewData[size];
        }
    };
}
