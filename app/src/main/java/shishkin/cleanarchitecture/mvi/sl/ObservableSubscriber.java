package shishkin.cleanarchitecture.mvi.sl;

import java.util.List;


import shishkin.cleanarchitecture.mvi.sl.state.Stateable;

public interface ObservableSubscriber<T> extends SpecialistSubscriber, Stateable {

    /**
     * Получить список Observable объектов
     *
     * @return список имен Observable объектов
     */
    List<String> getObservable();

    /**
     * Событие - объект изменен
     */
    void onChange(T object);

}
