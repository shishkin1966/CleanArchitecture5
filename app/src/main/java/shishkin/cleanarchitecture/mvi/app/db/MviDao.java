package shishkin.cleanarchitecture.mvi.app.db;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Transaction;
import android.arch.persistence.room.Update;


import java.util.List;


import shishkin.cleanarchitecture.mvi.app.data.Account;
import shishkin.cleanarchitecture.mvi.app.data.Transfer;

import static android.arch.persistence.room.OnConflictStrategy.ROLLBACK;

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

    public static class Balance {
        public String currency;
        public Double balance;
    }
}
