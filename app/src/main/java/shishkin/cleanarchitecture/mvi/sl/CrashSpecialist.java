package shishkin.cleanarchitecture.mvi.sl;

import androidx.annotation.NonNull;
import shishkin.cleanarchitecture.mvi.sl.data.Result;

/**
 * Специалист, протоколирующий Uncaught Exception
 */
public class CrashSpecialist implements Thread.UncaughtExceptionHandler, Specialist {

    public static final String NAME = CrashSpecialist.class.getName();
    private static Thread.UncaughtExceptionHandler mHandler = Thread.getDefaultUncaughtExceptionHandler();

    public CrashSpecialist() {
        Thread.setDefaultUncaughtExceptionHandler(this);
    }

    @Override
    public void uncaughtException(Thread thread, Throwable throwable) {
        android.util.Log.e(NAME, throwable.getMessage(), throwable);
        ErrorSpecialistImpl.getInstance().onError(NAME, throwable);
        if (mHandler != null) {
            mHandler.uncaughtException(thread, throwable);
        }
    }

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public String getPasport() {
        return getName();
    }

    @Override
    public boolean isPersistent() {
        return false;
    }

    @Override
    public void onUnRegister() {
    }

    @Override
    public void onRegister() {
    }

    @Override
    public void stop() {
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
    public int compareTo(@NonNull Object o) {
        return (Thread.UncaughtExceptionHandler.class.isInstance(o)) ? 0 : 1;
    }

}

