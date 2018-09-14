package shishkin.cleanarchitecture.mvi.sl.state;

import java.lang.ref.WeakReference;


/**
 * Объект, отвечающий за текущее состояние внешнего View
 */
public class ViewStateObserver implements Stateable {
    public static final int STATE_CREATE = 0;
    public static final int STATE_READY = 1;
    public static final int STATE_DESTROY = 2;
    public static final int STATE_PAUSE = 3;
    public static final int STATE_RESUME = 4;
    public static final int STATE_ACTIVITY_CREATED = 5;

    private int mState = STATE_CREATE;
    private WeakReference<ViewStateListener> mListener;

    public ViewStateObserver(final ViewStateListener listener) {
        if (listener != null) {
            mListener = new WeakReference<>(listener);
        }
        setState(STATE_CREATE);
    }

    /**
     * Получить состояние объекта
     *
     * @return состояние объекта
     */
    @Override
    public int getState() {
        return mState;
    }

    /**
     * Установить состояние объекта
     *
     * @param state состояние объекта
     */
    @Override
    public void setState(final int state) {
        mState = state;
        switch (mState) {
            case STATE_CREATE:
                onCreateView();
                break;

            case STATE_READY:
                onViewCreated();
                break;

            case STATE_DESTROY:
                onDestroyView();
                break;

            case STATE_PAUSE:
                onPauseView();
                break;

            case STATE_RESUME:
                onResumeView();
                break;

            case STATE_ACTIVITY_CREATED:
                onActivityCreated();
                break;

            default:
                break;

        }
    }

    private void onCreateView() {
        if (mListener != null && mListener.get() != null) {
            mListener.get().onCreateView();
        }
    }

    private void onViewCreated() {
        if (mListener != null && mListener.get() != null) {
            mListener.get().onReadyView();
        }
    }

    private void onResumeView() {
        if (mListener != null && mListener.get() != null) {
            mListener.get().onResumeView();
        }
    }

    private void onPauseView() {
        if (mListener != null && mListener.get() != null) {
            mListener.get().onPauseView();
        }
    }

    private void onDestroyView() {
        if (mListener != null && mListener.get() != null) {
            mListener.get().onDestroyView();
        }
    }

    private void onActivityCreated() {
        if (mListener != null && mListener.get() != null) {
            mListener.get().onActivityCreated();
        }
    }

}
