package shishkin.cleanarchitecture.mvi.app.storage;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

import com.google.common.base.Charsets;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.io.CharStreams;
import com.jakewharton.disklrucache.DiskLruCache;


import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.lang.ref.WeakReference;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;


import shishkin.cleanarchitecture.mvi.common.utils.CloseUtils;
import shishkin.cleanarchitecture.mvi.common.utils.Constant;
import shishkin.cleanarchitecture.mvi.common.utils.StringUtils;
import shishkin.cleanarchitecture.mvi.sl.AbsSpecialist;
import shishkin.cleanarchitecture.mvi.sl.ApplicationSpecialistImpl;
import shishkin.cleanarchitecture.mvi.sl.ErrorSpecialistImpl;
import shishkin.cleanarchitecture.mvi.sl.data.Result;
import shishkin.cleanarchitecture.mvi.sl.ui.IActivity;

@SuppressWarnings("unused")
public class StorageSpecialistImpl extends AbsSpecialist implements StorageSpecialist {

    public static final String NAME = StorageSpecialistImpl.class.getName();

    private static final String PARCELABLE = "PARCELABLE";
    private static final String LIST = "LIST";
    private static final long MAX_SIZE = 1000L;
    private static final long DURATION = 3;
    private static final TimeUnit DURATION_TIMEUNIT = TimeUnit.MINUTES;
    private static final String DISK_CACHE_DIR = ApplicationSpecialistImpl.getInstance().getCacheDir().getAbsolutePath() + File.separator + "ParcelableDiskCache";
    private static final int DISK_CACHE_SIZE = Constant.MB * 10; // 10MB
    private static final int BUFFER_SIZE = Constant.KB * 16; // 16kb
    private static final int INDEX_EXPIRED = 0;
    private static final int INDEX_DATA = 1;
    private static final int COUNT_INDEX = 2;

    private ReentrantLock mLock = new ReentrantLock();
    private LoadingCache<String, byte[]> mCache;
    private byte[] mValue;
    private DiskLruCache mDiskLruCache;

