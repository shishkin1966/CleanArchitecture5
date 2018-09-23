package shishkin.cleanarchitecture.mvi.sl.repository;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.migration.Migration;
import android.content.Context;
import android.support.annotation.NonNull;


import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


import shishkin.cleanarchitecture.mvi.common.utils.StringUtils;
import shishkin.cleanarchitecture.mvi.sl.ApplicationSpecialistImpl;
import shishkin.cleanarchitecture.mvi.sl.ErrorSpecialistImpl;
import shishkin.cleanarchitecture.mvi.sl.RequestSpecialist;
import shishkin.cleanarchitecture.mvi.sl.RequestSpecialistImpl;
import shishkin.cleanarchitecture.mvi.sl.SL;
import shishkin.cleanarchitecture.mvi.sl.data.Result;
import shishkin.cleanarchitecture.mvi.sl.request.Request;
import shishkin.cleanarchitecture.mvi.sl.request.ResponseListener;

/**
 * Created by Shishkin on 19.12.2017.
 */

public class DbProviderImpl<T extends RoomDatabase> extends AbsProvider implements DbProvider<T> {

    public static final String NAME = DbProviderImpl.class.getName();
    private Map<String, T> mDb = Collections.synchronizedMap(new ConcurrentHashMap<String, T>());
    private RoomDatabase.Callback mCallback = new RoomDatabase.Callback() {
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
    };

    public DbProviderImpl() {
    }

    private boolean connect(final Class<T> klass, final String databaseName, Migration... migrations) {
        final Context context = ApplicationSpecialistImpl.getInstance();
        if (context == null) {
            return false;
        }

        try {
            T db;
            if (migrations == null) {
                db = Room.databaseBuilder(context, klass, databaseName)
                        .addCallback(mCallback)
                        .build();
            } else {
                db = Room.databaseBuilder(context, klass, databaseName)
                        .addMigrations(migrations)
                        .addCallback(mCallback)
                        .build();
            }
            if (db != null) {
                db.getOpenHelper().getReadableDatabase().getVersion();
            }
            mDb.put(databaseName, db);
        } catch (Exception e) {
            ErrorSpecialistImpl.getInstance().onError(NAME, e);
        }
        return isConnected(databaseName);
    }

    private boolean disconnect(final String databaseName) {
        if (isConnected(databaseName)) {
            final T db = mDb.get(databaseName);
            try {
                db.close();
                mDb.remove(databaseName);
            } catch (Exception e) {
                ErrorSpecialistImpl.getInstance().onError(NAME, e);
            }
        }
        return !isConnected(databaseName);
    }

    private boolean isConnected(final String databaseName) {
        if (StringUtils.isNullOrEmpty(databaseName)) {
            return false;
        }

        return mDb.containsKey(databaseName);
    }

    @Override
    public T getDb(final Class<T> klass, final String databaseName) {
        if (!isConnected(databaseName)) {
            connect(klass, databaseName);
        }
        return mDb.get(databaseName);
    }

    @Override
    public T getDb(final Class<T> klass, final String databaseName, Migration... migrations) {
        if (!isConnected(databaseName)) {
            connect(klass, databaseName, migrations);
        }
        return mDb.get(databaseName);
    }

    @Override
    public T getDb(final String databaseName) {
        if (mDb.containsKey(databaseName)) {
            return mDb.get(databaseName);
        }
        return null;
    }

    @Override
    public T getDb() {
        if (!mDb.isEmpty()) {
            return mDb.values().iterator().next();
        }
        return null;
    }

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public void request(Request request) {
        if (request != null && validate()) {
            ((RequestSpecialist) SL.getInstance().get(RequestSpecialistImpl.NAME)).request(this, request);
        }
    }

    @Override
    public void cancelRequests(ResponseListener listener) {
        ((RequestSpecialist) SL.getInstance().get(RequestSpecialistImpl.NAME)).cancelRequests(this, listener);
    }

    @Override
    public boolean isPersistent() {
        return false;
    }

    @Override
    public void onUnRegister() {
        for (String databaseName : mDb.keySet()) {
            disconnect(databaseName);
        }
    }

    @Override
    public void onRegister() {
    }

    @Override
    public Result<Boolean> validateExt() {
        return new Result<>(!mDb.isEmpty());
    }

    @Override
    public void onCreateDatabase(final SupportSQLiteDatabase db) {
    }

    @Override
    public void onOpenDatabase(final SupportSQLiteDatabase db) {
    }

    @Override
    public int compareTo(@NonNull Object o) {
        return (DbProvider.class.isInstance(o)) ? 0 : 1;
    }

}
