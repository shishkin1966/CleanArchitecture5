package shishkin.cleanarchitecture.mvi.app.specialist.location;

import android.location.Location;


import shishkin.cleanarchitecture.mvi.sl.SpecialistSubscriber;

public interface LocationSubscriber extends SpecialistSubscriber {
    /**
     * Установить у подписчика текущее местоположение
     *
     * @param location текущий Location
     */
    void setLocation(Location location);
}