    @Override
    public void onRegister() {
        mLock.lock();

        if (mCache == null) {
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

        final int version = ApplicationSpecialistImpl.getInstance().getVersion();

        try {
            if (mDiskLruCache == null || mDiskLruCache.isClosed()) {
                final File dir = new File(DISK_CACHE_DIR + File.separator + version);
                if (!dir.exists() && !dir.mkdirs()) {
                    return;
                }
                mDiskLruCache = DiskLruCache.open(dir, version, COUNT_INDEX, DISK_CACHE_SIZE);
            }
        } catch (Exception e) {
            ErrorSpecialistImpl.getInstance().onError(NAME, e);
        } finally {
            mLock.unlock();
        }
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
    public <T extends Parcelable> void put(final String key, final T value) {
        put(key, value, 0);
    }

    @Override
    public <T extends Parcelable> void put(final String key, final T value, final long expired) {
        if (mDiskLruCache == null || StringUtils.isNullOrEmpty(key) || value == null) {
            return;
        }

        mLock.lock();

        Parcel parcel = null;
        final String hash = hashKeyForDisk(key);
        OutputStream out = null;
        DiskLruCache.Editor editor = null;
        DiskLruCache.Snapshot snapshot = null;

        try {
            parcel = Parcel.obtain();
            parcel.writeString(PARCELABLE);
            parcel.writeParcelable(value, 0);

            snapshot = mDiskLruCache.get(hash);
            if (snapshot == null) {
                editor = mDiskLruCache.edit(hash);
                if (editor != null) {
                    out = editor.newOutputStream(INDEX_EXPIRED);
                    writeToOutputStream(out, String.valueOf(expired).getBytes(Charsets.UTF_8));

                    out = new BufferedOutputStream(editor.newOutputStream(INDEX_DATA), BUFFER_SIZE);
                    writeToOutputStream(out, parcel.marshall());

                    editor.commit();
                }
                mDiskLruCache.flush();
            }
        } catch (Exception e) {
            ErrorSpecialistImpl.getInstance().onError(NAME, e);
            if (editor != null) {
                try {
                    editor.abort();
                } catch (Exception e1) {
                    ErrorSpecialistImpl.getInstance().onError(NAME, e1);
                }
            }
        } finally {
            CloseUtils.close(out);
            CloseUtils.close(snapshot);
            if (parcel != null) {
                parcel.recycle();
            }
            mLock.unlock();
        }
    }

    @Override
    public <T extends Parcelable> void put(final String key, final List<T> values) {
        put(key, values, 0);
    }

    @Override
    public <T extends Parcelable> void put(final String key, final List<T> values, final long expired) {
        if (mDiskLruCache == null || StringUtils.isNullOrEmpty(key) || values == null) {
            return;
        }

        final String hash = hashKeyForDisk(key);
        OutputStream out = null;
        DiskLruCache.Editor editor = null;
        DiskLruCache.Snapshot snapshot = null;
        Parcel parcel = null;

        mLock.lock();

        try {
            parcel = Parcel.obtain();
            parcel.writeString(LIST);
            parcel.writeList(values);

            snapshot = mDiskLruCache.get(hash);
            if (snapshot == null) {
                editor = mDiskLruCache.edit(hash);
                if (editor != null) {
                    out = editor.newOutputStream(INDEX_EXPIRED);
                    writeToOutputStream(out, String.valueOf(expired).getBytes(Charsets.UTF_8));

                    out = new BufferedOutputStream(editor.newOutputStream(INDEX_DATA), BUFFER_SIZE);
                    writeToOutputStream(out, parcel.marshall());

                    editor.commit();
                }
                mDiskLruCache.flush();
            }
        } catch (Exception e) {
            ErrorSpecialistImpl.getInstance().onError(NAME, e);
            if (editor != null) {
                try {
                    editor.abort();
                } catch (Exception e1) {
                    ErrorSpecialistImpl.getInstance().onError(NAME, e1);
                }
            }
        } finally {
            CloseUtils.close(out);
            CloseUtils.close(snapshot);
            if (parcel != null) {
                parcel.recycle();
            }
            mLock.unlock();
        }
    }

    @Override
    public <T extends Parcelable> T get(final String key, final Class itemClass) {
        if (mDiskLruCache == null || StringUtils.isNullOrEmpty(key) || itemClass == null) {
            return null;
        }

        final String hash = hashKeyForDisk(key);
        InputStream inputStream = null;
        long expired = -1;
        DiskLruCache.Snapshot snapshot = null;
        Parcel parcel = null;

        mLock.lock();

        try {
            parcel = Parcel.obtain();
            snapshot = mDiskLruCache.get(hash);
            if (snapshot != null) {
                inputStream = snapshot.getInputStream(INDEX_EXPIRED);
                if (inputStream != null) {
                    final String s = CharStreams.toString(new InputStreamReader(
                            inputStream, Charsets.UTF_8));
                    inputStream.close();
                    if (!StringUtils.isNullOrEmpty(s)) {
                        expired = StringUtils.toLong(s);
                    }
                }

                if (expired == 0 || expired >= System.currentTimeMillis()) {
                    inputStream = snapshot.getInputStream(INDEX_DATA);
                    if (inputStream != null) {
                        final byte[] value = getFromInputStream(inputStream);
                        inputStream.close();
                        if (value != null) {
                            parcel.unmarshall(value, 0, value.length);
                            parcel.setDataPosition(0);
                            final String type = parcel.readString();
                            if (PARCELABLE.equals(type)) {
                                return parcel.readParcelable(itemClass.getClassLoader());
                            }
                        }
                    }
                } else {
                    snapshot.close();
                    mDiskLruCache.remove(hash);
                    mDiskLruCache.flush();
                }
            }
        } catch (final IOException e) {
            ErrorSpecialistImpl.getInstance().onError(NAME, e);
        } finally {
            CloseUtils.close(inputStream);
            CloseUtils.close(snapshot);
            if (parcel != null) {
                parcel.recycle();
            }
            mLock.unlock();
        }
        return null;
    }

    @Override
    public <T extends Parcelable> List<T> getList(final String key, final Class itemClass) {
        if (mDiskLruCache == null || StringUtils.isNullOrEmpty(key) || itemClass == null) {
            return null;
        }

        final String hash = hashKeyForDisk(key);
        InputStream inputStream = null;
        long expired = -1;
        DiskLruCache.Snapshot snapshot = null;
        Parcel parcel = null;

        mLock.lock();

        try {
            parcel = Parcel.obtain();
            snapshot = mDiskLruCache.get(hash);
            if (snapshot != null) {
                inputStream = snapshot.getInputStream(INDEX_EXPIRED);
                if (inputStream != null) {
                    String s = CharStreams.toString(new InputStreamReader(
                            inputStream, Charsets.UTF_8));
                    inputStream.close();
                    if (!StringUtils.isNullOrEmpty(s)) {
                        expired = StringUtils.toLong(s);
                    }
                }

                if (expired == 0 || expired >= System.currentTimeMillis()) {
                    inputStream = snapshot.getInputStream(INDEX_DATA);
                    if (inputStream != null) {
                        final byte[] value = getFromInputStream(inputStream);
                        inputStream.close();
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
                    }
                } else {
                    snapshot.close();
                    mDiskLruCache.remove(hash);
                    mDiskLruCache.flush();
                }
            }
        } catch (final IOException e) {
            ErrorSpecialistImpl.getInstance().onError(NAME, e);
        } finally {
            CloseUtils.close(inputStream);
            CloseUtils.close(snapshot);
            if (parcel != null) {
                parcel.recycle();
            }
            mLock.unlock();
        }
        return null;
    }

    private void writeToOutputStream(final OutputStream stream, final byte[] array) throws IOException {
        stream.write(array);
        stream.flush();
        stream.close();
    }

    private byte[] getFromInputStream(final InputStream is) throws IOException {
        final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        try {
            final byte[] data = new byte[1024];
            int count;
            while ((count = is.read(data, 0, data.length)) != -1) {
                outputStream.write(data, 0, count);
            }
            outputStream.flush();
            return outputStream.toByteArray();
        } finally {
            CloseUtils.close(is);
            CloseUtils.close(outputStream);
        }
    }

    private String hashKeyForDisk(String key) {
        String cacheKey;
        try {
            final MessageDigest mDigest = MessageDigest.getInstance("MD5");
            mDigest.update(key.getBytes(Charsets.UTF_8));
            cacheKey = StringUtils.byteArrayToHex(mDigest.digest());
        } catch (NoSuchAlgorithmException e) {
            cacheKey = String.valueOf(key.hashCode());
        }
        return cacheKey;
    }

    @Override
    public void clear(final String key) {
        if (mDiskLruCache == null || StringUtils.isNullOrEmpty(key)) {
            return;
        }

        final String hash = hashKeyForDisk(key);

        mLock.lock();

        try {
            final DiskLruCache.Snapshot snapshot = mDiskLruCache.get(hash);
            if (snapshot != null) {
                snapshot.close();
                mDiskLruCache.remove(hash);
                mDiskLruCache.flush();
            }
        } catch (final IOException e) {
            ErrorSpecialistImpl.getInstance().onError(NAME, e);
        } finally {
            mLock.unlock();
        }
    }

    @Override
    public void clear() {
        if (mDiskLruCache != null && !mDiskLruCache.isClosed()) {
            mLock.lock();

            try {
                mDiskLruCache.delete();
            } catch (IOException e) {
                ErrorSpecialistImpl.getInstance().onError(NAME, e);
            } finally {
                mLock.unlock();
            }
            mDiskLruCache = null;

            onRegister();
        }
    }

    private void flush() {
        if (mDiskLruCache == null) {
            return;
        }

        mLock.lock();

        try {
            mDiskLruCache.flush();
        } catch (IOException e) {
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

    @Override
    public void onFinishApplication() {
        clearCache();
        flush();
    }

}
