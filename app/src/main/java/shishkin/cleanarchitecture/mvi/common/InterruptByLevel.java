package shishkin.cleanarchitecture.mvi.common;

/**
 * Объект, реализующий прерывание по уровню
 */
public class InterruptByLevel {

    private InterruptListener listener;
    private boolean isInterrupt = false;

    public InterruptByLevel(InterruptListener listener) {
        this.listener = listener;
    }

    public void up() {
        if (!isInterrupt) {
            isInterrupt = true;
            if (listener != null) {
                listener.onInterrupt();
            }
        }
    }

    public void down() {
        isInterrupt = false;
    }

}
