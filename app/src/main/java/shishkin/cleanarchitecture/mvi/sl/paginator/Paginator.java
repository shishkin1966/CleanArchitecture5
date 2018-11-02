package shishkin.cleanarchitecture.mvi.sl.paginator;

import java.util.List;


import shishkin.cleanarchitecture.mvi.sl.SpecialistSubscriber;
import shishkin.cleanarchitecture.mvi.sl.request.Request;
import shishkin.cleanarchitecture.mvi.sl.request.ResponseListener;

public interface Paginator extends ResponseListener, SpecialistSubscriber {

    /**
     * Опросить наличие данных
     */
    void hasData();

    /**
     * Сбросить - после сброса paginator готов к повторной выборке
     */
    void reset();

    /**
     * Получить запрос для выборки данныъ
     *
     * @param currentPosition - текущая позиция для выборки данных
     * @param currentPageSize - текущее количество выбираемых данных
     */
    Request getRequest(int currentPosition, int currentPageSize);

    /**
     * Получить количество строк для prefetch
     */
    int getPrefetchSize();

    /**
     * Установить слушателя выбираемых данных
     *
     * @param listener - имя слушателя
     */
    NetPaginator setListener(String listener);

    /**
     * Установить начальный размер выбираемых данных
     *
     * @param initialPageSize - размер первоначальной выборки данных
     */
    NetPaginator setInitialPageSize(int initialPageSize);

    /**
     * Установить размеры выбираемых данных
     *
     * @param pageSize - последовательнось размеров выборки данных
     */
    NetPaginator setPageSize(List<Integer> pageSize);

    /**
     * Остановить выборку данных
     */
    void stop();

}
