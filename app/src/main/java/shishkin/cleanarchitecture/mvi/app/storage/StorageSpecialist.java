package shishkin.cleanarchitecture.mvi.app.storage;

import android.os.Parcelable;


import java.util.List;


import shishkin.cleanarchitecture.mvi.sl.Specialist;

public interface StorageSpecialist extends Specialist{
    /**
     * Put value to cache.
     *
     * @param key     the key
     * @param value   the value
     */
    <T extends Parcelable> void putCache(final String key, final T value);

    /**
     * Put list to cache.
     *
     * @param key     the key
     * @param values   the value
     */
    <T extends Parcelable> void putCache(final String key, final List<T> values);

    /**
     * Get value from cache.
     *
     * @param key       the key
     * @param itemClass the value class
     * @return the Parcelable
     */
    <T extends Parcelable> T getCache(final String key, final Class itemClass);

    /**
     * Get list from cache.
     *
     * @param key       the key
     * @param itemClass the value class
     * @return the list
     */
    <T extends Parcelable> List<T> getListCache(final String key, final Class itemClass);

    /**
     * delete value from cache
     *
     * @param key       the key
     */
    void clearCache(final String key);

    /**
     * delete cache
     */
    void clearCache();

    /**
     * Put value to storage.
     *
     * @param key     the key
     * @param value   the value
     */
    <T extends Parcelable> void put(final String key, final T value);

    /**
     * Put value to storage.
     *
     * @param key     the key
     * @param value   the value
     * @param expired expired date
     */
    <T extends Parcelable> void put(String key, T value, long expired);

    /**
     * Put list to storage.
     *
     * @param key     the key
     * @param values  the list
     */
    <T extends Parcelable> void put(final String key, final List<T> values);

    /**
     * Put list to storage.
     *
     * @param key     the key
     * @param values  the list
     * @param expired expired date
     */
    <T extends Parcelable> void put(String key, List<T> values, long expired);

    /**
     * Get value from storage.
     *
     * @param key       the key
     * @param itemClass the value class
     * @return the Parcelable
     */
    <T extends Parcelable> T get(String key, Class itemClass);

    /**
     * Get list of values from storage.
     *
     * @param key       the key
     * @param itemClass the value class
     * @return the list of Parcelable
     */
    <T extends Parcelable> List<T> getList(String key, Class itemClass);

    /**
     * Clear value.
     *
     * @param key the key
     */
    void clear(String key);

    /**
     * Clear all values.
     */
    void clear();

    /**
     * Событие - остановка приложения
     */
    void onFinishApplication();
}
