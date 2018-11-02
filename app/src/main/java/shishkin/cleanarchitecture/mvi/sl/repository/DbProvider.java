package shishkin.cleanarchitecture.mvi.sl.repository;

import androidx.sqlite.db.SupportSQLiteDatabase;
import androidx.room.RoomDatabase;
import androidx.room.migration.Migration;

/**
 * Created by Shishkin on 22.12.2017.
 */

@SuppressWarnings("unused")
public interface DbProvider<T extends RoomDatabase> extends Provider {

    /**
     * Создать/открыть БД
     *
     * @param klass        класс БД
     * @param databaseName имя БД
     */
    T getDb(Class<T> klass, String databaseName);

    /**
     * Создать/открыть БД
     *
     * @param klass        класс БД
     * @param databaseName имя БД
     * @param migrations   массив процедур обновления БД
     */
    T getDb(Class<T> klass, String databaseName, Migration... migrations);

    /**
     * Получить БД
     *
     * @param databaseName имя БД
     */
    T getDb(String databaseName);

    /**
     * Получить БД, если она единственная
     */
    T getDb();

    /**
     * Событие - БД создана
     *
     * @param db БД
     */
    void onCreateDatabase(SupportSQLiteDatabase db);

    /**
     * Событие - БД открыта
     *
     * @param db БД
     */
    void onOpenDatabase(SupportSQLiteDatabase db);

    /**
     * Резервировать БД
     *
     * @param databaseName имя БД
     * @param dirBackup    каталог копии БД
     */
    void backup(String databaseName, String dirBackup);

    /**
     * Восстановить БД
     *
     * @param databaseName имя БД
     * @param dirBackup    каталог копии БД
     */
    void restore(String databaseName, String dirBackup);

}
