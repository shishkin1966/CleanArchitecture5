package shishkin.cleanarchitecture.mvi.sl.observe;

import java.util.List;


import shishkin.cleanarchitecture.mvi.sl.ObservableSubscriber;
import shishkin.cleanarchitecture.mvi.sl.Subscriber;

/**
 * Created by Shishkin on 15.12.2017.
 */

public interface Observable<T, K extends ObservableSubscriber> extends Subscriber {

    /**
     * Добавить слушателя к слушаемому объекту
     *
     * @param subscriber слушатель
     */
    void addObserver(K subscriber);

    /**
     * Удалить слушателя у слушаемого объекта
     *
     * @param subscriber слушатель
     */
    void removeObserver(K subscriber);

    /**
     * Зарегестрировать слушаемый объект. Вызывается при появлении
     * первого слушателя
     */
    void register();

    /**
     * Отменить регистрацию слушаемого объекта. Вызывается при удалении
     * последнего слушателя
     */
    void unregister();

    /**
     * Событие - в слушаемом объекте произошли изменения
     *
     * @param object объект изменения
     */
    void onChange(T object);

    /**
     * Получить список слушателей
     *
     * @return список слушателей
     */
    <K extends ObservableSubscriber> List<K> getObserver();

}
