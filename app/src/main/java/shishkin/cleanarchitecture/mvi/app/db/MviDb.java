package shishkin.cleanarchitecture.mvi.app.db;

import androidx.sqlite.db.SupportSQLiteDatabase;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import android.content.Context;
import androidx.annotation.NonNull;


import shishkin.cleanarchitecture.mvi.app.data.Account;
import shishkin.cleanarchitecture.mvi.app.data.Transfer;


@Database(entities = {Account.class, Transfer.class}, version = MviDb.VERSION, exportSchema = false)
public abstract class MviDb extends RoomDatabase {

    public static final String NAME = "mvi.db";
    public static final int VERSION = 1;

    public abstract MviDao MviDao();

    private static volatile MviDb sInstance;

    public static MviDb getInstance(Context context) {
        if (sInstance == null) {
            sInstance = buildDatabase(context);
        }
        return sInstance;
    }

    private static MviDb buildDatabase(Context context) {
        if (context == null) return null;

        return Room.databaseBuilder(context,
                MviDb.class,
                NAME)
                .addCallback(new RoomDatabase.Callback() {
                    @Override
                    public void onCreate(@NonNull SupportSQLiteDatabase db) {
                        super.onCreate(db);

                        onCreateDatabase(db);
                    }

                    @Override
                    public void onOpen(@NonNull SupportSQLiteDatabase db) {
                        super.onOpen(db);

                        onOpenDatabase(db);
                    }
                })
                .build();
    }

    private static void onCreateDatabase(final SupportSQLiteDatabase db) {
    }

    private static void onOpenDatabase(final SupportSQLiteDatabase db) {
    }


}