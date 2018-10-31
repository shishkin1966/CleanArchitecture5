package shishkin.cleanarchitecture.mvi.sl.repository;

import shishkin.cleanarchitecture.mvi.sl.Validated;
import shishkin.cleanarchitecture.mvi.sl.data.Result;

/**
 * Created by Shishkin on 27.12.2017.
 */

public abstract class AbsProvider implements Provider, Validated {

    public abstract Result<Boolean> validateExt();

    @Override
    public boolean validate() {
        return validateExt().getData();
    }

    @Override
    public void stop() {
    }
}
