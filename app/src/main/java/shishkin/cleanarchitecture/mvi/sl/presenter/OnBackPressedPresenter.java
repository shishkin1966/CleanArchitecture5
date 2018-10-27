package shishkin.cleanarchitecture.mvi.sl.presenter;

import android.content.Context;
import android.support.design.widget.Snackbar;


import java.util.Timer;
import java.util.TimerTask;


import shishkin.cleanarchitecture.mvi.R;
import shishkin.cleanarchitecture.mvi.app.SLUtil;
import shishkin.cleanarchitecture.mvi.sl.ApplicationSpecialistImpl;
import shishkin.cleanarchitecture.mvi.sl.SL;
import shishkin.cleanarchitecture.mvi.sl.ViewUnion;
import shishkin.cleanarchitecture.mvi.sl.ViewUnionImpl;
import shishkin.cleanarchitecture.mvi.sl.event.ShowMessageEvent;

public class OnBackPressedPresenter extends AbsPresenter {
    private static final String NAME = OnBackPressedPresenter.class.getName();

    private boolean mDoubleBackPressedOnce = false;
    private Timer mTimer;

    public boolean onClick() {
        if (validate()) {
            if (!mDoubleBackPressedOnce) {
                final Context context = SLUtil.getContext();
                if (context != null) {
                    mDoubleBackPressedOnce = true;
                    ((ViewUnion) SL.getInstance().get(ViewUnionImpl.NAME)).showSnackbar(new ShowMessageEvent(context.getString(R.string.double_back_pressed)).setAction(context.getString(R.string.exit)).setDuration(Snackbar.LENGTH_SHORT));
                    startTimer();
                }
            } else {
                ApplicationSpecialistImpl.getInstance().finish();
                return true;
            }
        }
        return false;
    }

    private void startTimer() {
        if (mTimer != null) {
            stopTimer();
        }
        mTimer = new Timer();
        mTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                mDoubleBackPressedOnce = false;
            }
        }, 3000);
    }

    private void stopTimer() {
        if (mTimer != null) {
            mTimer.cancel();
            mTimer = null;
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        stopTimer();
    }

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public boolean isRegister() {
        return false;
    }


}
