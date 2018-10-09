package shishkin.cleanarchitecture.mvi.app.data;


import android.os.Parcel;
import android.os.Parcelable;


import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

@Root(name = "Valute")
public class Valute implements Parcelable {
    @Attribute(name = "ID")
    private String id;

    @Element(name = "NumCode")
    private String numCode;

    @Element(name = "CharCode")
    private String charCode;

    @Element(name = "Nominal")
    private String nominal;

    @Element(name = "Name")
    private String name;

    @Element(name = "Value")
    private String value;


    public String getId() {
        return id;
    }

    public String getNumCode() {
        return numCode;
    }

    public String getCharCode() {
        return charCode;
    }

    public String getNominal() {
        return nominal;
    }

    public String getName() {
        return name;
    }

    public String getValue() {
        return value;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.numCode);
        dest.writeString(this.charCode);
        dest.writeString(this.nominal);
        dest.writeString(this.name);
        dest.writeString(this.value);
    }

    public Valute() {
    }

    protected Valute(Parcel in) {
        this.id = in.readString();
        this.numCode = in.readString();
        this.charCode = in.readString();
        this.nominal = in.readString();
        this.name = in.readString();
        this.value = in.readString();
    }

    public static final Parcelable.Creator<Valute> CREATOR = new Parcelable.Creator<Valute>() {
        @Override
        public Valute createFromParcel(Parcel source) {
            return new Valute(source);
        }

        @Override
        public Valute[] newArray(int size) {
            return new Valute[size];
        }
    };
}
