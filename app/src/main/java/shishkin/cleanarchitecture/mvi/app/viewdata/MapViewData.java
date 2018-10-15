package shishkin.cleanarchitecture.mvi.app.viewdata;

import android.os.Parcel;
import android.os.Parcelable;

public class MapViewData implements Parcelable {

    public static final String NAME = MapViewData.class.getName();

    private String address;

    public MapViewData() {
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.address);
    }

    protected MapViewData(Parcel in) {
        this.address = in.readString();
    }

    public static final Parcelable.Creator<MapViewData> CREATOR = new Parcelable.Creator<MapViewData>() {
        @Override
        public MapViewData createFromParcel(Parcel source) {
            return new MapViewData(source);
        }

        @Override
        public MapViewData[] newArray(int size) {
            return new MapViewData[size];
        }
    };
}
