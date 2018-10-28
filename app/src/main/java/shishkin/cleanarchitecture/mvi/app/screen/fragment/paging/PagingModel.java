package shishkin.cleanarchitecture.mvi.app.screen.fragment.paging;

import shishkin.cleanarchitecture.mvi.sl.model.AbsModel;

/**
 * Created by Shishkin on 17.03.2018.
 */

public class PagingModel extends AbsModel {

    PagingModel(PagingFragment fragment) {
        super(fragment);

        setPresenter(new PagingPresenter(this));
    }

    @Override
    public PagingFragment getView() {
        return super.getView();
    }

    @Override
    public PagingPresenter getPresenter() {
        return super.getPresenter();
    }

}
