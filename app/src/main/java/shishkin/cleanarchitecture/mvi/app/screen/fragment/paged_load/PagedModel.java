package shishkin.cleanarchitecture.mvi.app.screen.fragment.paged_load;

import shishkin.cleanarchitecture.mvi.sl.model.AbsModel;

/**
 * Created by Shishkin on 17.03.2018.
 */

public class PagedModel extends AbsModel {

    PagedModel(PagedFragment fragment) {
        super(fragment);

        setPresenter(new PagedPresenter(this));
    }

    @Override
    public PagedFragment getView() {
        return super.getView();
    }

    @Override
    public PagedPresenter getPresenter() {
        return super.getPresenter();
    }
}
