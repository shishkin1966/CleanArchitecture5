package shishkin.cleanarchitecture.mvi.sl.event;

import android.content.Intent;

/**
 * Событие - выполнить команду "start activity for result"
 */
@SuppressWarnings("unused")
public class StartActivityForResultEvent extends AbsEvent {

    private Intent mIntent;
    private int mRequestCode;

    public <F> StartActivityForResultEvent(final Intent intent, final int requestCode) {
        mIntent = intent;
        mRequestCode = requestCode;
    }

    public Intent getIntent() {
        return mIntent;
    }

    public int getRequestCode() {
        return mRequestCode;
    }
}
