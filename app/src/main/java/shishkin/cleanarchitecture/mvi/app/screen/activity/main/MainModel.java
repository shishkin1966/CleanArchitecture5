package shishkin.cleanarchitecture.mvi.app.screen.activity.main;

import shishkin.cleanarchitecture.mvi.app.model.BaseModelRouter;
import shishkin.cleanarchitecture.mvi.sl.model.AbsModel;

/**
 * Created by Shishkin on 29.11.2017.
 */

public class MainModel extends AbsModel {

    public MainModel(MainActivity activity) {
        super(activity);

        setRouter(new BaseModelRouter(this));
        setPresenter(new MainPresenter(this));
    }

    @Override
    public BaseModelRouter getRouter() {
        return super.getRouter();
    }

    @Override
    public MainPresenter getPresenter() {
        return super.getPresenter();
    }

    @Override
    public MainView getView() {
        return super.getView();
    }
}
