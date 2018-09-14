package shishkin.cleanarchitecture.mvi.sl;

import android.support.annotation.NonNull;


import shishkin.cleanarchitecture.mvi.sl.event.OnActionEvent;
import shishkin.cleanarchitecture.mvi.sl.usecase.OnActionUseCase;

/**
 * Модуль реализующий бизнес и пользовательскую логику в приложении
 */
@SuppressWarnings("unused")
public class UseCasesSpecialistImpl extends AbsSpecialist implements UseCasesSpecialist {
    public static final String NAME = UseCasesSpecialistImpl.class.getName();

    public UseCasesSpecialistImpl() {
    }

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public void onAction(final OnActionEvent event) {
        OnActionUseCase.onClick(event);
    }

    @Override
    public int compareTo(@NonNull Object o) {
        return (UseCasesSpecialist.class.isInstance(o)) ? 0 : 1;
    }
}