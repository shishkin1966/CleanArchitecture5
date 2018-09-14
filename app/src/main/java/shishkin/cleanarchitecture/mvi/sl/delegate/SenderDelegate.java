package shishkin.cleanarchitecture.mvi.sl.delegate;


import java.util.List;

/**
 * Created by Shishkin on 07.03.2018.
 */

public interface SenderDelegate<T extends SenderDelegating> {

    void processing(Object sender, Object object);

    T get(Object sender);

    List<T> getAll();
}
