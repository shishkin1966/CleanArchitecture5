package shishkin.cleanarchitecture.mvi.app.screen.fragment.val_curs;

import shishkin.cleanarchitecture.mvi.sl.model.AbsModel;

/**
 * Created by Shishkin on 17.03.2018.
 */

public class ValCursModel extends AbsModel {

    ValCursModel(ValCursFragment fragment) {
        super(fragment);

        setInteractor(new ValCursInteractor());
        setPresenter(new ValCursPresenter(this));
    }

    @Override
    public ValCursFragment getView() {
        return super.getView();
    }

    @Override
    public ValCursPresenter getPresenter() {
        return super.getPresenter();
    }

    @Override
    public ValCursInteractor getInteractor() {
        return super.getInteractor();
    }
}
