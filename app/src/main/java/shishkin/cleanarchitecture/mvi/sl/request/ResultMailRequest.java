package shishkin.cleanarchitecture.mvi.sl.request;

import java.util.List;


import shishkin.cleanarchitecture.mvi.sl.data.ExtError;

/**
 * Created by Shishkin on 13.12.2017.
 */

public interface ResultMailRequest<T> extends Request {

    /**
     * Получить имя собственника запроса
     *
     * @return имя собственника запроса
     */
    String getOwnerName();

    /**
     * Получить список получателей запроса
     *
     * @return список получателей запроса
     */
    List<String> getCopyTo();

    /**
     * Установить список получателей запроса
     *
     * @param copyTo список получателей запроса
     */
    void setCopyTo(List<String> copyTo);

    /**
     * Получить результат запроса
     *
     * @return результат запроса
     */
    T getData();

    /**
     * Установить результат запроса
     *
     * @param data результат запроса
     */
    void setData(T data);

    /**
     * Получить ошибку запроса
     *
     * @return ошибка запроса
     */
    ExtError getError();

    /**
     * Установить ошибку запроса
     *
     * @param error ошибка запроса
     */
    void setError(ExtError error);

    /**
     * Разослать результаты запроса
     */
    void response();

}
