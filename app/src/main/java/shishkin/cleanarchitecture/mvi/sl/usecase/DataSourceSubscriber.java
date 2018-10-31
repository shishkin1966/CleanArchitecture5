package shishkin.cleanarchitecture.mvi.sl.usecase;

import shishkin.cleanarchitecture.mvi.sl.SpecialistSubscriber;

public interface DataSourceSubscriber extends SpecialistSubscriber {

    void invalidate();

    void onInvalidated();

    void refresh();
}
