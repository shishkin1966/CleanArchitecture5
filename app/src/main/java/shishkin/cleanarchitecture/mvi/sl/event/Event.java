package shishkin.cleanarchitecture.mvi.sl.event;

import shishkin.cleanarchitecture.mvi.sl.data.ExtError;

@SuppressWarnings("unused")
public interface Event {

    /**
     * Получить Id события
     *
     * @return id события
     */
    int getId();

    /**
     * Установить Id события
     *
     * @param id id события
     * @return событие
     */
    Event setId(int id);

    /**
     * Получить имя отправителя события
     *
     * @return отправитель события
     */
    String getSender();

    /**
     * Установить имя отправителя события
     *
     * @param sender отправитель события
     * @return событие
     */
    Event setSender(final String sender);

    /**
     * Флаг - имеется ли ошибка
     *
     * @return true - устанговлен флаг наличия ошибки
     */
    boolean hasError();

    /**
     * Получить ошибку
     *
     * @return ошибка
     */
    ExtError getError();

    /**
     * Установить ошибку события
     *
     * @param error ошибка
     * @return событие
     */
    Event setError(final ExtError error);

}
