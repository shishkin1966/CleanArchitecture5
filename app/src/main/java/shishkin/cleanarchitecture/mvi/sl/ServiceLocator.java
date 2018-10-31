package shishkin.cleanarchitecture.mvi.sl;

import java.util.List;

/**
 * Итерфейс администратора(Service Locator)
 */
@SuppressWarnings("unused")
public interface ServiceLocator extends Subscriber {

    /**
     * Проверить существование специалиста
     *
     * @param name имя специалиста
     * @return true - специалист существует
     */
    boolean exists(final String name);

    /**
     * Получить специалиста
     *
     * @param <C>  тип специалиста
     * @param name имя специалиста
     * @return специалист
     */
    <C> C get(String name);

    /**
     * Зарегистрировать специалиста
     *
     * @param specialist специалист
     * @return флаг - операция завершена успешно
     */
    boolean register(Specialist specialist);

    /**
     * Зарегистрировать специалиста
     *
     * @param name имя класса специалиста
     * @return флаг - операция завершена успешно
     */
    boolean register(String name);

    /**
     * Отменить регистрацию специалиста
     *
     * @param name имя специалиста/объекта
     * @return флаг - операция завершена успешно
     */
    boolean unregister(String name);

    /**
     * Зарегистрировать подписчика специалиста
     *
     * @param subscriber подписчик специалиста
     * @return флаг - операция завершена успешно
     */
    boolean register(SpecialistSubscriber subscriber);

    /**
     * Отменить регистрацию подписчика специалиста
     *
     * @param subscriber подписчик специалиста
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
     * Событие - остановка service locator
     */
    void onStop();

    /**
     * Событие - старт service locator
     */
    void onStart();

    /**
     * Получить фабрику специалистов
     *
     * @return фабрика специалистов
     */
    SpecialistFactory getSpecialistFactory();

    /**
     * Получить список специалистов
     *
     * @return список специалистов
     */
    List<Specialist> getSpecialists();
}
