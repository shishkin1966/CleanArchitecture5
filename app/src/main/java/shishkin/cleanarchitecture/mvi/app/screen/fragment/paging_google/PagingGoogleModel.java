package shishkin.cleanarchitecture.mvi.app.screen.fragment.paging_google;

import shishkin.cleanarchitecture.mvi.sl.model.AbsModel;

/**
 * Created by Shishkin on 17.03.2018.
 */

public class PagingGoogleModel extends AbsModel {

    PagingGoogleModel(PagingGoogleFragment fragment) {
        super(fragment);

        setPresenter(new PagingGooglePresenter(this));
    }

    @Override
    public PagingGoogleFragment getView() {
        return super.getView();
    }

    @Override
    public PagingGooglePresenter getPresenter() {
        return super.getPresenter();
    }
}
