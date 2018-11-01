package shishkin.cleanarchitecture.mvi.app.screen.fragment.paging_google;

import android.support.v4.widget.SwipeRefreshLayout;


import java.util.List;


import shishkin.cleanarchitecture.mvi.app.SLUtil;
import shishkin.cleanarchitecture.mvi.app.data.Account;
import shishkin.cleanarchitecture.mvi.app.screen.fragment.paging.PagingViewData;
import shishkin.cleanarchitecture.mvi.common.utils.ApplicationUtils;
import shishkin.cleanarchitecture.mvi.sl.data.Result;
import shishkin.cleanarchitecture.mvi.sl.event.ShowMessageEvent;
import shishkin.cleanarchitecture.mvi.sl.presenter.AbsPresenter;
import shishkin.cleanarchitecture.mvi.sl.request.ResponseListener;

/**
 * Created by Shishkin on 17.03.2018.
 */

public class PagingGooglePresenter extends AbsPresenter<PagingGoogleModel> implements SwipeRefreshLayout.OnRefreshListener, ResponseListener {

    public static final String NAME = PagingGooglePresenter.class.getName();

    private PagingViewData viewData;

    public PagingGooglePresenter(PagingGoogleModel model) {
        super(model);
    }

    @Override
    public void onStart() {
        if (viewData == null) {
            viewData = new PagingViewData();
        }
    }

    @Override
    public void response(Result result) {
        if (!validate()) return;

        if (!result.hasError()) {
            viewData.addAccounts((List<Account>) result.getData());
            getModel().getView().refreshViews(viewData);
        } else {
            SLUtil.getViewUnion().showMessage(new ShowMessageEvent(result.getErrorText()).setType(ApplicationUtils.MESSAGE_TYPE_ERROR));
        }

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
    public void onRefresh() {
        viewData.clearAccounts();
        getModel().getView().onRefresh();
    }

    public void showProgressBar() {
        getModel().getView().showProgressBar();
    }

    public void hideProgressBar() {
        getModel().getView().hideProgressBar();
    }
}

