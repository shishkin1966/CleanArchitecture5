package shishkin.cleanarchitecture.mvi.sl.task;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.TimeUnit;


import shishkin.cleanarchitecture.mvi.sl.request.AbsRequest;

@SuppressWarnings("unused")
public class CommonExecutor extends AbsRequestExecutor {

    public static final String NAME = CommonExecutor.class.getName();
    private static int QUEUE_CAPACITY = 1024;
    private int mThreadCount = 8;
    private int mMaxThreadCount = 8;
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
        final BlockingQueue queue = new PriorityBlockingQueue<AbsRequest>(QUEUE_CAPACITY);
        mExecutor = new RequestThreadPoolExecutor(mThreadCount, mMaxThreadCount, mKeepAliveTime, mUnit, queue);
    }

    @Override
    protected RequestThreadPoolExecutor getExecutor() {
        return mExecutor;
    }
}
