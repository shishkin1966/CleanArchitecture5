package shishkin.cleanarchitecture.mvi.app.screen.fragment.create_account;

import java.util.Observable;
import java.util.Observer;


import shishkin.cleanarchitecture.mvi.app.data.Account;
import shishkin.cleanarchitecture.mvi.app.request.AddAccountRequest;
import shishkin.cleanarchitecture.mvi.common.utils.ApplicationUtils;
import shishkin.cleanarchitecture.mvi.sl.SLUtil;
import shishkin.cleanarchitecture.mvi.sl.data.Result;
import shishkin.cleanarchitecture.mvi.sl.event.ShowMessageEvent;
import shishkin.cleanarchitecture.mvi.sl.presenter.AbsPresenter;
import shishkin.cleanarchitecture.mvi.sl.request.ResponseListener;

/**
 * Created by Shishkin on 17.03.2018.
 */

public class CreateAccountPresenter extends AbsPresenter<CreateAccountModel> implements ResponseListener, Observer {

    public static final String NAME = CreateAccountPresenter.class.getName();

    public CreateAccountPresenter(CreateAccountModel model) {
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
    public void response(Result result) {
        getModel().getView().hideProgressBar();
        if (!result.hasError()) {
            if (result.getName().equals(AddAccountRequest.NAME)) {
                getModel().getView().exit();
            }
        } else {
            SLUtil.getActivityUnion().showToast(new ShowMessageEvent(result.getErrorText()).setType(ApplicationUtils.MESSAGE_TYPE_ERROR));
        }
    }

    @Override
    public void update(Observable o, Object arg) {
        getModel().getView().refresh();
    }

    public void createAccount(Account account) {
        getModel().getView().showProgressBar();
        getModel().getInteractor().addAccount(account, this);
    }
}

