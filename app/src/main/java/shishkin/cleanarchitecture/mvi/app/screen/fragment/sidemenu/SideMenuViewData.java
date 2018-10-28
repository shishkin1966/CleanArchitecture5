package shishkin.cleanarchitecture.mvi.app.screen.fragment.sidemenu;

import android.os.Parcel;
import android.os.Parcelable;


import java.util.List;


import shishkin.cleanarchitecture.mvi.app.db.MviDao;

public class SideMenuViewData implements Parcelable {
    public static final String NAME = SideMenuViewData.class.getName();

    private List<MviDao.Balance> balance;

    SideMenuViewData() {
    }

    public List<MviDao.Balance> getBalance() {
        return balance;
    }

    public void setBalance(List<MviDao.Balance> balance) {
        this.balance = balance;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(this.balance);
    }

    private SideMenuViewData(Parcel in) {
        this.balance = in.createTypedArrayList(MviDao.Balance.CREATOR);
    }

    public static final Parcelable.Creator<SideMenuViewData> CREATOR = new Parcelable.Creator<SideMenuViewData>() {
        @Override
        public SideMenuViewData createFromParcel(Parcel source) {
            return new SideMenuViewData(source);
        }

        @Override
        public SideMenuViewData[] newArray(int size) {
            return new SideMenuViewData[size];
        }
    };
}
