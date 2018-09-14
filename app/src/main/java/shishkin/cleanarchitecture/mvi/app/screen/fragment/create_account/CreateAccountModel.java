package shishkin.cleanarchitecture.mvi.app.screen.fragment.create_account;

import shishkin.cleanarchitecture.mvi.sl.model.AbsModel;

/**
 * Created by Shishkin on 17.03.2018.
 */

public class CreateAccountModel extends AbsModel {

    public CreateAccountModel(CreateAccountFragment fragment) {
        super(fragment);

        setInteractor(new CreateAccountInteractor());
        setPresenter(new CreateAccountPresenter(this));
    }

    @Override
    public CreateAccountFragment getView() {
        return super.getView();
    }

    @Override
    public CreateAccountPresenter getPresenter() {
        return super.getPresenter();
    }

    @Override
    public CreateAccountInteractor getInteractor() {
        return super.getInteractor();
    }
}
