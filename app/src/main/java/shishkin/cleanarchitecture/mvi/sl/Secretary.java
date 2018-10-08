package shishkin.cleanarchitecture.mvi.sl;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Секретарь - ведет учет своих сотрудников
 */
public class Secretary<T> {

    private Map<String, T> mSubscribers = Collections.synchronizedMap(new ConcurrentHashMap<>());

    public Set<Map.Entry<String, T>> entrySet() {
        return mSubscribers.entrySet();
    }

    public T remove(String key) {
        return mSubscribers.remove(key);
    }

    public int size() {
        return mSubscribers.size();
    }

    public T put(String key, T value) {
        return mSubscribers.put(key, value);
    }

    public boolean containsKey(String key) {
        return mSubscribers.containsKey(key);
    }

    public T get(String key) {
        return mSubscribers.get(key);
    }

    public Collection<T> values() {
        return mSubscribers.values();
    }

    public boolean isEmpty() {
        return mSubscribers.isEmpty();
    }
}
