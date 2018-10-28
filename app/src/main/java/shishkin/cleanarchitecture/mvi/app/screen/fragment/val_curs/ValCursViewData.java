package shishkin.cleanarchitecture.mvi.app.screen.fragment.val_curs;

import android.os.Parcel;
import android.os.Parcelable;


import java.util.HashMap;
import java.util.List;
import java.util.Map;


import shishkin.cleanarchitecture.mvi.app.SLUtil;
import shishkin.cleanarchitecture.mvi.app.data.ValCurs;
import shishkin.cleanarchitecture.mvi.app.data.Valute;

public class ValCursViewData implements Parcelable {

    public static final String NAME = ValCursViewData.class.getName();

    private ValCurs valCurs;
    private Map<String, Valute> selected = new HashMap<>();

    ValCursViewData() {
    }

    void setValCurs(ValCurs valCurs) {
        this.valCurs = valCurs;
    }

    public List<Valute> getData() {
        if (valCurs == null) return null;

        return SLUtil.getDataSpecialist().sort(valCurs.getValute(), (o1, o2) -> o1.getName().compareTo(o2.getName())).toList();
    }

    public boolean isSelected() {
        return !selected.isEmpty();
    }

    public Map<String, Valute> getSelected() {
        return selected;
    }

    int getSelectedCount() {
        return selected.size();
    }

    void clearSelected() {
        selected.clear();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(this.valCurs, flags);
        dest.writeInt(this.selected.size());
        for (Map.Entry<String, Valute> entry : this.selected.entrySet()) {
            dest.writeString(entry.getKey());
            dest.writeParcelable(entry.getValue(), flags);
        }
    }

    private ValCursViewData(Parcel in) {
        this.valCurs = in.readParcelable(ValCurs.class.getClassLoader());
        int selectedSize = in.readInt();
        this.selected = new HashMap<>(selectedSize);
        for (int i = 0; i < selectedSize; i++) {
            String key = in.readString();
            Valute value = in.readParcelable(Valute.class.getClassLoader());
            this.selected.put(key, value);
        }
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
