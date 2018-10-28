package shishkin.cleanarchitecture.mvi.app.screen.fragment.digital_currencies;

import shishkin.cleanarchitecture.mvi.sl.model.AbsModel;

/**
 * Created by Shishkin on 17.03.2018.
 */

public class DigitalCurrenciesModel extends AbsModel {

    DigitalCurrenciesModel(DigitalCurrenciesFragment fragment) {
        super(fragment);

        setInteractor(new DigitalCurrenciesInteractor());
        setPresenter(new DigitalCurrenciesPresenter(this));
    }

    @Override
    public DigitalCurrenciesFragment getView() {
        return super.getView();
    }

    @Override
    public DigitalCurrenciesPresenter getPresenter() {
        return super.getPresenter();
    }

    @Override
    public DigitalCurrenciesInteractor getInteractor() {
        return super.getInteractor();
    }
}
