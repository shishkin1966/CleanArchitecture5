package shishkin.cleanarchitecture.mvi.app.screen.fragment.portion_load;

import android.support.v4.widget.SwipeRefreshLayout;


import java.util.List;


import shishkin.cleanarchitecture.mvi.app.SLUtil;
import shishkin.cleanarchitecture.mvi.app.data.Account;
import shishkin.cleanarchitecture.mvi.common.utils.ApplicationUtils;
import shishkin.cleanarchitecture.mvi.sl.data.Result;
import shishkin.cleanarchitecture.mvi.sl.event.ShowMessageEvent;
import shishkin.cleanarchitecture.mvi.sl.presenter.AbsPresenter;
import shishkin.cleanarchitecture.mvi.sl.request.ResponseListener;

/**
 * Created by Shishkin on 17.03.2018.
 */

public class PortionPresenter extends AbsPresenter<PortionModel> implements ResponseListener, SwipeRefreshLayout.OnRefreshListener {

    public static final String NAME = PortionPresenter.class.getName();

    private PagingViewData viewData;

    PortionPresenter(PortionModel model) {
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
        if (viewData == null) {
            viewData = new PagingViewData();
        }
        viewData.clearAccounts();
        getData();
    }

    private void getData() {
        getModel().getView().showProgressBar();
        SLUtil.getRepository().getPagingAccounts(NAME);
    }

    @Override
    public void response(Result result) {
        if (!validate()) return;

        if (!result.hasError()) {
            if (result.getOrder() == Result.LAST) {
                getModel().getView().hideProgressBar();
            } else {
                getModel().getView().showProgressBar();
            }
            viewData.addAccounts((List<Account>) result.getData());
            getModel().getView().refreshViews(viewData);
        } else {
            getModel().getView().hideProgressBar();
            SLUtil.getViewUnion().showMessage(new ShowMessageEvent(result.getErrorText()).setType(ApplicationUtils.MESSAGE_TYPE_ERROR));
        }
    }

    @Override
    public void onRefresh() {
        SLUtil.getRepository().cancelRequests(NAME);
        viewData.clearAccounts();
        getModel().getView().refreshViews(viewData);
        getData();
    }

    @Override
    public void onStop() {
        viewData.clearAccounts();
        SLUtil.getRepository().cancelRequests(NAME);
    }
}

