package shishkin.cleanarchitecture.mvi.sl.task;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.TimeUnit;


import shishkin.cleanarchitecture.mvi.sl.request.AbsRequest;
import shishkin.cleanarchitecture.mvi.sl.request.Request;
import shishkin.cleanarchitecture.mvi.sl.request.ResponseListener;

public class CommonThreadPoolExecutor implements IExecutor {

    public static final String NAME = CommonThreadPoolExecutor.class.getName();
    private static int QUEUE_CAPACITY = 1024;
    private int mThreadCount = 4;
    private int mMaxThreadCount = 4;
    private long mKeepAliveTime = 10; // 10 мин
    private TimeUnit mUnit = TimeUnit.MINUTES;
    private RequestThreadPoolExecutor mExecutor;
    private static volatile CommonThreadPoolExecutor sInstance;

    public static CommonThreadPoolExecutor getInstance() {
        if (sInstance == null) {
            synchronized (CommonThreadPoolExecutor.class) {
                if (sInstance == null) {
                    sInstance = new CommonThreadPoolExecutor();
                }
            }
        }
        return sInstance;
    }

    private CommonThreadPoolExecutor() {
        final BlockingQueue queue = new PriorityBlockingQueue<AbsRequest>(QUEUE_CAPACITY);
        mExecutor = new RequestThreadPoolExecutor(mThreadCount, mMaxThreadCount, mKeepAliveTime, mUnit, queue);
    }

    public void execute(final Request request) {
        mExecutor.addRequest(request);
    }

    public void shutdown() {
        mExecutor.shutdown();
    }

    @Override
    public void clear() {
    }

    public void cancelRequests(ResponseListener listener) {
        mExecutor.cancelRequests(listener);
    }

    public boolean isShutdown() {
        return mExecutor.isShutdown();
    }

    @Override
    public void processing(Object sender, Object object) {
        execute((Request) object);
    }
}
