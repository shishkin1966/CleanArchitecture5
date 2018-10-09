package shishkin.cleanarchitecture.mvi.app.data;

import android.os.Parcel;
import android.os.Parcelable;


import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.util.ArrayList;

@Root(name = "ValCurs")
public class ValCurs implements Parcelable {
    @Attribute(name = "Date")
    private String date;

    @Attribute(name = "name")
    private String name;

    @ElementList(inline = true)
    private ArrayList<Valute> valute;

    public String getDate() {
        return date;
    }

    public String getName() {
        return name;
    }

    public ArrayList<Valute> getValute() {
        return valute;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.date);
        dest.writeString(this.name);
        dest.writeTypedList(this.valute);
    }

    public ValCurs() {
    }

    protected ValCurs(Parcel in) {
        this.date = in.readString();
        this.name = in.readString();
        this.valute = in.createTypedArrayList(Valute.CREATOR);
    }

    public static final Parcelable.Creator<ValCurs> CREATOR = new Parcelable.Creator<ValCurs>() {
        @Override
        public ValCurs createFromParcel(Parcel source) {
            return new ValCurs(source);
        }

        @Override
        public ValCurs[] newArray(int size) {
            return new ValCurs[size];
        }
    };
}
