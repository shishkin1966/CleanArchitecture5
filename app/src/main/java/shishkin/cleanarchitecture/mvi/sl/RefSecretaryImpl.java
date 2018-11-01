package shishkin.cleanarchitecture.mvi.sl;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


import shishkin.cleanarchitecture.mvi.common.utils.StringUtils;

/**
 * Секретарь - ведет учет своих сотрудников
 */
public class RefSecretaryImpl<T> implements Secretary<T> {

    private Map<String, WeakReference<T>> subscribers = Collections.synchronizedMap(new ConcurrentHashMap<>());

    @Override
    public T remove(String key) {
        if (StringUtils.isNullOrEmpty(key)) return null;

        checkNull();
        return subscribers.remove(key).get();
    }

    @Override
    public int size() {
        checkNull();
        return subscribers.size();
    }

    @Override
    public T put(String key, T value) {
        if (value == null) return null;
        if (StringUtils.isNullOrEmpty(key)) return null;

        subscribers.put(key, new WeakReference<>(value));
        return get(key);
    }

    @Override
    public boolean containsKey(String key) {
        if (StringUtils.isNullOrEmpty(key)) return false;

        checkNull();
        return subscribers.containsKey(key);
    }

    @Override
    public T get(String key) {
        if (StringUtils.isNullOrEmpty(key)) return null;

        checkNull();
        return subscribers.get(key).get();
    }

    @Override
    public List<T> values() {
        checkNull();
        final List<T> list = new ArrayList<>();
        for (WeakReference<T> reference : subscribers.values()) {
            list.add(reference.get());
        }
        return list;
    }

    @Override
    public boolean isEmpty() {
        checkNull();
        return subscribers.isEmpty();
    }

    private void checkNull() {
        for (Map.Entry<String, WeakReference<T>> entry : subscribers.entrySet()) {
            if (entry.getValue() == null || entry.getValue().get() == null) {
                subscribers.remove(entry.getKey());
            }
        }
    }

    @Override
    public void clear() {
        subscribers.clear();
    }

    @Override
    public Collection<String> keys() {
        return subscribers.keySet();
    }
}
