package shishkin.cleanarchitecture.mvi.sl.model;

import shishkin.cleanarchitecture.mvi.sl.Validated;
import shishkin.cleanarchitecture.mvi.sl.presenter.Presenter;
import shishkin.cleanarchitecture.mvi.sl.state.Stateable;
import shishkin.cleanarchitecture.mvi.sl.state.ViewStateListener;

@SuppressWarnings("unused")
public interface Model extends Validated, ViewStateListener {

    /**
     * Получить View объект модели
     *
     * @return View объект модели
     */
    <V> V getView();

    /**
     * Установить View объект модели
     *
     * @param view View объект модели
     */
    void setView(ModelView view);

    /**
     * Добавить слушателя к модели
     *
     * @param stateable stateable объект
     */
    void addStateObserver(final Stateable stateable);

    /**
     * Установить presenter модели
     *
     * @param presenter presenter модели
     */
    void setPresenter(Presenter presenter);

    /**
     * Получить presenter модели
     *
     * @return презентер модели
     */
    <C> C getPresenter();

    /**
     * Установить interactor модели
     *
     * @param interactor interactor модели
     */
    void setInteractor(ModelInteractor interactor);

    /**
     * Получить interactor модели
     *
     * @return interactor модели
     */
    <C> C getInteractor();

    /**
     * Установить router модели
     *
     * @param router router модели
     */
    void setRouter(ModelRouter router);

    /**
     * Получить router модели
     *
     * @return router модели
     */
    <C> C getRouter();
}
