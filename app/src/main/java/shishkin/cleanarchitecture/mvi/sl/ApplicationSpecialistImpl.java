package shishkin.cleanarchitecture.mvi.sl;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.os.Environment;


import java.io.File;


import androidx.annotation.NonNull;
import androidx.multidex.MultiDex;
import androidx.multidex.MultiDexApplication;
import shishkin.cleanarchitecture.mvi.BuildConfig;
import shishkin.cleanarchitecture.mvi.sl.data.Result;


/**
 * Специалист приложения
 */
@SuppressWarnings("unused")
public abstract class ApplicationSpecialistImpl extends MultiDexApplication implements ApplicationSpecialist {

    public static final String NAME = ApplicationSpecialistImpl.class.getName();
    private static volatile ApplicationSpecialistImpl sInstance;
    private static volatile ApplicationLifecycleHandler sHandler;
    private boolean isStoped = false;
    private boolean isScreenOff = false;

    @Override
    public void onCreate() {
        super.onCreate();

        start();
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    public static ApplicationSpecialistImpl getInstance() {
        return sInstance;
    }

    @Override
    public void onApplicationUpdated(final int oldVersion, final int newVersion) {
    }

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public boolean isPersistent() {
        return true;
    }

    @Override
    public void onUnRegister() {
    }

    @Override
    public Result<Boolean> validateExt() {
        return new Result<>(true);
    }

    @Override
    public boolean validate() {
        return validateExt().getData();
    }

    @Override
    public boolean isInBackground() {
        return sHandler.isInBackground();
    }

    @Override
    public int getVersion() {
        try {
            final PackageInfo pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
            return pInfo.versionCode;
        } catch (Exception e) {
            return 0;
        }
    }

    private void start() {
        if (sInstance != null) {
            return;
        }

        sInstance = this;

        onStart();
    }

    @Override
    public void onStart() {

        SL.instantiate();

        sHandler = new ApplicationLifecycleHandler();
        registerActivityLifecycleCallbacks(sHandler);
        registerComponentCallbacks(sHandler);
    }

    @Override
    public void stop() {
        isStoped = true;

        onStop();

        SL.getInstance().onStop();
    }

    @Override
    public void onStop() {
    }

    @Override
    public boolean isStoped() {
        return isStoped;
    }

    @Override
    public void onBackgroundApplication() {
    }

    @Override
    public void onResumeApplication() {
    }

    @Override
    public boolean isKillOnFinish() {
        return false;
    }

    @Override
    public int compareTo(@NonNull Object o) {
        return (ApplicationSpecialist.class.isInstance(o)) ? 0 : 1;
    }


    @Override
    public void onRegister() {
    }

    @Override
    public String getDataPath() {
        return getFilesDir().getAbsolutePath() + File.separator;
    }

    @Override
    public String getExternalDataPath() {
        return Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + BuildConfig.APPLICATION_ID + File.separator;
    }

    @Override
    public String getPasport() {
        return getName();
    }
}


