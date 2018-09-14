package shishkin.cleanarchitecture.mvi.sl.task;

import android.support.annotation.NonNull;


import java.lang.ref.WeakReference;
import java.util.Collections;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;


import shishkin.cleanarchitecture.mvi.common.utils.SafeUtils;
import shishkin.cleanarchitecture.mvi.sl.ErrorSpecialistImpl;
import shishkin.cleanarchitecture.mvi.sl.request.Request;
import shishkin.cleanarchitecture.mvi.sl.request.ResponseListener;
import shishkin.cleanarchitecture.mvi.sl.request.ResultRequest;


public class RequestThreadPoolExecutor extends ThreadPoolExecutor implements IExecutor {

    private Map<String, WeakReference<Request>> mRequests = Collections.synchronizedMap(new ConcurrentHashMap<String, WeakReference<Request>>());

    /**
     * @param corePoolSize    The size of the pool
     * @param maximumPoolSize The maximum size of the pool
     * @param keepAliveTime   The amount of time you wish to keep a single task alive
     * @param unit            The unit of time that the keep alive time represents
     * @param workQueue       The queue that holds your tasks
     * @see {@link ThreadPoolExecutor#ThreadPoolExecutor(int, int, long, TimeUnit, BlockingQueue)}
     */
    public RequestThreadPoolExecutor(final int corePoolSize, final int maximumPoolSize, final long keepAliveTime, final TimeUnit unit, final BlockingQueue<Runnable> workQueue) {
        super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, new JobThreadFactory());
    }

    public void addRequest(Request request) {
        if (request == null) return;

        checkNullRequest();

        if (request.isDistinct()) {
            if (mRequests.containsKey(request.getName())) {
                final Request oldRequest = mRequests.get(request.getName()).get();
                if (oldRequest != null) {
                    oldRequest.setCanceled();
                }
            }
        }
        mRequests.put(request.getName(), new WeakReference<>(request));

        execute(request);
    }

    @Override
    public void afterExecute(Runnable r, Throwable t) {
        super.afterExecute(r, t);

        if (t != null) {
            ErrorSpecialistImpl.getInstance().onError(getClass().getName(), t);
        }
    }

    public void clear() {
        mRequests.clear();
    }

    private void checkNullRequest() {
        for (Map.Entry<String, WeakReference<Request>> entry : mRequests.entrySet()) {
            if (entry.getValue() == null || entry.getValue().get() == null) {
                mRequests.remove(entry.getKey());
            }
        }
    }

    public void cancelRequests(ResponseListener listener) {
        if (listener == null) return;

        checkNullRequest();

        for (WeakReference<Request> ref : mRequests.values()) {
            if (ResultRequest.class.isInstance(ref.get())) {
                final ResultRequest request = SafeUtils.cast(ref.get());
                if (request != null && request.validate() && request.getListener().equals(listener)) {
                    request.setCanceled();
                }
            }
        }
    }

    public void shutdown() {
        clear();
        shutdownNow();
    }

    @Override
    public void processing(Object sender, Object object) {
        addRequest((Request) object);
    }

    private static class JobThreadFactory implements ThreadFactory {
        private int counter = 0;

        @Override
        public Thread newThread(@NonNull Runnable runnable) {
            final Thread thread = new Thread(runnable, "Thread_" + counter++);
            thread.setPriority(Thread.NORM_PRIORITY);
            return thread;
        }
    }

}