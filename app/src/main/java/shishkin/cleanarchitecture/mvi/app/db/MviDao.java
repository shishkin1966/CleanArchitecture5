package shishkin.cleanarchitecture.mvi.app.db;

import android.os.Parcel;
import android.os.Parcelable;


import java.util.List;


import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;
import shishkin.cleanarchitecture.mvi.app.data.Account;
import shishkin.cleanarchitecture.mvi.app.data.Transfer;

import static androidx.room.OnConflictStrategy.ROLLBACK;

@Dao
public abstract class MviDao {
    private static String PLUS = " + ";

    @Transaction
    @Insert(onConflict = ROLLBACK)
    public abstract void insertAccount(Account account);

    @Transaction
    @Update(onConflict = ROLLBACK)
    public abstract void updateAccount(Account account);

    @Delete
    public abstract void deleteAccount(Account account);

    @Query("DELETE FROM " + Account.TABLE)
    public abstract void deleteAccounts();

    @Query("SELECT * FROM " + Account.TABLE + " ORDER BY " + Account.Columns.friendlyName + " ASC")
    public abstract List<Account> getAccounts();

    @Transaction
    @Insert(onConflict = ROLLBACK)
    public abstract void insertTransfer(Transfer transfer);

    @Query("SELECT * FROM " + Transfer.TABLE + " ORDER BY " + Transfer.Columns.createdAt + " DESC")
    public abstract List<Transfer> getTransfers();

    @Query("UPDATE " + Account.TABLE +
            " SET " + Account.Columns.balance + " = " + Account.Columns.balance + " + :value " +
            " WHERE " + Account.Columns.id + " = :id")
    public abstract void updateAccountBalance(Long id, double value);

    @Transaction
    public void insertAmountTransfer(Transfer transfer) {
        insertTransfer(transfer);
        updateAccountBalance(transfer.getSourceAccountId(), -transfer.getAmount());
        updateAccountBalance(transfer.getTargetAccountId(), transfer.getAmount());
    }

    @Query("SELECT " + Account.Columns.currency + " as currency, sum(" + Account.Columns.balance + ") as balance FROM " + Account.TABLE + " GROUP BY " + Account.Columns.currency)
    public abstract List<Balance> getBalance();

    @Query("SELECT DISTINCT " + Account.Columns.currency + " FROM " + Account.TABLE)
    public abstract List<String> getCurrency();

    public static class Balance implements Parcelable {
        public String currency;
        public Double balance;

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(this.currency);
            dest.writeValue(this.balance);
        }

        public Balance() {
        }

        protected Balance(Parcel in) {
            this.currency = in.readString();
            this.balance = (Double) in.readValue(Double.class.getClassLoader());
        }

        public static final Parcelable.Creator<Balance> CREATOR = new Parcelable.Creator<Balance>() {
            @Override
            public Balance createFromParcel(Parcel source) {
                return new Balance(source);
            }

            @Override
            public Balance[] newArray(int size) {
                return new Balance[size];
            }
        };
    }
}
