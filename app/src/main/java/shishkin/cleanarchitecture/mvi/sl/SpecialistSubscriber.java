package shishkin.cleanarchitecture.mvi.sl;

import java.util.List;

/**
 * Интерфейс объекта, который регистрируется у специалистов для получения/предоставления сервиса
 */
public interface SpecialistSubscriber extends Subscriber, Validated {

    /**
     * Получить список имен специалистов, у которых должен быть зарегистрирован объект
     *
     * @return список имен специалистов
     */
    List<String> getSpecialistSubscription();

}
