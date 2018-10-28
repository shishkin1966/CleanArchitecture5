package shishkin.cleanarchitecture.mvi.sl;

import android.support.annotation.NonNull;

import com.google.gson.Gson;


import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;


import io.paperdb.Paper;
import shishkin.cleanarchitecture.mvi.common.utils.StringUtils;

public class SerializableDiskStorageSpecialistImpl extends AbsSpecialist implements SerializableDiskStorageSpecialist {

    public static final String NAME = SerializableDiskStorageSpecialistImpl.class.getName();
    private static final String TIME = SerializableDiskStorageSpecialistImpl.class.getName() + ".time";

    private ReentrantLock lock;
    private Gson gson;

    @Override
    public void onRegister() {
        lock = new ReentrantLock();
        gson = new Gson();
        Paper.init(ApplicationSpecialistImpl.getInstance());
    }

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public void put(final String key, final Serializable value) {
        if (StringUtils.isNullOrEmpty(key)) {
            return;
        }

        lock.lock();

        try {
            if (value == null) {
                Paper.book(NAME).delete(key);
            } else {
                Paper.book(NAME).write(key, value);
            }
        } catch (Exception e) {
            ErrorSpecialistImpl.getInstance().onError(NAME, e);
        } finally {
            lock.unlock();
        }
    }

    @Override
    public void put(final String key, final List<Serializable> values) {
        if (StringUtils.isNullOrEmpty(key)) {
            return;
        }

        lock.lock();

        try {
            final Serializable s = toSerializable(values);
            if (values == s) {
                Paper.book(NAME).delete(key);
            } else {
                Paper.book(NAME).write(key, values);
            }
        } catch (Exception e) {
            ErrorSpecialistImpl.getInstance().onError(NAME, e);
        } finally {
            lock.unlock();
        }
    }

    @Override
    public void put(final String key, final Serializable value, final long expired) {
        if (StringUtils.isNullOrEmpty(key)) {
            return;
        }

        if (expired < System.currentTimeMillis()) {
            return;
        }

        lock.lock();

        try {
            if (value == null) {
                deleteKeys(key);
            } else {
                Paper.book(NAME).write(key, value);
                Paper.book(TIME).write(key, expired);
            }
        } catch (Exception e) {
            ErrorSpecialistImpl.getInstance().onError(NAME, e);
        } finally {
            lock.unlock();
        }
    }

    private void deleteKeys(final String key) {
        if (Paper.book(NAME).contains(key)) {
            Paper.book(NAME).delete(key);
        }
        if (Paper.book(TIME).contains(key)) {
            Paper.book(TIME).delete(key);
        }
    }

    @Override
    public Serializable get(final String key) {
        if (StringUtils.isNullOrEmpty(key)) {
            return null;
        }

        lock.lock();

        try {
            if (Paper.book(NAME).contains(key)) {
                if (Paper.book(TIME).contains(key)) {
                    final long expired = Paper.book(TIME).read(key);
                    if (expired < System.currentTimeMillis()) {
                        deleteKeys(key);
                        return null;
                    }
                }
                return Paper.book(NAME).read(key);
            } else {
                deleteKeys(key);
            }
        } catch (Exception e) {
            ErrorSpecialistImpl.getInstance().onError(NAME, e);
        } finally {
            lock.unlock();
        }
        return null;
    }

    @Override
    public List<Serializable> getList(String key) {
        if (StringUtils.isNullOrEmpty(key)) {
            return null;
        }

        lock.lock();

        try {
            if (Paper.book(NAME).contains(key)) {
                if (Paper.book(TIME).contains(key)) {
                    final long expired = Paper.book(TIME).read(key);
                    if (expired < System.currentTimeMillis()) {
                        deleteKeys(key);
                        return null;
                    }
                }

                final Serializable s = Paper.book(NAME).read(key);
                if (s != null) {
                    return serializableToList(s);
                }
            } else {
                deleteKeys(key);
            }
        } catch (Exception e) {
            ErrorSpecialistImpl.getInstance().onError(NAME, e);
        } finally {
            lock.unlock();
        }
        return null;
    }

    @Override
    public Serializable get(final String key, final Serializable defaultValue) {
        final Serializable value = get(key);
        if (value == null) {
            return defaultValue;
        }
        return value;
    }

    @Override
    public void clear(final String key) {
        if (StringUtils.isNullOrEmpty(key)) {
            return;
        }

        lock.lock();

        try {
            deleteKeys(key);
        } catch (Exception e) {
            ErrorSpecialistImpl.getInstance().onError(NAME, e);
        } finally {
            lock.unlock();
        }
    }

    @Override
    public void clear() {
        lock.lock();

        try {
            Paper.book(NAME).destroy();
            Paper.book(TIME).destroy();
        } catch (Exception e) {
            ErrorSpecialistImpl.getInstance().onError(NAME, e);
        } finally {
            lock.unlock();
        }
    }

    @Override
    public void check() {
        lock.lock();

        try {
            final List<String> list = Paper.book(TIME).getAllKeys();
            for (String key : list) {
                long expired = Paper.book(TIME).read(key);
                if (expired < System.currentTimeMillis()) {
                    deleteKeys(key);
                }
            }
        } catch (Exception e) {
            ErrorSpecialistImpl.getInstance().onError(NAME, e);
        } finally {
            lock.unlock();
        }
    }

    @Override
    public <T> Serializable toSerializable(List<T> list) {
        if (list == null) {
            return null;
        }

        final LinkedList<T> linkedList = new LinkedList<>();
        linkedList.addAll(list);
        return linkedList;
    }

    @Override
    public <T> List<T> serializableToList(Serializable value) {
        if (value == null) {
            return null;
        }

        if (value instanceof LinkedList) {
            LinkedList<T> items = (LinkedList) value;
            final List<T> list = new ArrayList<>();
            list.addAll(items);
            return list;
        } else if (value instanceof ArrayList) {
            return (ArrayList) value;
        }
        return null;
    }

    @Override
    public <T> Serializable toJson(final T obj) {
        return gson.toJson(obj);
    }

    @Override
    public <T> Serializable toJson(final T obj, Type type) {
        //use example : type = new com.google.gson.reflect.TypeToken<List<ContactItem>>(){}.getType()
        return gson.toJson(obj, type);
    }

    @Override
    public <T> T fromJson(final String json, final Class<T> cl) {
        return gson.fromJson(json, cl);
    }

    @Override
    public <T> T fromJson(final String json, Type type) {
        // use example : type = new com.google.gson.reflect.TypeToken<List<ContactItem>>(){}.getType()
        return gson.fromJson(json, type);
    }

    @Override
    public int compareTo(@NonNull Object o) {
        return (SerializableDiskStorageSpecialist.class.isInstance(o)) ? 0 : 1;
    }
}
