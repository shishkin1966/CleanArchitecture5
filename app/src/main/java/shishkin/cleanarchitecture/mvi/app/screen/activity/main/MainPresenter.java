package shishkin.cleanarchitecture.mvi.app.screen.activity.main;

import shishkin.cleanarchitecture.mvi.sl.presenter.AbsPresenter;

/**
 * Created by Shishkin on 17.03.2018.
 */

public class MainPresenter extends AbsPresenter<MainModel> {

    public static final String NAME = MainPresenter.class.getName();

    public MainPresenter(MainModel model) {
        super(model);
    }

    public void hideSideMenu() {
        getModel().getView().hideSideMenu();
    }

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public boolean isRegister() {
        return true;
    }
}



