package shishkin.cleanarchitecture.mvi.sl;

/**
 * Интерфейс специалиста - объекта предоставлющий сервис
 */
@SuppressWarnings("unused")
public interface Specialist extends Subscriber, Validated, Comparable {

    /**
     * Получить тип специалиста
     *
     * @return true - не будет удаляться администратором
     */
    boolean isPersistent();

    /**
     * Событие - отключить регистрацию
     */
    void onUnRegister();

    /**
     * Событие - регистрация
     */
    void onRegister();

    /**
     * Остановитить работу специалиста
     */
    void stop();

}
