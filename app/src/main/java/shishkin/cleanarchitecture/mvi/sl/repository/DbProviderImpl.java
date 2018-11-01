package shishkin.cleanarchitecture.mvi.sl.repository;

import android.Manifest;
import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.migration.Migration;
import android.content.Context;
import android.support.annotation.NonNull;
import android.widget.Toast;

import com.google.common.io.Files;


import java.io.File;


import shishkin.cleanarchitecture.mvi.R;
import shishkin.cleanarchitecture.mvi.common.utils.ApplicationUtils;
import shishkin.cleanarchitecture.mvi.common.utils.SafeUtils;
import shishkin.cleanarchitecture.mvi.common.utils.StringUtils;
import shishkin.cleanarchitecture.mvi.sl.ApplicationSpecialistImpl;
import shishkin.cleanarchitecture.mvi.sl.ErrorSpecialistImpl;
import shishkin.cleanarchitecture.mvi.sl.RequestSpecialist;
import shishkin.cleanarchitecture.mvi.sl.RequestSpecialistImpl;
import shishkin.cleanarchitecture.mvi.sl.SL;
import shishkin.cleanarchitecture.mvi.sl.Secretary;
import shishkin.cleanarchitecture.mvi.sl.SecretaryImpl;
import shishkin.cleanarchitecture.mvi.sl.data.Result;
import shishkin.cleanarchitecture.mvi.sl.request.Request;

/**
 * Created by Shishkin on 19.12.2017.
 */

public class DbProviderImpl<T extends RoomDatabase> extends AbsProvider implements DbProvider<T> {

    public static final String NAME = DbProviderImpl.class.getName();
    private Secretary<T> mDb = new SecretaryImpl<>();
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
            db.getOpenHelper().getReadableDatabase().getVersion();
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
    public boolean isPersistent() {
        return false;
    }

    @Override
    public void onUnRegister() {
        for (String databaseName : mDb.keys()) {
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

    @Override
    public void backup(final String databaseName, final String dirBackup) {
        if (!ApplicationUtils.checkPermission(ApplicationSpecialistImpl.getInstance(), Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            return;
        }

        final T db = mDb.get(databaseName);
        if (db == null) {
            return;
        }

        final Class<T> klass = SafeUtils.cast(db.getClass().getSuperclass());
        final String pathDb = db.getOpenHelper().getReadableDatabase().getPath();
        if (StringUtils.isNullOrEmpty(pathDb)) {
            return;
        }

        disconnect(databaseName);

        final File fileDb = new File(pathDb);
        final String nameDb = fileDb.getName();
        final String pathBackup = dirBackup + File.separator + nameDb;
        try {
            final File fileBackup = new File(pathBackup);
            final File fileBackupOld = new File(pathBackup + "1");
            if (fileDb.exists()) {
                if (fileBackup.exists()) {
                    final File dir = new File(dirBackup);
                    if (!dir.exists()) {
                        dir.mkdirs();
                    }
                    if (dir.exists()) {
                        if (fileBackupOld.exists()) {
                            fileBackupOld.delete();
                        }
                        if (!fileBackupOld.exists()) {
                            Files.copy(fileBackup, fileBackupOld);
                            if (fileBackupOld.exists()) {
                                fileBackup.delete();
                                if (!fileBackup.exists()) {
                                    Files.copy(fileDb, fileBackup);
                                    if (fileBackup.exists()) {
                                        fileBackupOld.delete();
                                    } else {
                                        Files.copy(fileBackupOld, fileBackup);
                                    }
                                }
                            }
                        }
                    }
                } else {
                    final File dir = new File(dirBackup);
                    if (!dir.exists()) {
                        dir.mkdirs();
                    }
                    if (dir.exists()) {
                        Files.copy(fileDb, fileBackup);
                    }
                }
            }

            connect(klass, nameDb);

            ApplicationUtils.runOnUiThread(() -> ApplicationUtils.showToast(ApplicationSpecialistImpl.getInstance().getString(R.string.db_backuped), Toast.LENGTH_LONG, ApplicationUtils.MESSAGE_TYPE_INFO));
        } catch (Exception e) {
            ErrorSpecialistImpl.getInstance().onError(NAME, e);
        }
    }

    @Override
    public void restore(final String databaseName, final String dirBackup) {
        if (!ApplicationUtils.checkPermission(ApplicationSpecialistImpl.getInstance(), Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            return;
        }

        final T db = mDb.get(databaseName);
        if (db == null) {
            return;
        }

        final Class<T> klass = SafeUtils.cast(db.getClass().getSuperclass());
        final String pathDb = db.getOpenHelper().getReadableDatabase().getPath();
        if (StringUtils.isNullOrEmpty(pathDb)) {
            return;
        }

        disconnect(databaseName);

        final File fileDb = new File(pathDb);
        final String nameDb = fileDb.getName();
        final String pathBackup = dirBackup + File.separator + nameDb;
        final File fileBackup = new File(pathBackup);
        if (fileBackup.exists()) {
            try {
                if (fileDb.exists()) {
                    fileDb.delete();
                }
                if (!fileDb.exists()) {
                    Files.createParentDirs(fileDb);
                    final File dir = new File(fileDb.getParent());
                    if (!dir.exists()) {
                        dir.mkdirs();
                    }
                    if (dir.exists()) {
                        Files.copy(fileBackup, fileDb);
                    }
                }

                connect(klass, nameDb);

                ApplicationUtils.runOnUiThread(() -> ApplicationUtils.showToast(ApplicationSpecialistImpl.getInstance().getString(R.string.db_restored), Toast.LENGTH_LONG, ApplicationUtils.MESSAGE_TYPE_INFO));
            } catch (Exception e) {
                ErrorSpecialistImpl.getInstance().onError(NAME, e);
            }
        }
    }

}
