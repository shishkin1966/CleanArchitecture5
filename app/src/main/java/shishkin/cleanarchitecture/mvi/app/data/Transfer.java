package shishkin.cleanarchitecture.mvi.app.data;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;


import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;
import shishkin.cleanarchitecture.mvi.sl.data.AbsEntity;

/**
 * Created by Shishkin on 10.01.2018.
 */

@Entity(tableName = Transfer.TABLE, foreignKeys = {
        @ForeignKey(entity = Account.class, parentColumns = Account.Columns.id, childColumns = Transfer.Columns.sourceAccountId, onUpdate = ForeignKey.CASCADE, onDelete = ForeignKey.CASCADE),
        @ForeignKey(entity = Account.class, parentColumns = Account.Columns.id, childColumns = Transfer.Columns.targetAccountId, onUpdate = ForeignKey.CASCADE, onDelete = ForeignKey.CASCADE),
},
        indices = {@Index(name = Transfer.TABLE + "_" + Transfer.Columns.sourceAccountId, value = Transfer.Columns.sourceAccountId),
                @Index(name = Transfer.TABLE + "_" + Transfer.Columns.targetAccountId, value = Transfer.Columns.targetAccountId)
        })
@SuppressWarnings("unused")
public class Transfer extends AbsEntity implements Parcelable {

    public static final String TABLE = "Transfer";

    public interface Columns {
        String id = "id";
        String createdAt = "createdAt";
        String sourceAccountId = "sourceAccountId";
        String targetAccountId = "targetAccountId";
        String description = "description";
        String amount = "amount";
    }

    public static final String[] PROJECTION = {
            Columns.id,
            Columns.createdAt,
            Columns.sourceAccountId,
            Columns.targetAccountId,
            Columns.description,
            Columns.amount
    };

    @ColumnInfo(name = "id")
    @PrimaryKey(autoGenerate = true)
    @SerializedName("id")
    @NonNull
    private Long mId;

    @ColumnInfo(name = Columns.createdAt)
    @SerializedName(Columns.createdAt)
    @NonNull
    private Long mCreatedAt;

    @ColumnInfo(name = Columns.sourceAccountId)
    @SerializedName(Columns.sourceAccountId)
    private Long mSourceAccountId;

    @ColumnInfo(name = Columns.targetAccountId)
    @SerializedName(Columns.targetAccountId)
    private Long mTargetAccountId;

    @ColumnInfo(name = Columns.amount)
    @SerializedName(Columns.amount)
    private Double mAmount;

    public Transfer() {
        mCreatedAt = System.currentTimeMillis();
        mAmount = 0.00d;
    }

    @NonNull
    public Long getId() {
        return mId;
    }

    public void setId(@NonNull Long id) {
        this.mId = id;
    }

    @NonNull
    public Long getCreatedAt() {
        return mCreatedAt;
    }

    public void setCreatedAt(@NonNull Long createdAt) {
        this.mCreatedAt = createdAt;
    }

    public Double getAmount() {
        return mAmount;
    }

    public void setAmount(Double amount) {
        this.mAmount = amount;
    }

    public Long getSourceAccountId() {
        return mSourceAccountId;
    }

    public void setSourceAccountId(Long sourceAccountId) {
        this.mSourceAccountId = sourceAccountId;
    }

    public Long getTargetAccountId() {
        return mTargetAccountId;
    }

    public void setTargetAccountId(Long targetAccountId) {
        this.mTargetAccountId = targetAccountId;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(this.mId);
        dest.writeValue(this.mCreatedAt);
        dest.writeValue(this.mSourceAccountId);
        dest.writeValue(this.mTargetAccountId);
        dest.writeValue(this.mAmount);
    }

    protected Transfer(Parcel in) {
        this.mId = (Long) in.readValue(Long.class.getClassLoader());
        this.mCreatedAt = (Long) in.readValue(Long.class.getClassLoader());
        this.mSourceAccountId = (Long) in.readValue(Long.class.getClassLoader());
        this.mTargetAccountId = (Long) in.readValue(Long.class.getClassLoader());
        this.mAmount = (Double) in.readValue(Double.class.getClassLoader());
    }

    public static final Parcelable.Creator<Transfer> CREATOR = new Parcelable.Creator<Transfer>() {
        @Override
        public Transfer createFromParcel(Parcel source) {
            return new Transfer(source);
        }

        @Override
        public Transfer[] newArray(int size) {
            return new Transfer[size];
        }
    };
}
