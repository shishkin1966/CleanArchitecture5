package shishkin.cleanarchitecture.mvi.sl.delegate;


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by Shishkin on 07.03.2018.
 */

public abstract class AbsDelegate<T extends Delegating> implements Delegate<T> {

    private Map<String, T> mDelegates = Collections.synchronizedMap(new ConcurrentHashMap<String, T>());
    private DelegatingFactory<T> mDelegateFactory = getDelegateFactory();

    public abstract DelegatingFactory<T> getDelegateFactory();

    @Override
    public void processing(Object object) {
        if (object != null) {
            if (!mDelegates.containsKey(object.getClass().getName())) {
                final T delegate = mDelegateFactory.create(object.getClass().getName());
                mDelegates.put(object.getClass().getName(), delegate);
            }
            if (mDelegates.containsKey(object.getClass().getName())) {
                final T delegating = mDelegates.get(object.getClass().getName());
                if (delegating != null) {
                    delegating.processing(object);
                }
            }
        }
    }

    @Override
    public T get(Object object) {
        if (!mDelegates.containsKey(object.getClass().getName())) {
            final T delegate = mDelegateFactory.create(object.getClass().getName());
            mDelegates.put(object.getClass().getName(), delegate);
        }
        if (mDelegates.containsKey(object.getClass().getName())) {
            return (T) mDelegates.get(object.getClass().getName());
        }
        return null;
    }

    @Override
    public List<T> getAll() {
        final ArrayList<T> list = new ArrayList<>();
        list.addAll(mDelegates.values());
        return list;
    }

}
