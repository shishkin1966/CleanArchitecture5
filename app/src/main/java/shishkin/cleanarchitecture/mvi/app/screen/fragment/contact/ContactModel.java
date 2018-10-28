package shishkin.cleanarchitecture.mvi.app.screen.fragment.contact;

import shishkin.cleanarchitecture.mvi.sl.model.AbsModel;

/**
 * Created by Shishkin on 17.03.2018.
 */

public class ContactModel extends AbsModel {

    ContactModel(ContactFragment fragment) {
        super(fragment);

        setRouter(new ContactRouter(this));
        setPresenter(new ContactPresenter(this));
    }

    @Override
    public ContactFragment getView() {
        return super.getView();
    }

    @Override
    public ContactRouter getRouter() {
        return super.getRouter();
    }

    @Override
    public ContactPresenter getPresenter() {
        return super.getPresenter();
    }

}
