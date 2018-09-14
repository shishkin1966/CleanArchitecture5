package shishkin.cleanarchitecture.mvi.sl.request;

import shishkin.cleanarchitecture.mvi.sl.Validated;
import shishkin.cleanarchitecture.mvi.sl.data.Result;

/**
 * Created by Shishkin on 05.12.2017.
 */

public interface ResponseListener extends Validated {

    /**
     * Событие - пришел ответ с результатами запроса
     *
     * @param result - результат
     */
    void response(Result result);

}
