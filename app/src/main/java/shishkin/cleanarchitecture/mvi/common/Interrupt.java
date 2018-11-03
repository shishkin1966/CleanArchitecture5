package shishkin.cleanarchitecture.mvi.common;

public class Interrupt {

    private  InterruptListener listener;
    private boolean isInterrupt = false;

    public Interrupt(InterruptListener listener) {
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
