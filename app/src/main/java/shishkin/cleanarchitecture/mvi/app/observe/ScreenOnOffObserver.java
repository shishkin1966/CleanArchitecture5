package shishkin.cleanarchitecture.mvi.app.observe;

import android.content.Intent;


import java.util.List;


import shishkin.cleanarchitecture.mvi.app.SLUtil;
import shishkin.cleanarchitecture.mvi.common.utils.StringUtils;
import shishkin.cleanarchitecture.mvi.sl.ObservableSubscriber;
import shishkin.cleanarchitecture.mvi.sl.ObservableUnionImpl;
import shishkin.cleanarchitecture.mvi.sl.data.Result;
import shishkin.cleanarchitecture.mvi.sl.observe.ScreenBroadcastReceiverObservable;
import shishkin.cleanarchitecture.mvi.sl.state.ViewStateObserver;

/**
 * Слушатель гашения/включения экрана
 */
public class ScreenOnOffObserver implements ObservableSubscriber<Intent> {

    public static final String NAME = ScreenOnOffObserver.class.getName();

    private static volatile ScreenOnOffObserver sInstance;

    public static void instantiate() {
        if (sInstance == null) {
            synchronized (ScreenOnOffObserver.class) {
                if (sInstance == null) {
                    sInstance = new ScreenOnOffObserver();
                }
            }
        }
    }

    public static ScreenOnOffObserver getInstance() {
        if (sInstance == null) {
            instantiate();
        }
        return sInstance;
    }

    private ScreenOnOffObserver() {
    }

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public List<String> getObservable() {
        return StringUtils.arrayToList(ScreenBroadcastReceiverObservable.NAME);
    }

    @Override
    public void onChange(Intent intent) {
        final String strAction = intent.getAction();

        if (strAction.equals(Intent.ACTION_SCREEN_OFF)) {
            onScreenOff();
        } else {
            onScreenOn();
        }
    }

    private void onScreenOff() {
        SLUtil.getLocationUnion().stop();
        SLUtil.getMediaSpecialist().pause();
    }

    private void onScreenOn(){
        SLUtil.getLocationUnion().start();
        SLUtil.getMediaSpecialist().resume();
    }

    @Override
    public List<String> getSpecialistSubscription() {
        return StringUtils.arrayToList(ObservableUnionImpl.NAME);
    }

    @Override
    public int getState() {
        return ViewStateObserver.STATE_RESUME;
    }

    @Override
    public void setState(int state) {
    }

    @Override
    public Result<Boolean> validateExt() {
        return new Result<>(true);
    }

    @Override
    public boolean validate() {
        return validateExt().getData();
    }
}
