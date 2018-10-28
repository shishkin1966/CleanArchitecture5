package shishkin.cleanarchitecture.mvi.app.screen.fragment.map;

import shishkin.cleanarchitecture.mvi.sl.model.AbsModel;

/**
 * Created by Shishkin on 27.11.2017.
 */

public class MapModel extends AbsModel {

    MapModel(MapFragment fragment) {
        super(fragment);

        setPresenter(new MapPresenter(this));
    }

    @Override
    public MapView getView() {
        return super.getView();
    }

    @Override
    public MapPresenter getPresenter() {
        return super.getPresenter();
    }

}
