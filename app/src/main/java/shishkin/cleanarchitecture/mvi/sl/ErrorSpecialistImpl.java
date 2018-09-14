package shishkin.cleanarchitecture.mvi.sl;

import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;


import shishkin.cleanarchitecture.mvi.BuildConfig;
import shishkin.cleanarchitecture.mvi.common.utils.ApplicationUtils;
import shishkin.cleanarchitecture.mvi.common.utils.StringUtils;
import shishkin.cleanarchitecture.mvi.sl.data.ExtError;

/**
 * Модуль обработки ошибок
 */
public class ErrorSpecialistImpl extends AbsSpecialist implements ErrorSpecialist {
    public static final String NAME = ErrorSpecialist.class.getName();

    private static volatile ErrorSpecialist sInstance;

    public static ErrorSpecialist getInstance() {
        if (sInstance == null) {
            synchronized (ErrorSpecialist.class) {
                if (sInstance == null) {
                    sInstance = new ErrorSpecialistImpl();
                }
            }
        }
        return sInstance;
    }

    private ErrorSpecialistImpl() {
    }

    @Override
    public void onError(final String source, final Exception e) {
        if (BuildConfig.DEBUG) {
            ApplicationUtils.showToast(e.getMessage(), Toast.LENGTH_LONG, ApplicationUtils.MESSAGE_TYPE_ERROR);
        }
        Log.e(source, e.getMessage());
    }

    @Override
    public void onError(final String source, final Throwable throwable) {
        if (BuildConfig.DEBUG) {
            ApplicationUtils.showToast(throwable.getMessage(), Toast.LENGTH_LONG, ApplicationUtils.MESSAGE_TYPE_ERROR);
        }
        Log.e(source, throwable.getMessage());
    }

    @Override
    public void onError(final String source, final Exception e, final String displayMessage) {
        onError(source, e);

        if (!StringUtils.isNullOrEmpty(displayMessage)) {
            ApplicationUtils.showToast(displayMessage, Toast.LENGTH_LONG, ApplicationUtils.MESSAGE_TYPE_ERROR);
        }
    }

    @Override
    public void onError(final String source, final String message, final boolean isDisplay) {
        if (!StringUtils.isNullOrEmpty(message)) {
            if (BuildConfig.DEBUG && !isDisplay) {
                ApplicationUtils.showToast(message, Toast.LENGTH_LONG, ApplicationUtils.MESSAGE_TYPE_ERROR);
            }
            Log.e(source, message);
            if (isDisplay) {
                ApplicationUtils.showToast(message, Toast.LENGTH_LONG, ApplicationUtils.MESSAGE_TYPE_ERROR);
            }
        }
    }

    @Override
    public void onError(final ExtError extError) {
        if (extError != null && extError.hasError()) {
            ApplicationUtils.showToast(extError.getErrorText(), Toast.LENGTH_LONG, ApplicationUtils.MESSAGE_TYPE_ERROR);
        }
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
    public int compareTo(@NonNull Object o) {
        return (ErrorSpecialist.class.isInstance(o)) ? 0 : 1;
    }

}
