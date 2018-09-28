package shishkin.cleanarchitecture.mvi.sl;

import shishkin.cleanarchitecture.mvi.sl.presenter.Presenter;

/**
 * Интерфейс объединения презенторов
 */
@SuppressWarnings("unused")
public interface PresenterUnion extends Union<Presenter> {

    /**
     * Получить presenter
     *
     * @param name имя презентера
     * @return презентер
     */
    <C> C getPresenter(final String name);

}
