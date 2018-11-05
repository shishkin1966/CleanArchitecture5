package shishkin.cleanarchitecture.mvi.common;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.os.Process;


import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;


import shishkin.cleanarchitecture.mvi.common.utils.SafeUtils;
import shishkin.cleanarchitecture.mvi.sl.ErrorSpecialistImpl;

/**
 * Класс помещает действие, которое будет выполняться в другом потоке, чем ваш собственный.
 * Очередь сообщений будут автоматически прекращаться после определенного периода времени.
 */
public class AutoCompleteHandler<E> {

    /**
     * Callback interface you can use to handle posted events.
     */
    public interface OnHandleEventListener<E> {

        void onHandleEvent(final E event);

    }

    /**
     * Callback interface to manage message queue stopping.
     */
    public interface OnShutdownListener {

        void onShutdown(final AutoCompleteHandler handler);

    }

    private static final String NAME = AutoCompleteHandler.class.getName();

    private static final int EVENT_TOKEN = 0xD0D0F00D;
    private static final int SHUTDOWN_TOKEN = 0xDEADBEEF;

    private long mShutdownTimeout = TimeUnit.SECONDS.toMillis(30);
    private OnHandleEventListener<E> mOnHandleEventListener;
    private OnShutdownListener mOnShutdownListener;

    private final String mName;
    private AsyncHandler mHandler;
    private ReentrantLock mLock;

    /**
     * Constructs an AsyncAutoCompleteHandler.
     *
     * @param name The thread name that can be used to trace thread.
     */
    public AutoCompleteHandler(final String name) {
        mLock = new ReentrantLock();
        mName = name;
    }

    /**
     * Set shutdown timeout in milliseconds when messages queue will be stopped
     * after queue is empty.
     *
     * @param shutdownTimeout The timeout in milliseconds.
     */
    public void setShutdownTimeout(final long shutdownTimeout) {
        mLock.lock();
        try {
            mShutdownTimeout = shutdownTimeout;
        } finally {
            mLock.unlock();
        }
    }

    /**
     * Register callback to manage messages queue stopping.
     */
    public void setOnShutdownListener(final OnShutdownListener onShutdownListener) {
        mLock.lock();
        try {
            mOnShutdownListener = onShutdownListener;
        } finally {
            mLock.unlock();
        }
    }

    /**
     * Register callback to manage events.
     */
    public void setOnHandleEventListener(final OnHandleEventListener<E> onHandleEventListener) {
        mLock.lock();
        try {
            mOnHandleEventListener = onHandleEventListener;
        } finally {
            mLock.unlock();
        }
    }

    /**
     * Causes the event to be added to the message queue.
     * The event will be run on the worker thread.
     *
     * @param event The event that will be executed.
     */
    public void post(final E event) {
        post(event, false);
    }

    /**
     * Causes the event to be added to the message queue.
     * The event will be run on the worker thread.
     *
     * @param event          The event that will be executed.
     * @param cancelPrevious The flag to remove any pending events that are in the message queue.
     */
    public void post(final E event, final boolean cancelPrevious) {
        mLock.lock();
        try {
            if (mHandler == null) {
                final HandlerThread thread = new HandlerThread(mName, Process.THREAD_PRIORITY_BACKGROUND);
                thread.setPriority(Thread.MAX_PRIORITY);
                thread.start();
                mHandler = new AsyncHandler(thread.getLooper());
            }

            mHandler.removeMessages(SHUTDOWN_TOKEN);
            if (cancelPrevious) {
                mHandler.removeMessages(EVENT_TOKEN);
            }
            mHandler.obtainMessage(EVENT_TOKEN, event).sendToTarget();
        } finally {
            mLock.unlock();
        }
    }

    private void handleEvent(final E event) {
        if (mOnHandleEventListener != null) {
            mOnHandleEventListener.onHandleEvent(event);
        }
    }

    private void scheduleShutdown() {
        mLock.lock();
        try {
            if (mHandler != null) {
                final Message shutdownMsg = mHandler.obtainMessage(SHUTDOWN_TOKEN);
                mHandler.sendMessageDelayed(shutdownMsg, mShutdownTimeout);
            }
        } finally {
            mLock.unlock();
        }
    }

    public void shutdown() {
        mLock.lock();
        try {
            if (mHandler != null) {
                mHandler.getLooper().quit();
                mHandler = null;

                if (mOnShutdownListener != null) {
                    mOnShutdownListener.onShutdown(this);
                }
            }
        } finally {
            mLock.unlock();
        }
    }

    private class AsyncHandler extends Handler {

        public AsyncHandler(final Looper looper) {
            super(looper);
        }

        @Override
        public void handleMessage(final Message msg) {
            final int what = msg.what;
            switch (what) {
                case EVENT_TOKEN:
                    final E event = SafeUtils.cast(msg.obj);
                    try {
                        handleEvent(event);
                    } catch (final Exception e) {
                        ErrorSpecialistImpl.getInstance().onError(NAME, e);
                    }
                    scheduleShutdown();
                    break;

                case SHUTDOWN_TOKEN:
                    shutdown();
                    break;

                default:
                    super.handleMessage(msg);
                    break;
            }
        }
    }

}
