package shishkin.cleanarchitecture.mvi.sl;

import shishkin.cleanarchitecture.mvi.sl.request.Request;
import shishkin.cleanarchitecture.mvi.sl.request.ResponseListener;

/**
 * Интерфейс специалиста выполнения запросов
 */
public interface RequestSpecialist extends Specialist {

    /**
     * Выполнить запрос параллельно
     *
     * @param request запрос
     */
    void request(Object sender, Request request);

    /**
     * Выполнить запрос последовательно
     *
     * @param request запрос
     */
    void requestSequentially(Object sender, Request request);

    /**
     * Отменить запросы слушателя
     *
     * @param listener имя слушателя
     */
    void cancelRequests(Object sender, String listener);
}
