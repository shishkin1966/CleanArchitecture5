package shishkin.cleanarchitecture.mvi.app.storage;

import android.os.Parcelable;


import java.util.List;

public interface StorageSpecialist {
    <T extends Parcelable> void putCache(final String key, final T value);

    <T extends Parcelable> void putCache(final String key, final List<T> values);

    <T extends Parcelable> T getCache(final String key, final Class itemClass);

    <T extends Parcelable> List<T> getListCache(final String key, final Class itemClass);

    void clearCache(final String key);

    void clearCache();

}
