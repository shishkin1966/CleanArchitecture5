package shishkin.cleanarchitecture.mvi.app.screen.fragment.scanner;

import shishkin.cleanarchitecture.mvi.sl.model.AbsModel;

/**
 * Created by Shishkin on 17.03.2018.
 */

public class ScannerModel extends AbsModel {

    ScannerModel(ScannerFragment fragment) {
        super(fragment);

        setPresenter(new ScannerPresenter(this));
    }

    @Override
    public ScannerFragment getView() {
        return super.getView();
    }

    @Override
    public ScannerPresenter getPresenter() {
        return super.getPresenter();
    }

}
