package shishkin.cleanarchitecture.mvi.app.screen.fragment.portion_load;

import android.os.Parcel;
import android.os.Parcelable;


import java.util.ArrayList;
import java.util.List;


import shishkin.cleanarchitecture.mvi.app.data.Account;

public class PagingViewData implements Parcelable {

    public static final String NAME = PagingViewData.class.getName();

    private List<Account> accounts = null;

    public PagingViewData() {
    }

    public List<Account> getAccounts() {
        return accounts;
    }

    public void clearAccounts() {
        accounts = new ArrayList<>();
    }

    public void addAccounts(List<Account> accounts) {
        if (this.accounts == null) {
            this.accounts = accounts;
        } else {
            this.accounts.addAll(accounts);
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(this.accounts);
    }

    private PagingViewData(Parcel in) {
        this.accounts = in.createTypedArrayList(Account.CREATOR);
    }

    public static final Parcelable.Creator<PagingViewData> CREATOR = new Parcelable.Creator<PagingViewData>() {
        @Override
        public PagingViewData createFromParcel(Parcel source) {
            return new PagingViewData(source);
        }

        @Override
        public PagingViewData[] newArray(int size) {
            return new PagingViewData[size];
        }
    };
}
