package shishkin.cleanarchitecture.mvi.sl;

import java.util.Collection;
import java.util.List;

public interface Secretary<T> {

    T remove(String key);

    int size();

    T put(String key, T value);

    boolean containsKey(String key);

    T get(String key);

    List<T> values();

    boolean isEmpty();

    void clear();

    Collection<String> keys();
}
