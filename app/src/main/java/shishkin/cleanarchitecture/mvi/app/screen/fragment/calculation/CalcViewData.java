package shishkin.cleanarchitecture.mvi.app.screen.fragment.calculation;

import android.os.Parcel;
import android.os.Parcelable;

public class CalcViewData implements Parcelable {

    public static final String NAME = CalcViewData.class.getName();

    private int sum = 0;

    private String item1 = "";
    private String item2 = "";
    private String item3 = "";
    private String item4 = "";
    private String item5 = "";

    CalcViewData() {
    }

    public int getSum() {
        return sum;
    }

    public void setSum(int sum) {
        this.sum = sum;
    }

    public String getItem1() {
        return item1;
    }

    void setItem1(String item1) {
        this.item1 = item1;
    }

    public String getItem2() {
        return item2;
    }

    void setItem2(String item2) {
        this.item2 = item2;
    }

    public String getItem3() {
        return item3;
    }

    void setItem3(String item3) {
        this.item3 = item3;
    }

    public String getItem4() {
        return item4;
    }

    void setItem4(String item4) {
        this.item4 = item4;
    }

    public String getItem5() {
        return item5;
    }

    void setItem5(String item5) {
        this.item5 = item5;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.sum);
        dest.writeString(this.item1);
        dest.writeString(this.item2);
        dest.writeString(this.item3);
        dest.writeString(this.item4);
        dest.writeString(this.item5);
    }

    private CalcViewData(Parcel in) {
        this.sum = in.readInt();
        this.item1 = in.readString();
        this.item2 = in.readString();
        this.item3 = in.readString();
        this.item4 = in.readString();
        this.item5 = in.readString();
    }

    public static final Parcelable.Creator<CalcViewData> CREATOR = new Parcelable.Creator<CalcViewData>() {
        @Override
        public CalcViewData createFromParcel(Parcel source) {
            return new CalcViewData(source);
        }

        @Override
        public CalcViewData[] newArray(int size) {
            return new CalcViewData[size];
        }
    };
}
