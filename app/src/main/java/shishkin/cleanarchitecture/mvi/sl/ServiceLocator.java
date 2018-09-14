package shishkin.cleanarchitecture.mvi.sl;

/**
 * Итерфейс администратора(Service Locator)
 */
@SuppressWarnings("unused")
public interface ServiceLocator extends Subscriber {

    /**
     * Проверить существование модуля
     *
     * @param name имя модуля
     * @return true - модуль существует
     */
    boolean exists(final String name);

    /**
     * Получить модуль
     *
     * @param <C>  тип модуля
     * @param name имя модуля
     * @return модуль
     */
    <C> C get(String name);

    /**
     * Зарегистрировать модуль
     *
     * @param specialist модуль
     * @return флаг - операция завершена успешно
     */
    boolean register(Specialist specialist);

    /**
     * Зарегистрировать модуль
     *
     * @param name имя класса модуля
     * @return флаг - операция завершена успешно
     */
    boolean register(String name);

    /**
     * Отменить регистрацию модуля
     *
     * @param name имя модуля/объекта
     * @return флаг - операция завершена успешно
     */
    boolean unregister(String name);

    /**
     * Зарегистрировать подписчика модуля
     *
     * @param subscriber подписчик модуля
     * @return флаг - операция завершена успешно
     */
    boolean register(SpecialistSubscriber subscriber);

    /**
     * Отменить регистрацию подписчика модуля
     *
     * @param subscriber подписчик модуля
     * @return флаг - операция завершена успешно
     */
    boolean unregister(SpecialistSubscriber subscriber);

    /**
     * Установить подписчика текущим
     *
     * @param subscriber подписчик
     * @return флаг - операция завершена успешно
     */
    boolean setCurrentSubscriber(SpecialistSubscriber subscriber);

    /**
     * Событие - остановить service locator
     */
    void onFinish();

    /**
     * Событие - старт service locator
     */
    void onStart();

    /**
     * Получить фабрику модулей
     *
     * @return фабрика модулей
     */
    SpecialistFactory getSpecialistFactory();
}
