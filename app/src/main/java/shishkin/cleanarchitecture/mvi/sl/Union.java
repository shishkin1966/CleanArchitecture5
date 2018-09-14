package shishkin.cleanarchitecture.mvi.sl;

/**
 * Интерфейс объединения подписчиков
 *
 * @param <T> the type parameter
 */
@SuppressWarnings("unused")
public interface Union<T> extends SmallUnion<T> {

    /**
     * Получить текущего подписчика (подписчик, у которого состояние = RESUME)
     *
     * @return текущий подписчик
     */
    T getCurrentSubscriber();

    /**
     * Установить текущего подписчика
     *
     * @param subscriber подписчик
     */
    void setCurrentSubscriber(T subscriber);

}
