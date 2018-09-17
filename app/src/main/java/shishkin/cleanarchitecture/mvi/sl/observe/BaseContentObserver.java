package shishkin.cleanarchitecture.mvi.sl.observe;

import android.database.ContentObserver;
import android.os.Handler;

/**
 * Created by Shishkin on 15.12.2017.
 */

public class BaseContentObserver extends ContentObserver {

    private final Observable mObservable;

    public BaseContentObserver(Observable observable) {
        super(new Handler());

        mObservable = observable;
    }

    @Override
    public void onChange(final boolean selfChange) {
        super.onChange(selfChange);

        mObservable.onChange(selfChange);
    }
}
