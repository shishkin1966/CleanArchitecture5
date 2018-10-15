package shishkin.cleanarchitecture.mvi.sl.task;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.TimeUnit;


import shishkin.cleanarchitecture.mvi.sl.request.AbsRequest;
import shishkin.cleanarchitecture.mvi.sl.request.Request;
import shishkin.cleanarchitecture.mvi.sl.request.ResponseListener;

@SuppressWarnings("unused")
public class DbExecutor implements RequestExecutor {

    public static final String NAME = DbExecutor.class.getName();
    private static int QUEUE_CAPACITY = 1024;
    private int mThreadCount = 4;
    private int mMaxThreadCount = 4;
    private long mKeepAliveTime = 10; // 10 мин
    private TimeUnit mUnit = TimeUnit.MINUTES;
    private RequestThreadPoolExecutor mExecutor;
    private static volatile DbExecutor sInstance;

    public static DbExecutor getInstance() {
        if (sInstance == null) {
            synchronized (DbExecutor.class) {
                if (sInstance == null) {
                    sInstance = new DbExecutor();
                }
            }
        }
        return sInstance;
    }

    private DbExecutor() {
        final BlockingQueue queue = new PriorityBlockingQueue<AbsRequest>(QUEUE_CAPACITY);
        mExecutor = new RequestThreadPoolExecutor(mThreadCount, mMaxThreadCount, mKeepAliveTime, mUnit, queue);
    }

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
    }

    @Override
    public void cancelRequests(ResponseListener listener) {
        mExecutor.cancelRequests(listener);
    }

    @Override
    public void cancelRequests(ResponseListener listener, String taskName) {
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
}