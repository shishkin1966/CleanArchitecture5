package shishkin.cleanarchitecture.mvi.app.screen.fragment.setting;

import shishkin.cleanarchitecture.mvi.sl.model.AbsModel;

/**
 * Created by Shishkin on 27.11.2017.
 */

public class SettingModel extends AbsModel {

    public SettingModel(SettingFragment fragment) {
        super(fragment);

        setPresenter(new SettingPresenter(this));
    }

    @Override
    public SettingPresenter getPresenter() {
        return super.getPresenter();
    }

    @Override
    public SettingFragment getView() {
        return super.getView();
    }

}
