package shishkin.cleanarchitecture.mvi.common;

import android.os.Handler;

/**
 * Класс, устраняющий дребезг (частое повторение) события
 */
public class Debounce implements Runnable {

    private long mDelay = 5000; //5 sec
    private int mSkip = 0;
    private Handler mHandler = null;

    /**
     * Конструктор
     *
     * @param delay задержка, после которой запустится действие
     */
    public Debounce(final long delay) {
        this(delay, 0);
    }

    /**
     * Конструктор
     *
     * @param delay задержка, после которой запустится действие
     * @param skip  количество событий, которое будет пропущено перед запуском задержки
     */
    public Debounce(final long delay, final int skip) {
        mHandler = new Handler();
        mDelay = delay;
        mSkip = skip;
    }

    /**
     * Событие
     */
    public void onEvent() {
        if (mSkip >= 0) {
            mSkip--;
        }

        if (mSkip < 0) {
            mHandler.removeCallbacks(this);
            mHandler.postDelayed(this, mDelay);
        }
    }

    @Override
    public void run() {
    }

    /**
     * остановить объект
     */
    public void finish() {
        if (mHandler != null) {
            mHandler.removeCallbacks(this);
        }
    }

}
