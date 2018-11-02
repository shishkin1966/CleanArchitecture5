package shishkin.cleanarchitecture.mvi.sl;

import android.Manifest;
import androidx.annotation.NonNull;
import android.widget.Toast;

import com.github.snowdream.android.util.FilePathGenerator;
import com.github.snowdream.android.util.Log;


import java.io.File;


import shishkin.cleanarchitecture.mvi.BuildConfig;
import shishkin.cleanarchitecture.mvi.common.utils.ApplicationUtils;
import shishkin.cleanarchitecture.mvi.common.utils.StringUtils;
import shishkin.cleanarchitecture.mvi.sl.data.ExtError;

/**
 * Интерфейс специалиста обработки ошибок
 */
public class ErrorSpecialistImpl extends AbsSpecialist implements ErrorSpecialist {

    public static final String NAME = ErrorSpecialist.class.getName();
    private static final long MAX_LOG_LENGTH = 2000000;//2Mb

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
        try {
            Log.setEnabled(true);
            Log.setLog2FileEnabled(true);
            String path;
            if (BuildConfig.DEBUG && ApplicationUtils.checkPermission(ApplicationSpecialistImpl.getInstance(), Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                path = ApplicationSpecialistImpl.getInstance().getExternalDataPath();
            } else {
                path = ApplicationSpecialistImpl.getInstance().getDataPath();
            }
            final File file = new File(path);
            if (!file.exists()) {
                file.mkdirs();
            }
            if (file.exists()) {
                Log.setFilePathGenerator(new FilePathGenerator.DefaultFilePathGenerator(path,
                        StringUtils.replace(BuildConfig.APPLICATION_ID, ".", "_"), ".log"));
                checkLogSize();
            } else {
                Log.setEnabled(false);
            }
        } catch (Exception e) {
            Log.setEnabled(false);
        }
    }

    @Override
    public String getPath() {
        return Log.getPath();
    }

    private void checkLogSize() {
        final String path = Log.getPath();

        try {
            final File file = new File(path);
            if (file.exists()) {
                if (file.length() == 0) {
                    Log.i(ApplicationUtils.getPhoneInfo());
                }

                if (file.length() > MAX_LOG_LENGTH) {
                    final String new_path = path + ".1";
                    final File new_file = new File(new_path);
                    if (new_file.exists()) {
                        new_file.delete();
                    }
                    file.renameTo(new_file);
                }
            }
        } catch (Exception e) {
            android.util.Log.e(NAME, e.getMessage());
        }
    }

    @Override
    public void onError(final String source, final Exception e) {
        Log.e(source, Log.getStackTraceString(e));
        ApplicationUtils.showToast(e.getMessage(), Toast.LENGTH_LONG, ApplicationUtils.MESSAGE_TYPE_ERROR);
    }

    @Override
    public void onError(final String source, final Throwable throwable) {
        Log.e(source, Log.getStackTraceString(throwable));
        ApplicationUtils.showToast(throwable.getMessage(), Toast.LENGTH_LONG, ApplicationUtils.MESSAGE_TYPE_ERROR);
    }

    @Override
    public void onError(final String source, final Exception e, final String displayMessage) {
        Log.e(source, Log.getStackTraceString(e));
        if (!StringUtils.isNullOrEmpty(displayMessage)) {
            ApplicationUtils.showToast(displayMessage, Toast.LENGTH_LONG, ApplicationUtils.MESSAGE_TYPE_ERROR);
        }
    }

    @Override
    public void onError(final String source, final String message, final boolean isDisplay) {
        if (!StringUtils.isNullOrEmpty(message)) {
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
