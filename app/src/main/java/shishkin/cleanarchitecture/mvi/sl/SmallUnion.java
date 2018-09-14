package shishkin.cleanarchitecture.mvi.sl;


import java.lang.ref.WeakReference;
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
    void register(T subscriber);

    /**
     * Отключить подписчика
     *
     * @param subscriber подписчик
     */
    void unregister(T subscriber);

    /**
     * Получить список подписчиков
     *
     * @return список подписчиков
     */
    List<WeakReference<T>> getSubscribers();

    /**
     * Проверить наличие подписчиков
     *
     * @return true - подписчики есть
     */
    boolean hasSubscribers();

    /**
     * Получить подписчика по его имени
     *
     * @param name имя подписчика
     * @return подписчик
     */
    T getSubscriber(final String name);

    /**
     * Проверить подписчика
     *
     * @param name имя подписчика
     * @return результат проверки подписчика
     */
    Result<Boolean> validateExt(final String name);

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

    /**
     * Событие - остановка приложения
     */
    void onFinishApplication();

}
