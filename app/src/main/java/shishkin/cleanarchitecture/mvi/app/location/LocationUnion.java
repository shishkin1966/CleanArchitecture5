package shishkin.cleanarchitecture.mvi.app.location;

import android.location.Address;
import android.location.Location;


import java.util.List;


import shishkin.cleanarchitecture.mvi.sl.SmallUnion;

/**
 * Интерфейс объединения, предоставляющее сервис геолокации подписчикам.
 */
@SuppressWarnings("unused")
public interface LocationUnion extends SmallUnion<LocationSubscriber> {


    /**
     * запустить службу геолокации
     */
    void start();

    /**
     * остановить службу геолокации
     */
    void stop();

    /**
     * Получить текущее положение
     *
     * @return текущее положение
     */
    Location getLocation();

    /**
     * Получить список адресов по его месту
     *
     * @param location     location
     * @param countAddress кол-во адресов
     * @return список адресов
     */
    List<Address> getAddress(Location location, int countAddress);

    /**
     * Получить список адресов по его месту
     *
     * @param location location
     * @return список адресов
     */
    List<Address> getAddress(final Location location);

    /**
     * Признак - модуль получает данные от сервиса определения местоположения
     */
    boolean isGetLocation();

    /**
     * Объединение определения местоположения запущено
     */
    boolean isRuning();
}

