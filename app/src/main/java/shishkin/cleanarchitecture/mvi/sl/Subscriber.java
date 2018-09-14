package shishkin.cleanarchitecture.mvi.sl;

/**
 * Интерфейс объекта - подписчика.
 */
public interface Subscriber {

    /**
     * Получить имя подписчика
     *
     * @return имя подписчика
     */
    String getName();

}