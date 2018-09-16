package shishkin.cleanarchitecture.mvi.sl;

import shishkin.cleanarchitecture.mvi.sl.event.OnActionEvent;

/**
 * Интерфейс специалиста пользовательской и бизнес логики
 */
public interface UseCasesSpecialist extends Specialist {

    /**
     * Событие - выполнить Action
     *
     * @param event событие
     */
    void onAction(OnActionEvent event);
}
