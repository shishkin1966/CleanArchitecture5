package shishkin.cleanarchitecture.mvi.sl.task;

import androidx.annotation.NonNull;


import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;


import shishkin.cleanarchitecture.mvi.sl.request.Request;

public class RunnableExecutor extends AbsRequestExecutor {

    public static final String NAME = RunnableExecutor.class.getName();
    private static int QUEUE_CAPACITY = 1024;
    private int mThreadCount = 4;
    private int mMaxThreadCount = 4;
    private long mKeepAliveTime = 10; // 10 мин
    private TimeUnit mUnit = TimeUnit.MINUTES;
    private RequestThreadPoolExecutor mExecutor;
    private static volatile RunnableExecutor sInstance;

    public static RunnableExecutor getInstance() {
        if (sInstance == null) {
            synchronized (RunnableExecutor.class) {
                if (sInstance == null) {
                    sInstance = new RunnableExecutor();
                }
            }
        }
        return sInstance;
    }

    private RunnableExecutor() {
        final BlockingQueue queue = new ArrayBlockingQueue(QUEUE_CAPACITY);
        mExecutor = new RequestThreadPoolExecutor(mThreadCount, mMaxThreadCount, mKeepAliveTime, mUnit, queue);
    }

    @Override
    protected RequestThreadPoolExecutor getExecutor() {
        return mExecutor;
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
