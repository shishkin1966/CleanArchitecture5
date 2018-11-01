package shishkin.cleanarchitecture.mvi.sl.task;

import android.support.annotation.NonNull;


import java.util.concurrent.BlockingQueue;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.TimeUnit;


import shishkin.cleanarchitecture.mvi.sl.request.AbsRequest;
import shishkin.cleanarchitecture.mvi.sl.request.Request;

@SuppressWarnings("unused")
public abstract class AbsExecutor implements RequestExecutor {

    public static final String NAME = AbsExecutor.class.getName();
    private static int QUEUE_CAPACITY = 1024;
    private int mThreadCount = 8;
    private int mMaxThreadCount = 8;
    private long mKeepAliveTime = 10; // 10 мин
    private TimeUnit mUnit = TimeUnit.MINUTES;
    private RequestThreadPoolExecutor mExecutor;
    private static volatile AbsExecutor sInstance;

    protected abstract RequestThreadPoolExecutor getExecutor();

    @Override
    public void execute(final Request request) {
        mExecutor.addRequest(request);
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
        execute((Request) object);
    }

    @Override
    public void execute(@NonNull Runnable command) {
        if (command instanceof Request) {
            execute((Request) command);
        }
    }
}
