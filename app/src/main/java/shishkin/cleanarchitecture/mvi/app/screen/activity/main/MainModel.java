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
    }

    @Override
    public BaseModelRouter getRouter() {
        return super.getRouter();
    }
}
