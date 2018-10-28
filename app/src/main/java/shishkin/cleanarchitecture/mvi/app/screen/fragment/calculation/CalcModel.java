package shishkin.cleanarchitecture.mvi.app.screen.fragment.calculation;

import shishkin.cleanarchitecture.mvi.sl.model.AbsModel;

/**
 * Created by Shishkin on 17.03.2018.
 */

public class CalcModel extends AbsModel {

    CalcModel(CalcFragment fragment) {
        super(fragment);

        setPresenter(new CalcPresenter(this));
    }

    @Override
    public CalcFragment getView() {
        return super.getView();
    }

    @Override
    public CalcPresenter getPresenter() {
        return super.getPresenter();
    }
}
