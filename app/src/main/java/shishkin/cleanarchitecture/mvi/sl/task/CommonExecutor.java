package shishkin.cleanarchitecture.mvi.sl.task;

import android.support.annotation.NonNull;


import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;


import shishkin.cleanarchitecture.mvi.sl.request.Request;

public class CommonExecutor implements RequestExecutor, Executor {

    public static final String NAME = CommonExecutor.class.getName();
    private static int QUEUE_CAPACITY = 1024;
    private int mThreadCount = 4;
    private int mMaxThreadCount = 4;
    private long mKeepAliveTime = 10; // 10 мин
    private TimeUnit mUnit = TimeUnit.MINUTES;
    private RequestThreadPoolExecutor mExecutor;
    private static volatile CommonExecutor sInstance;

    public static CommonExecutor getInstance() {
        if (sInstance == null) {
            synchronized (CommonExecutor.class) {
                if (sInstance == null) {
                    sInstance = new CommonExecutor();
                }
            }
        }
        return sInstance;
    }

    private CommonExecutor() {
        final BlockingQueue queue = new ArrayBlockingQueue(QUEUE_CAPACITY);
        mExecutor = new RequestThreadPoolExecutor(mThreadCount, mMaxThreadCount, mKeepAliveTime, mUnit, queue);
    }

    @Override
    public void shutdown() {
        mExecutor.shutdown();
    }

    @Override
    public void clear() {
        mExecutor.clear();
    }

    @Override
    public void cancelRequests(String listener) {
        mExecutor.cancelRequests(listener);
    }

    @Override
    public void cancelRequests(String listener, String taskName) {
        mExecutor.cancelRequests(listener, taskName);
    }

    @Override
    public boolean isShutdown() {
        return mExecutor.isShutdown();
    }

    @Override
    public void processing(Object sender, Object object) {
        execute((Runnable) object);
    }

    @Override
    public void execute(@NonNull Request command) {
        mExecutor.execute(command);
    }

    @Override
    public void execute(@NonNull Runnable command) {
        mExecutor.execute(command);
    }
}
