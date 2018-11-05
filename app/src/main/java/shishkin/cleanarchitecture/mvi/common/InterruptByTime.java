package shishkin.cleanarchitecture.mvi.common;

import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 * Объект, реализующий прерывание по уровню
 */
public class InterruptByTime {

    private static long DELAY = TimeUnit.SECONDS.toMillis(5);
    private InterruptListener listener;
    private Integer cnt = 0;
    private boolean isInterrupt = false;
    private AutoCompleteHandler<Boolean> mServiceHandler;

    public InterruptByTime(InterruptListener listener, long delay) {
        this.listener = listener;

        final Random r = new Random();
        final int i = r.nextInt();
        mServiceHandler = new AutoCompleteHandler<>("InterruptByTime [" + i + "]");
        ;
        mServiceHandler.setOnShutdownListener(this::onShutdown);
        if (delay > 0) {
            mServiceHandler.setShutdownTimeout(delay);
        } else {
            mServiceHandler.setShutdownTimeout(DELAY);
        }
    }

    public void up() {
        if (!isInterrupt) {
            mServiceHandler.post(true);
            isInterrupt = true;
            cnt = 0;
            if (listener != null) {
                listener.onInterrupt();
            }
        } else {
            cnt = 1;
        }
    }

    public void down() {
        isInterrupt = false;
        if (cnt > 0) {
            up();
        }
    }

    private void onShutdown(AutoCompleteHandler handler) {
        down();
    }
}
