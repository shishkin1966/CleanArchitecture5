package shishkin.cleanarchitecture.mvi.app.storage;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;


import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;


import shishkin.cleanarchitecture.mvi.common.utils.StringUtils;
import shishkin.cleanarchitecture.mvi.sl.AbsSpecialist;
import shishkin.cleanarchitecture.mvi.sl.ErrorSpecialistImpl;
import shishkin.cleanarchitecture.mvi.sl.data.Result;

public class StorageSpecialistImpl extends AbsSpecialist implements StorageSpecialist {

    public static final String NAME = StorageSpecialistImpl.class.getName();

    private static final String PARCELABLE = "PARCELABLE";
    private static final String LIST = "LIST";
    private static final long MAX_SIZE = 1000L;
    private static final long DURATION = 3;
    private static final TimeUnit DURATION_TIMEUNIT = TimeUnit.MINUTES;

    private ReentrantLock mLock = new ReentrantLock();
    private LoadingCache<String, byte[]> mCache;
    private byte[] mValue;

    @Override
    public void onRegister() {
        mCache = CacheBuilder.newBuilder()
                .maximumSize(MAX_SIZE)
                .expireAfterWrite(DURATION, DURATION_TIMEUNIT)
                .build(
                        new CacheLoader<String, byte[]>() {
                            public byte[] load(String key) {
                                return mValue;
                            }
                        });
    }

    @Override
    public <T extends Parcelable> void putCache(final String key, final T value) {
        if (StringUtils.isNullOrEmpty(key) || value == null || mCache == null) {
            return;
        }

        if (!validate()) {
            return;
        }

        Parcel parcel = null;

        mLock.lock();

        try {
            parcel = Parcel.obtain();
            parcel.writeString(PARCELABLE);
            parcel.writeParcelable(value, 0);
            mValue = parcel.marshall();
            mCache.put(key, mValue);
        } catch (Exception e) {
            ErrorSpecialistImpl.getInstance().onError(NAME, e);
        } finally {
            if (parcel != null) {
                parcel.recycle();
            }
            mLock.unlock();
        }
    }

    @Override
    public <T extends Parcelable> void putCache(final String key, final List<T> values) {
        if (StringUtils.isNullOrEmpty(key) || values == null) {
            return;
        }

        if (!validate()) {
            return;
        }

        Parcel parcel = null;

        mLock.lock();

        try {
            parcel = Parcel.obtain();
            parcel.writeString(LIST);
            parcel.writeList(values);
            mValue = parcel.marshall();
            mCache.put(key, mValue);
        } catch (Exception e) {
            ErrorSpecialistImpl.getInstance().onError(NAME, e);
        } finally {
            if (parcel != null) {
                parcel.recycle();
            }
            mLock.unlock();
        }
    }

    @Override
    public <T extends Parcelable> T getCache(final String key, final Class itemClass) {
        if (StringUtils.isNullOrEmpty(key)) {
            return null;
        }

        Parcel parcel = null;

        mLock.lock();

        try {
            parcel = Parcel.obtain();
            final byte[] value = mCache.getIfPresent(key);
            if (value != null) {
                parcel.unmarshall(value, 0, value.length);
                parcel.setDataPosition(0);
                final String type = parcel.readString();
                if (PARCELABLE.equals(type)) {
                    return (T) parcel.readParcelable(itemClass.getClassLoader());
                }
            }
        } catch (Exception e) {
            ErrorSpecialistImpl.getInstance().onError(NAME, e);
        } finally {
            if (parcel != null) {
                parcel.recycle();
            }
            mLock.unlock();
        }
        return null;
    }

    @Override
    public <T extends Parcelable> List<T> getListCache(final String key, final Class itemClass) {
        if (StringUtils.isNullOrEmpty(key)) {
            return null;
        }

        Parcel parcel = null;

        mLock.lock();

        try {
            parcel = Parcel.obtain();
            final byte[] value = mCache.getIfPresent(key);
            if (value != null) {
                parcel.unmarshall(value, 0, value.length);
                parcel.setDataPosition(0);
                final String type = parcel.readString();
                if (LIST.equals(type)) {
                    final ArrayList<T> res = new ArrayList<>();
                    parcel.readList(res, itemClass.getClassLoader());
                    return res;
                }
            }
        } catch (Exception e) {
            ErrorSpecialistImpl.getInstance().onError(NAME, e);
        } finally {
            if (parcel != null) {
                parcel.recycle();
            }
            mLock.unlock();
        }
        return null;
    }

    @Override
    public Result<Boolean> validateExt() {
        final Runtime runtime = Runtime.getRuntime();
        final long percent = 100 - ((runtime.totalMemory() - runtime.freeMemory()) * 100 / runtime.maxMemory());
        return new Result<>(percent >= 15).setError(NAME, "Not enough memory");
    }

    @Override
    public void clearCache(final String key) {
        if (StringUtils.isNullOrEmpty(key)) {
            return;
        }

        mLock.lock();

        try {
            mCache.invalidate(key);
        } catch (Exception e) {
            ErrorSpecialistImpl.getInstance().onError(NAME, e);
        } finally {
            mLock.unlock();
        }
    }

    @Override
    public void clearCache() {
        mLock.lock();

        try {
            mCache.invalidateAll();
        } catch (Exception e) {
            ErrorSpecialistImpl.getInstance().onError(NAME, e);
        } finally {
            mLock.unlock();
        }
    }


    @Override
    public int compareTo(@NonNull Object o) {
        return (StorageSpecialist.class.isInstance(o)) ? 0 : 1;
    }

    @Override
    public String getName() {
        return NAME;
    }
}
