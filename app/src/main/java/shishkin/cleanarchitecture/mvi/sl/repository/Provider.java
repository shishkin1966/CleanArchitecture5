package shishkin.cleanarchitecture.mvi.sl.repository;

import shishkin.cleanarchitecture.mvi.sl.Specialist;
import shishkin.cleanarchitecture.mvi.sl.request.Request;
import shishkin.cleanarchitecture.mvi.sl.request.ResponseListener;

/**
 * Created by Shishkin on 04.12.2017.
 */

@SuppressWarnings("unused")
public interface Provider extends Specialist {

    /**
     * Выполнить запрос
     *
     * @param request запрос
     */
    void request(Request request);

    /**
     * Прервать все запросы слушателя
     *
     * @param listener слушатель
     */
    void cancelRequests(ResponseListener listener);

}
