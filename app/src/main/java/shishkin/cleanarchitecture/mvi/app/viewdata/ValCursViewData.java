package shishkin.cleanarchitecture.mvi.app.viewdata;

import android.os.Parcel;
import android.os.Parcelable;


import java.util.List;


import shishkin.cleanarchitecture.mvi.app.SLUtil;
import shishkin.cleanarchitecture.mvi.app.data.ValCurs;
import shishkin.cleanarchitecture.mvi.app.data.Valute;

public class ValCursViewData implements Parcelable {

    public static final String NAME = ValCursViewData.class.getName();

    private ValCurs valCurs;

    public ValCursViewData() {
    }

    public ValCurs getValCurs() {
        return valCurs;
    }

    public void setValCurs(ValCurs valCurs) {
        this.valCurs = valCurs;
    }

    public List<Valute> getData() {
        if (valCurs == null) return null;

        return SLUtil.getDataSpecialist().sort(valCurs.getValute(), (o1, o2) -> o1.getName().compareTo(o2.getName())).toList();
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(this.valCurs, flags);
    }

    protected ValCursViewData(Parcel in) {
        this.valCurs = in.readParcelable(ValCurs.class.getClassLoader());
    }

    public static final Parcelable.Creator<ValCursViewData> CREATOR = new Parcelable.Creator<ValCursViewData>() {
        @Override
        public ValCursViewData createFromParcel(Parcel source) {
            return new ValCursViewData(source);
        }

        @Override
        public ValCursViewData[] newArray(int size) {
            return new ValCursViewData[size];
        }
    };
}
