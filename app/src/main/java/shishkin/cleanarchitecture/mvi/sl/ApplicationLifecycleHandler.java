package shishkin.cleanarchitecture.mvi.sl;

import android.app.Activity;
import android.app.Application;
import android.content.ComponentCallbacks2;
import android.content.res.Configuration;
import android.os.Bundle;

public class ApplicationLifecycleHandler implements Application.ActivityLifecycleCallbacks, ComponentCallbacks2 {

    private boolean isInBackground = false;

    @Override
    public void onActivityCreated(Activity activity, Bundle bundle) {
    }

    @Override
    public void onActivityStarted(Activity activity) {
    }

    @Override
    public void onActivityResumed(Activity activity) {
        if (isInBackground) {
            isInBackground = false;
            ApplicationSpecialistImpl.getInstance().onResumeApplication();
        }
    }

    @Override
    public void onActivityPaused(Activity activity) {
    }

    @Override
    public void onActivityStopped(Activity activity) {
    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle bundle) {
    }

    @Override
    public void onActivityDestroyed(Activity activity) {
    }

    @Override
    public void onConfigurationChanged(Configuration configuration) {
    }

    @Override
    public void onLowMemory() {
    }

    @Override
    public void onTrimMemory(int i) {
        if (!ApplicationSpecialistImpl.getInstance().isStoped()) {
            if (i == ComponentCallbacks2.TRIM_MEMORY_UI_HIDDEN) {
                isInBackground = true;
                ApplicationSpecialistImpl.getInstance().onBackgroundApplication();
            }
        }
    }

    public boolean isInBackground() {
        return isInBackground;
    }
}