package shishkin.cleanarchitecture.mvi.sl.usecase;

import shishkin.cleanarchitecture.mvi.sl.Subscriber;
import shishkin.cleanarchitecture.mvi.sl.Validated;
import shishkin.cleanarchitecture.mvi.sl.data.Result;

public abstract class AbsUseCase implements Subscriber, Validated {

    public abstract String getName();

    @Override
    public Result<Boolean> validateExt() {
        return new Result<>(true);
    }

    @Override
    public boolean validate() {
        return validateExt().getData();
    }
}
