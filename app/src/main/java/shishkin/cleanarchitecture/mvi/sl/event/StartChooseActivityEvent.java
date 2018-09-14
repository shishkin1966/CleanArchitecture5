package shishkin.cleanarchitecture.mvi.sl.event;

import android.content.Intent;

/**
 * Событие - выполнить команду "start choose activity"
 */
@SuppressWarnings("unused")
public class StartChooseActivityEvent extends AbsEvent {

    private Intent mIntent;
    private String mTitle;

    public <F> StartChooseActivityEvent(final Intent intent, final String title) {
        mIntent = intent;
        mTitle = title;
    }

    public Intent getIntent() {
        return mIntent;
    }

    public String getTitle() {
        return mTitle;
    }
}
