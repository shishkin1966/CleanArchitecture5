package shishkin.cleanarchitecture.mvi.sl.request;

/**
 * Created by Shishkin on 13.12.2017.
 */

public interface ResultRequest extends ResultMailRequest {

    /**
     * Получить слушателя запроса
     *
     * @return ResponseListener - слушатель запроса
     */
    ResponseListener getListener();
}
