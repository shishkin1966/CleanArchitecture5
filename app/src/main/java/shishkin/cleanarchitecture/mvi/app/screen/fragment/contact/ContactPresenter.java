package shishkin.cleanarchitecture.mvi.app.screen.fragment.contact;

import android.view.View;


import shishkin.cleanarchitecture.mvi.R;
import shishkin.cleanarchitecture.mvi.sl.presenter.AbsPresenter;

/**
 * Created by Shishkin on 17.03.2018.
 */

public class ContactPresenter extends AbsPresenter<ContactModel> implements View.OnClickListener {

    public static final String NAME = ContactPresenter.class.getName();

    ContactPresenter(ContactModel model) {
        super(model);
    }

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public boolean isRegister() {
        return true;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.web:
                getModel().getRouter().showUrl();
                break;

            case R.id.mail:
                getModel().getRouter().sendMail();
                break;

        }
    }
}

