package shishkin.cleanarchitecture.mvi.app.screen.fragment.portion_load;

import shishkin.cleanarchitecture.mvi.sl.model.AbsModel;

/**
 * Created by Shishkin on 17.03.2018.
 */

public class PortionModel extends AbsModel {

    PortionModel(PortionFragment fragment) {
        super(fragment);

        setPresenter(new PortionPresenter(this));
    }

    @Override
    public PortionFragment getView() {
        return super.getView();
    }

    @Override
    public PortionPresenter getPresenter() {
        return super.getPresenter();
    }

}
