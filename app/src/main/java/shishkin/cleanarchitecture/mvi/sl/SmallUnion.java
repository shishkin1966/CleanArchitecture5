package shishkin.cleanarchitecture.mvi.sl;


import java.util.List;


import shishkin.cleanarchitecture.mvi.sl.data.Result;

/**
 * Интерфейс малого объединения подписчиков
 */
@SuppressWarnings("unused")
public interface SmallUnion<T> extends Specialist {

    /**
     * Зарегестрировать подписчика
     *
     * @param subscriber подписчик
     */
    boolean register(T subscriber);

    /**
     * Отключить подписчика
     *
     * @param subscriber подписчик
     */
    boolean unregister(T subscriber);

    /**
     * Получить список подписчиков
     *
     * @return список подписчиков
     */
    List<T> getSubscribers();

    /**
     * Получить список валидных подписчиков
     *
     * @return список подписчиков
     */
    List<T> getValidatedSubscribers();

    /**
     * Получить список готовых Stateable подписчиков
     *
     * @return список подписчиков
     */
    List<T> getReadySubscribers();

    /**
     * Проверить наличие подписчиков
     *
     * @return true - подписчики есть
     */
    boolean hasSubscribers();

    /**
     * Проверить наличие подписчика
     *
     * @param name имя подписчика
     * @return true - подписчик есть
     */
    boolean hasSubscriber(String name);

    /**
     * Получить подписчика по его имени
     *
     * @param name имя подписчика
     * @return подписчик
     */
    T getSubscriber(String name);

    /**
     * Проверить подписчика
     *
     * @param name имя подписчика
     * @return результат проверки подписчика
     */
    Result<Boolean> validateExt(String name);

    /**
     * Проверить подписчика
     *
     * @param name имя подписчика
     * @return результат проверки подписчика
     */
    boolean validate(final String name);

    /**
     * Событие - появился первый подписчик
     */
    void onRegisterFirstSubscriber();

    /**
     * Событие - отписан последний подписчик
     */
    void onUnRegisterLastSubscriber();

    /**
     * Событие - добавлен подписчик
     *
     * @param subscriber подписчик
     */
    void onAddSubscriber(final T subscriber);

}
