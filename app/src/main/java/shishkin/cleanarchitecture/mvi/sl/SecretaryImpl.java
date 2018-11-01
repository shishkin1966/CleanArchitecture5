package shishkin.cleanarchitecture.mvi.sl;

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
public class SecretaryImpl<T> implements Secretary<T> {

    private Map<String, T> subscribers = Collections.synchronizedMap(new ConcurrentHashMap<>());

    @Override
    public T remove(String key) {
        if (StringUtils.isNullOrEmpty(key)) return null;

        return subscribers.remove(key);
    }

    @Override
    public int size() {
        return subscribers.size();
    }

    @Override
    public T put(String key, T value) {
        if (value == null) return null;
        if (StringUtils.isNullOrEmpty(key)) return null;

        return subscribers.put(key, value);
    }

    @Override
    public boolean containsKey(String key) {
        if (StringUtils.isNullOrEmpty(key)) return false;

        return subscribers.containsKey(key);
    }

    @Override
    public T get(String key) {
        if (StringUtils.isNullOrEmpty(key)) return null;

        return subscribers.get(key);
    }

    @Override
    public List<T> values() {
        return new ArrayList<>(subscribers.values());
    }

    @Override
    public boolean isEmpty() {
        return subscribers.isEmpty();
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
