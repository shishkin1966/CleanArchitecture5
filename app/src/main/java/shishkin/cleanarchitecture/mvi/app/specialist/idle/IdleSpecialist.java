package shishkin.cleanarchitecture.mvi.app.specialist.idle;

import shishkin.cleanarchitecture.mvi.sl.Specialist;

/**
 * Интерфейс специалиста отвечающего за бездействие пользователя
 */
public interface IdleSpecialist extends Specialist {


    /**
     * пользователь осуществил действие
     */
    void onUserInteraction();

    /**
     * установить таймаут специалиста
     *
     * @param timeout таймоут
     */
    void setTimeout(final long timeout);

    /**
     * получить время таймаута
     */
    long getTimeout();

    /**
     * получить время последнего поьзовательского действия
     */
    long getCurrentTime();
}
