package shishkin.cleanarchitecture.mvi.sl.request;


import shishkin.cleanarchitecture.mvi.sl.Subscriber;

/**
 * Created by Shishkin on 09.04.2017.
 */

@SuppressWarnings("unused")
public interface Request extends Runnable, Subscriber, Comparable<Request> {

    /**
     * Получить ранг запроса
     *
     * @return ранг запроса
     */
    int getRank();

    /**
     * Установить ранг запроса
     *
     * @param rank ранг запроса
     */
    void setRank(int rank);

    /**
     * Установить флаг - запрос прерван
     */
    void setCanceled();

    /**
     * Проверить прерван ли запрос
     *
     * @return true - запрос прерван
     */
    boolean isCancelled();

    /**
     * Проверить работоспособность запроса
     *
     * @return true - запрос может обеспечивать свою функциональность
     */
    boolean validate();

    /**
     * Проверить является ли запрос уникальным
     *
     * @return true - при запуске все предыдущие такие же запросы будут прерваны
     */
    boolean isDistinct();
}
