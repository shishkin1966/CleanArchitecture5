package shishkin.cleanarchitecture.mvi.sl.request;

/**
 * Created by Shishkin on 13.12.2017.
 */

public interface ResultRequest<T> extends ResultMailRequest<T> {

    /**
     * Получить собственника запроса
     *
     * @return ResponseListener - собственник запроса
     */
    ResponseListener getOwner();
}
