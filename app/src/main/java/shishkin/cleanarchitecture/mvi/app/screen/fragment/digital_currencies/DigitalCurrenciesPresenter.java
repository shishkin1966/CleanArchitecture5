package shishkin.cleanarchitecture.mvi.app.screen.fragment.digital_currencies;

import java.util.Comparator;
import java.util.List;
import java.util.Observable;
import java.util.Observer;


import shishkin.cleanarchitecture.mvi.app.SLUtil;
import shishkin.cleanarchitecture.mvi.app.data.Ticker;
import shishkin.cleanarchitecture.mvi.app.sl.Repository;
import shishkin.cleanarchitecture.mvi.app.viewdata.TickerViewData;
import shishkin.cleanarchitecture.mvi.common.utils.ApplicationUtils;
import shishkin.cleanarchitecture.mvi.common.utils.StringUtils;
import shishkin.cleanarchitecture.mvi.sl.data.Result;
import shishkin.cleanarchitecture.mvi.sl.event.ShowMessageEvent;
import shishkin.cleanarchitecture.mvi.sl.presenter.AbsPresenter;
import shishkin.cleanarchitecture.mvi.sl.request.ResponseListener;

/**
 * Created by Shishkin on 17.03.2018.
 */

public class DigitalCurrenciesPresenter extends AbsPresenter<DigitalCurrenciesModel> implements ResponseListener, Observer {

    public static final String NAME = DigitalCurrenciesPresenter.class.getName();

    private TickerViewData mViewData = new TickerViewData();

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
        setData();
        getModel().getView().showProgressBar();
        Repository.getInstance().getTicker(NAME);
    }

    @Override
    public void response(Result result) {
        getModel().getView().hideProgressBar();
        if (!result.hasError()) {
            mViewData.setData(SLUtil.getDataSpecialist().sort((List<Ticker>) result.getData(), (o1, o2) -> o1.getSymbol().compareTo(o2.getSymbol())).toList());
            setData();
        } else {
            SLUtil.getActivityUnion().showToast(new ShowMessageEvent(result.getErrorText()).setType(ApplicationUtils.MESSAGE_TYPE_ERROR));
        }
    }

    @Override
    public void update(Observable o, Object arg) {
        if (arg != null && !arg.equals(mViewData.getPattern())) {
            mViewData.setPattern((String) arg);
            setData();
        }
    }

    private void setData() {
        if (mViewData.getData() == null) return;

        if (StringUtils.isNullOrEmpty(mViewData.getPattern())) {
            getModel().getView().refreshTickers(mViewData.getData());
        } else {
            getModel().getView().refreshTickers(SLUtil.getDataSpecialist().filter(mViewData.getData(), item -> StringUtils.containsIgnoreCase(item.getName(), mViewData.getPattern())).toList());
        }
    }

    public TickerViewData getViewData() {
        return mViewData;
    }

}

