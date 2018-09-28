package shishkin.cleanarchitecture.mvi.app.viewdata;

import android.os.Parcel;
import android.os.Parcelable;


import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;


import shishkin.cleanarchitecture.mvi.app.SLUtil;
import shishkin.cleanarchitecture.mvi.app.data.Account;
import shishkin.cleanarchitecture.mvi.common.utils.StringUtils;

public class AccountsViewData implements Parcelable {

    public static final String NAME = AccountsViewData.class.getName();

    private int sort = 0;
    private String filter;
    private List<Account> accounts;
    private List<String> currencies;
    private Comparator<Account> nameComparator = (o1, o2) -> o1.getFriendlyName().compareTo(o2.getFriendlyName());
    private Comparator<Account> currencyComparator = (o1, o2) -> o1.getCurrency().compareTo(o2.getCurrency());

    public int getSort() {
        return sort;
    }

    public void setSort(int sort) {
        this.sort = sort;
    }

    public String getFilter() {
        return filter;
    }

    public void setFilter(String filter) {
        this.filter = filter;
    }

    public List<Account> getAccounts() {
        return accounts;
    }

    public void setAccounts(List<Account> accounts) {
        this.accounts = accounts;
    }

    public List<String> getCurrencies() {
        return currencies;
    }

    public void setCurrencies(List<String> currencies) {
        this.currencies = currencies;
    }

    public boolean isSortMenuEnabled() {
        return (accounts != null && accounts.size() > 1);
    }

    public boolean isFilterMenuEnabled() {
        return (accounts != null && accounts.size() > 1);
    }

    public List<Account> getData() {
        if (accounts == null) return null;

        final List<Account> list = new ArrayList<>();
        if (StringUtils.isNullOrEmpty(filter)) {
            list.addAll(accounts);
        } else {
            list.addAll(SLUtil.getDataSpecialist().filter(getAccounts(), value -> value.getCurrency().equals(filter)).toList());
        }
        switch (getSort()) {
            case 0:
                return list;
            case 1:
                return SLUtil.getDataSpecialist().sort(list, nameComparator).toList();
            case 2:
                return SLUtil.getDataSpecialist().sort(list, currencyComparator).toList();
            default:
                return list;
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.sort);
        dest.writeString(this.filter);
        dest.writeTypedList(this.accounts);
        dest.writeStringList(this.currencies);
    }

    public AccountsViewData() {
    }

    protected AccountsViewData(Parcel in) {
        this.sort = in.readInt();
        this.filter = in.readString();
        this.accounts = in.createTypedArrayList(Account.CREATOR);
        this.currencies = in.createStringArrayList();
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
