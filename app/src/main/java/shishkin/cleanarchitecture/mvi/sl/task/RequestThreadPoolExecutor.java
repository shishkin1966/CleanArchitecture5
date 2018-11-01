package shishkin.cleanarchitecture.mvi.sl.task;

import android.support.annotation.NonNull;


import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;


import shishkin.cleanarchitecture.mvi.common.utils.StringUtils;
import shishkin.cleanarchitecture.mvi.sl.ErrorSpecialistImpl;
import shishkin.cleanarchitecture.mvi.sl.RefSecretaryImpl;
import shishkin.cleanarchitecture.mvi.sl.Secretary;
import shishkin.cleanarchitecture.mvi.sl.request.Request;
import shishkin.cleanarchitecture.mvi.sl.request.ResultMailRequest;


public class RequestThreadPoolExecutor extends ThreadPoolExecutor implements IExecutor {

    public static int ACTION_NOTHING = -1;
    public static int ACTION_DELETE = 0;
    public static int ACTION_IGNORE = 1;

    private Secretary<Request> mRequests = new RefSecretaryImpl<>();

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

        int action = ACTION_NOTHING;
        if (request.isDistinct()) {
            if (mRequests.containsKey(request.getName())) {
                final Request oldRequest = mRequests.get(request.getName());
                if (oldRequest != null) {
                    action = request.getAction(oldRequest);
                    if (action == ACTION_DELETE) {
                        oldRequest.setCanceled();
                    }
                }
            }
        }
        if (action != ACTION_IGNORE) {
            mRequests.put(request.getName(), request);
            execute(request);
        }
    }

    @Override
    public void afterExecute(Runnable r, Throwable t) {
        super.afterExecute(r, t);

        if (t != null) {
            ErrorSpecialistImpl.getInstance().onError(getClass().getName(), t);
        }
    }

    @Override
    protected void beforeExecute(Thread t, Runnable r) {
        int priority = Thread.NORM_PRIORITY;
        if (r instanceof Request) {
            final int rank = ((Request) r).getRank();
            if (rank < 4) {
                priority = Thread.MIN_PRIORITY;
            } else if (rank > 6) {
                priority = Thread.MAX_PRIORITY;
            }
        }
        t.setPriority(priority);
        super.beforeExecute(t, r);
    }

    @Override
    public void clear() {
        for (Request request : mRequests.values()) {
            request.setCanceled();
        }

        mRequests.clear();
    }

    @Override
    public void cancelRequests(String listener) {
        if (listener == null) return;

        for (Request request : mRequests.values()) {
            if (ResultMailRequest.class.isInstance(request)) {
                if (request.validate() && ((ResultMailRequest) request).getOwnerName().equals(listener)) {
                    request.setCanceled();
                }
            }
        }
    }

    @Override
    public void cancelRequests(String listener, String taskName) {
        if (listener == null || StringUtils.isNullOrEmpty(taskName)) return;

        for (Request request : mRequests.values()) {
            if (ResultMailRequest.class.isInstance(request)) {
                if (request.validate() && ((ResultMailRequest) request).getOwnerName().equals(listener) && taskName.equalsIgnoreCase(request.getName())) {
                    request.setCanceled();
                }
            }
        }
    }

    @Override
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
            return new Thread(runnable, "Thread_" + counter++);
        }
    }

}