package shishkin.cleanarchitecture.mvi.sl;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


import shishkin.cleanarchitecture.mvi.common.utils.StringUtils;

/**
 * Секретарь - ведет учет своих сотрудников
 */
public class RefSecretaryImpl<T> implements Secretary<T> {

    private Map<String, WeakReference<T>> mSubscribers = Collections.synchronizedMap(new ConcurrentHashMap<>());

    @Override
    public T remove(String key) {
        if (StringUtils.isNullOrEmpty(key)) return null;

        checkNull();
        return mSubscribers.remove(key).get();
    }

    @Override
    public int size() {
        checkNull();
        return mSubscribers.size();
    }

    @Override
    public T put(String key, T value) {
        if (value == null) return null;
        if (StringUtils.isNullOrEmpty(key)) return null;

        mSubscribers.put(key, new WeakReference<>(value));
        return get(key);
    }

    @Override
    public boolean containsKey(String key) {
        if (StringUtils.isNullOrEmpty(key)) return false;

        checkNull();
        return mSubscribers.containsKey(key);
    }

    @Override
    public T get(String key) {
        if (StringUtils.isNullOrEmpty(key)) return null;

        checkNull();
        return mSubscribers.get(key).get();
    }

    @Override
    public List<T> values() {
        checkNull();
        final List<T> list = new ArrayList<>();
        for (WeakReference<T> reference : mSubscribers.values()) {
            list.add(reference.get());
        }
        return list;
    }

    @Override
    public boolean isEmpty() {
        checkNull();
        return mSubscribers.isEmpty();
    }

    private void checkNull() {
        for (Map.Entry<String, WeakReference<T>> entry : mSubscribers.entrySet()) {
            if (entry.getValue() == null || entry.getValue().get() == null) {
                mSubscribers.remove(entry.getKey());
            }
        }
    }
}
