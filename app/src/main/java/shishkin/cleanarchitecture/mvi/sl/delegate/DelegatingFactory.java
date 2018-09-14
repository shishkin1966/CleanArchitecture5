package shishkin.cleanarchitecture.mvi.sl.delegate;

/**
 * Created by Shishkin on 07.03.2018.
 */

public interface DelegatingFactory<T> {
    T create(Object object);
}
