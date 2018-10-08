package shishkin.cleanarchitecture.mvi.app.setting;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.InputType;

import com.google.gson.annotations.SerializedName;


import java.util.ArrayList;


import shishkin.cleanarchitecture.mvi.sl.data.AbsEntity;

public class ApplicationSetting extends AbsEntity implements Parcelable {
    public static final int TYPE_LIST = 0;
    public static final int TYPE_SWITCH = 1;
    public static final int TYPE_TEXT = 2;
    public static final int TYPE_COLOR = 3;
    public static final int TYPE_EDIT = 4;

    @SerializedName("values")
    private ArrayList<String> mValues;

    @SerializedName("current")
    private String mCurrentValue;

    @SerializedName("default")
    private String mDefaultValue;

    @SerializedName("name")
    private String mName;

    @SerializedName("title")
    private String mTitle;

    @SerializedName("id")
    private int mId;

    @SerializedName("type")
    private int mType = 0;

    @SerializedName("inputType")
    private int mInputType = InputType.TYPE_CLASS_NUMBER;

    public ApplicationSetting(int type) {
        mType = type;
    }

    public int getId() {
        return mId;
    }

    public ApplicationSetting setId(int id) {
        this.mId = id;
        return this;
    }

    public String getTitle() {
        return mTitle;
    }

    public ApplicationSetting setTitle(String title) {
        this.mTitle = title;
        return this;
    }

    public String getName() {
        return mName;
    }

    public ApplicationSetting setName(String name) {
        this.mName = name;
        return this;
    }

    public ArrayList<String> getValues() {
        return mValues;
    }

    public ApplicationSetting setValues(ArrayList<String> values) {
        this.mValues = values;
        return this;
    }

    public String getCurrentValue() {
        return mCurrentValue;
    }

    public ApplicationSetting setCurrentValue(String currentValue) {
        this.mCurrentValue = currentValue;
        return this;
    }

    public String getDefaultValue() {
        return mDefaultValue;
    }

    public ApplicationSetting setDefaultValue(String defaultValue) {
        this.mDefaultValue = defaultValue;
        return this;
    }

    public int getInputType() {
        return mInputType;
    }

    public ApplicationSetting setInputType(int mInputType) {
        this.mInputType = mInputType;
        return this;
    }

    public int getType() {
        return mType;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeStringList(this.mValues);
        dest.writeString(this.mCurrentValue);
        dest.writeString(this.mDefaultValue);
        dest.writeString(this.mName);
        dest.writeString(this.mTitle);
        dest.writeInt(this.mId);
        dest.writeInt(this.mType);
        dest.writeInt(this.mInputType);
    }

    protected ApplicationSetting(Parcel in) {
        this.mValues = in.createStringArrayList();
        this.mCurrentValue = in.readString();
        this.mDefaultValue = in.readString();
        this.mName = in.readString();
        this.mTitle = in.readString();
        this.mId = in.readInt();
        this.mType = in.readInt();
        this.mInputType = in.readInt();
    }

    public static final Parcelable.Creator<ApplicationSetting> CREATOR = new Parcelable.Creator<ApplicationSetting>() {
        @Override
        public ApplicationSetting createFromParcel(Parcel source) {
            return new ApplicationSetting(source);
        }

        @Override
        public ApplicationSetting[] newArray(int size) {
            return new ApplicationSetting[size];
        }
    };
}
