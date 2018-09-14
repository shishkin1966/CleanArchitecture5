package shishkin.cleanarchitecture.mvi.sl.event;

import android.content.Intent;

/**
 * Событие - выполнить команду "start activity"
 */
@SuppressWarnings("unused")
public class StartActivityEvent extends AbsEvent {

    private Intent mIntent;

    public <F> StartActivityEvent(final Intent intent) {
        mIntent = intent;
    }

    public Intent getIntent() {
        return mIntent;
    }
}
