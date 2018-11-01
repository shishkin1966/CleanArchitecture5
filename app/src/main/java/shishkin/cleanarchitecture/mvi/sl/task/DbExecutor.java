package shishkin.cleanarchitecture.mvi.sl.task;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.TimeUnit;


import shishkin.cleanarchitecture.mvi.sl.request.AbsRequest;

@SuppressWarnings("unused")
public class DbExecutor extends AbsRequestExecutor {

    public static final String NAME = DbExecutor.class.getName();
    private static int QUEUE_CAPACITY = 1024;
    private int mThreadCount = 4;
    private int mMaxThreadCount = 8;
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
    protected RequestThreadPoolExecutor getExecutor() {
        return mExecutor;
    }
}
