package shishkin.cleanarchitecture.mvi.app.specialist.storage;

import android.os.Parcelable;


import java.util.List;


import shishkin.cleanarchitecture.mvi.sl.Specialist;

@SuppressWarnings("unused")
public interface CacheSpecialist extends Specialist {
    /**
     * Put value to cache.
     *
     * @param key   the key
     * @param value the value
     */
    <T extends Parcelable> void put(final String key, final T value);

    /**
     * Put list to cache.
     *
     * @param key    the key
     * @param values the value
     */
    <T extends Parcelable> void put(final String key, final List<T> values);

    /**
     * Get value from cache.
     *
     * @param key       the key
     * @param itemClass the value class
     * @return the Parcelable
     */
    <T extends Parcelable> T get(final String key, final Class itemClass);

    /**
     * Get list from cache.
     *
     * @param key       the key
     * @param itemClass the value class
     * @return the list
     */
    <T extends Parcelable> List<T> getList(final String key, final Class itemClass);

    /**
     * delete value from cache
     *
     * @param key the key
     */
    void clear(final String key);

    /**
     * delete cache
     */
    void clear();

}
