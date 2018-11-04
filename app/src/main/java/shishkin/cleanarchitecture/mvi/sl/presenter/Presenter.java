package shishkin.cleanarchitecture.mvi.sl.presenter;

import shishkin.cleanarchitecture.mvi.sl.MessagerSubscriber;
import shishkin.cleanarchitecture.mvi.sl.SpecialistSubscriber;
import shishkin.cleanarchitecture.mvi.sl.state.ViewStateListener;

public interface Presenter<M> extends ViewStateListener, SpecialistSubscriber, MessagerSubscriber {

    /**
     * Установить модель презентера
     *
     * @param model the model
     */
    void setModel(M model);

    /**
     * Получить модель презентера
     *
     * @return the model
     */
    M getModel();

    /**
     * Флаг - регистрировать презентер в объединении презентеров
     *
     * @return true - регистрировать (презентер - глобальный)
     */
    boolean isRegister();

    /**
     * Событие - презентер перешел в состояние STATE_RESUME
     */
    void onStart();

    /**
     * Событие - презентер перешел в состояние STATE_DESTROY
     */
    void onStop();

    /**
     * Событие - обновлена модель
     */
    void onModelUpdated();

}
