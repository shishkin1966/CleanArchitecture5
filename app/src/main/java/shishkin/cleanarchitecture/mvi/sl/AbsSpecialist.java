package shishkin.cleanarchitecture.mvi.sl;

import shishkin.cleanarchitecture.mvi.sl.data.Result;

/**
 * Абстрактный специалист - объект предоставлющий сервис
 */
@SuppressWarnings("unused")
public abstract class AbsSpecialist implements Specialist {
    @Override
    public void onUnRegister() {
    }

    @Override
    public void onRegister() {
    }

    @Override
    public boolean isPersistent() {
        return false;
    }

    @Override
    public Result<Boolean> validateExt() {
        return new Result<>(true);
    }

    @Override
    public boolean validate() {
        return validateExt().getData();
    }

    @Override
    public void stop() {
    }

    @Override
    public String getPasport() {
        return getName();
    }
}
