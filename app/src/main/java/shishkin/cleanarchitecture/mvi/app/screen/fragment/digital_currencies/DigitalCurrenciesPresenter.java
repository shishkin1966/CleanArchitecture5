package shishkin.cleanarchitecture.mvi.app.screen.fragment.digital_currencies;

import java.util.List;
import java.util.Observable;
import java.util.Observer;


import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import shishkin.cleanarchitecture.mvi.app.SLUtil;
import shishkin.cleanarchitecture.mvi.app.data.Ticker;
import shishkin.cleanarchitecture.mvi.sl.data.Result;
import shishkin.cleanarchitecture.mvi.sl.presenter.AbsPresenter;
import shishkin.cleanarchitecture.mvi.sl.request.ResponseListener;
import shishkin.cleanarchitecture.mvi.sl.viewaction.ViewAction;

/**
 * Created by Shishkin on 17.03.2018.
 */

public class DigitalCurrenciesPresenter extends AbsPresenter<DigitalCurrenciesModel> implements ResponseListener, Observer, SwipeRefreshLayout.OnRefreshListener {

    public static final String NAME = DigitalCurrenciesPresenter.class.getName();

    private TickerViewData viewData;

    DigitalCurrenciesPresenter(DigitalCurrenciesModel model) {
        super(model);

        viewData = SLUtil.getCacheSpecialist().get(TickerViewData.NAME, TickerViewData.class);
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
        viewData = getViewData();
        getModel().getView().doViewAction(new ViewAction("refreshViews", viewData));
        getData();
    }

    private void getData() {
        getModel().getView().showProgressBar();
        getModel().getInteractor().getTicker(NAME);
    }

    @Override
    public void response(Result result) {
        if (!validate()) return;

        getModel().getView().hideProgressBar();
        if (!result.hasError()) {
            viewData.setTickers((List<Ticker>) result.getData());
            getModel().getView().doViewAction(new ViewAction("refreshViews", viewData));
        } else {
            SLUtil.onError(NAME, result.getErrorText(), true);
        }
    }

    @Override
    public void update(Observable o, Object arg) {
        if (arg != null && !arg.equals(viewData.getFilter())) {
            viewData.setFilter((String) arg);
            getModel().getView().doViewAction(new ViewAction("refreshViews", viewData));
        }
    }

    @Override
    public void onStop() {
        SLUtil.getCacheSpecialist().put(TickerViewData.NAME, viewData);
    }

    @Override
    public void onRefresh() {
        getData();
    }

    public TickerViewData getViewData() {
        if (viewData == null) {
            viewData = SLUtil.getCacheSpecialist().get(TickerViewData.NAME, TickerViewData.class);
            if (viewData == null) {
                viewData = new TickerViewData();
            }
        }
        return viewData;
    }
}

