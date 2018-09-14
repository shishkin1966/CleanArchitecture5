package shishkin.cleanarchitecture.mvi.sl.repository;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.migration.Migration;

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
}
