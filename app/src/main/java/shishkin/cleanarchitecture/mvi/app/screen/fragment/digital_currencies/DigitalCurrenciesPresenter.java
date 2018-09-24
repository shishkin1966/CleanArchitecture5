package shishkin.cleanarchitecture.mvi.app.screen.fragment.digital_currencies;

import java.util.List;


import shishkin.cleanarchitecture.mvi.app.SLUtil;
import shishkin.cleanarchitecture.mvi.app.data.Ticker;
import shishkin.cleanarchitecture.mvi.app.sl.Repository;
import shishkin.cleanarchitecture.mvi.common.utils.ApplicationUtils;
import shishkin.cleanarchitecture.mvi.sl.data.Result;
import shishkin.cleanarchitecture.mvi.sl.event.ShowMessageEvent;
import shishkin.cleanarchitecture.mvi.sl.presenter.AbsPresenter;
import shishkin.cleanarchitecture.mvi.sl.request.ResponseListener;

/**
 * Created by Shishkin on 17.03.2018.
 */

public class DigitalCurrenciesPresenter extends AbsPresenter<DigitalCurrenciesModel> implements ResponseListener {

    public static final String NAME = DigitalCurrenciesPresenter.class.getName();

    public DigitalCurrenciesPresenter(DigitalCurrenciesModel model) {
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
    public void onStart() {
        getModel().getView().showProgressBar();
        Repository.getInstance().getTicker(NAME);
    }

    @Override
    public void response(Result result) {
        getModel().getView().hideProgressBar();
        if (!result.hasError()) {
            getModel().getView().refreshTickers((List<Ticker>)result.getData());
        } else {
            SLUtil.getActivityUnion().showToast(new ShowMessageEvent(result.getErrorText()).setType(ApplicationUtils.MESSAGE_TYPE_ERROR));
        }
    }

}

