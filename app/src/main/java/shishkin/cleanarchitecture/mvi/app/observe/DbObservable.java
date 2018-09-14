package shishkin.cleanarchitecture.mvi.app.observe;

import shishkin.cleanarchitecture.mvi.sl.observe.AbsDbObservable;

/**
 * Created by Shishkin on 04.02.2018.
 */

public class DbObservable extends AbsDbObservable {

    public static final String NAME = DbObservable.class.getName();

    @Override
    public String getName() {
        return NAME;
    }
}
