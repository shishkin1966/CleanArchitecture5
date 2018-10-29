package shishkin.cleanarchitecture.mvi.sl.task;

import android.os.Handler;
import android.os.Looper;


import java.util.concurrent.Executor;

public class MainThreadExecutor implements Executor {
    private final Handler mHandler = new Handler(Looper.getMainLooper());

    @Override
    public void execute(Runnable command) {
        mHandler.post(command);
    }
}