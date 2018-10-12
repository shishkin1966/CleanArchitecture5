package shishkin.cleanarchitecture.mvi.app.idle;

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
}
