package shishkin.cleanarchitecture.mvi.sl.delegate;

import java.util.List;

/**
 * Created by Shishkin on 14.03.2018.
 */

public interface Delegate<T extends Delegating> {

    void processing(Object object);

    T get(Object object);

    List<T> getAll();

}
