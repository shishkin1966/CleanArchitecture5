package shishkin.cleanarchitecture.mvi.sl.ui;

import android.app.Activity;
import android.content.Intent;

/**
 * Callback to receive result of activity launch.
 */
public interface ActivityResultListener {

    /**
     * Called when an activity you launched exits, giving you the requestCode
     * you started it with, the resultCode it returned, and any additional
     * data from it. The <var>resultCode</var> will be
     * {@link Activity#RESULT_CANCELED} if the activity explicitly returned that,
     * didn't return any result, or crashed during its operation.
     * <p/>
     *
     * @param requestCode The integer request code originally supplied to
     *                    startActivityForResult(), allowing you to identify who this
     *                    result came from.
     * @param resultCode  The integer result code returned by the child activity
     *                    through its setResult().
     * @param data        An Intent, which can return result data to the caller
     *                    (various data can be attached to Intent "extras").
     * @see Activity#startActivityForResult
     * @see android.support.v4.app.Fragment#startActivityForResult
     * @see Activity#createPendingResult
     * @see Activity#setResult(int)
     */
    void onActivityResult(final int requestCode, final int resultCode, final Intent data);

}
