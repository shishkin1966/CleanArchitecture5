package shishkin.cleanarchitecture.mvi.common;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;

/**
 * Created by Shishkin on 08.11.2017.
 */

public class KeyboardRunnable implements Runnable {

    private static final int INTERVAL_MS = 100;
    private final Handler handler = new Handler();
    private Activity parentActivity = null;
    private View targetView = null;
    private int mode = WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE;

    public KeyboardRunnable(final Activity parentActivity, final View targetView) {
        this.parentActivity = parentActivity;
        this.targetView = targetView;
    }

    public KeyboardRunnable(final Activity parentActivity, final View targetView, final int mode) {
        this.parentActivity = parentActivity;
        this.targetView = targetView;
        this.mode = mode;
    }

    @Override
    public void run() {
        if (parentActivity == null || targetView == null) {
            return;
        }

        if (parentActivity.isFinishing()) {
            return;
        }

        parentActivity.getWindow().setSoftInputMode(mode);
        final InputMethodManager imm = (InputMethodManager) parentActivity.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (!(targetView.isFocusable() && targetView.isFocusableInTouchMode())) {
            return;
        } else if (!targetView.requestFocus()) {
            post();
        } else if (!imm.isActive(targetView)) {
            post();
        } else if (!imm.showSoftInput(targetView, InputMethodManager.SHOW_IMPLICIT)) {
            post();
        }
    }

    private void post() {
        handler.postDelayed(this, INTERVAL_MS);
    }
}