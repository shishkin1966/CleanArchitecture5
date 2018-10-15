package shishkin.cleanarchitecture.mvi.sl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


import shishkin.cleanarchitecture.mvi.common.utils.StringUtils;

/**
 * Секретарь - ведет учет своих сотрудников
 */
public class SecretaryImpl<T> implements Secretary<T> {

    private Map<String, T> mSubscribers = Collections.synchronizedMap(new ConcurrentHashMap<>());

    @Override
    public T remove(String key) {
        if (StringUtils.isNullOrEmpty(key)) return null;

        return mSubscribers.remove(key);
    }

    @Override
    public int size() {
        return mSubscribers.size();
    }

    @Override
    public T put(String key, T value) {
        if (value == null) return null;
        if (StringUtils.isNullOrEmpty(key)) return null;

        return mSubscribers.put(key, value);
    }

    @Override
    public boolean containsKey(String key) {
        if (StringUtils.isNullOrEmpty(key)) return false;

        return mSubscribers.containsKey(key);
    }

    @Override
    public T get(String key) {
        if (StringUtils.isNullOrEmpty(key)) return null;

        return mSubscribers.get(key);
    }

    @Override
    public List<T> values() {
        final List<T> list = new ArrayList<>();
        list.addAll(mSubscribers.values());
        return list;
    }

    @Override
    public boolean isEmpty() {
        return mSubscribers.isEmpty();
    }
}
