package shishkin.cleanarchitecture.mvi.sl.state;

/**
 * Интерфейс слушателя View объекта, имеющего состояния
 */
@SuppressWarnings("unused")
public interface ViewStateListener extends Stateable {
    /**
     * Событие - view на этапе создания
     */
    void onCreateView();

    /**
     * Событие - view готово к использованию
     */
    void onReadyView();

    /**
     * Событие - view становиться видимым на экране
     */
    void onResumeView();

    /**
     * Событие - view уходит в фон
     */
    void onPauseView();

    /**
     * Событие - уничтожение view
     */
    void onDestroyView();

    /**
     * Событие - activity создана
     */
    void onActivityCreated();
}
