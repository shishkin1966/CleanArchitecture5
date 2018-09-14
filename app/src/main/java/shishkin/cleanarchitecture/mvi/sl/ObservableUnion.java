package shishkin.cleanarchitecture.mvi.sl;

import java.util.List;


import shishkin.cleanarchitecture.mvi.sl.observe.Observable;

/**
 * Интерфейс объединения Observable объектов
 */
public interface ObservableUnion extends SmallUnion<ObservableSubscriber> {

    /**
     * Зарегестрировать слушаемый объект
     *
     * @param observable слушаемый (IObservable) объект
     */
    void register(Observable observable);

    /**
     * Отменить регистрацию слушаемого объекта
     *
     * @param name имя слушаемого (IObservable) объекта
     */
    void unregister(String name);

    /**
     * Получить слушаемый объект
     *
     * @param name имя слушаемого (IObservable) объекта
     * @return слушаемый(IObservable) объект
     */
    Observable get(final String name);

    /**
     * Получить список слушаемых объектов
     *
     * @return список слушаемых(IObservable) объектов
     */
    List<Observable> getObservables();
}
