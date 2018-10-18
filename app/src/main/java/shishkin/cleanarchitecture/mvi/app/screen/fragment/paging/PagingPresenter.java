package shishkin.cleanarchitecture.mvi.app.screen.fragment.paging;

import android.support.v4.widget.SwipeRefreshLayout;
import android.widget.Toast;


import java.util.List;


import shishkin.cleanarchitecture.mvi.app.SLUtil;
import shishkin.cleanarchitecture.mvi.app.data.Account;
import shishkin.cleanarchitecture.mvi.app.repository.Repository;
import shishkin.cleanarchitecture.mvi.app.screen.fragment.val_curs.ValCursViewData;
import shishkin.cleanarchitecture.mvi.common.utils.ApplicationUtils;
import shishkin.cleanarchitecture.mvi.sl.data.Result;
import shishkin.cleanarchitecture.mvi.sl.event.ShowMessageEvent;
import shishkin.cleanarchitecture.mvi.sl.presenter.AbsPresenter;
import shishkin.cleanarchitecture.mvi.sl.request.ResponseListener;

/**
 * Created by Shishkin on 17.03.2018.
 */

public class PagingPresenter extends AbsPresenter<PagingModel> implements ResponseListener, SwipeRefreshLayout.OnRefreshListener {

    public static final String NAME = PagingPresenter.class.getName();

    private PagingViewData viewData;

    public PagingPresenter(PagingModel model) {
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
        Repository.getPagingAccounts(NAME);
    }

    @Override
    public void response(Result result) {
        if (!validate()) return;

        if (!result.hasError()) {
            if (result.getOrder() == Result.LAST) {
                getModel().getView().hideProgressBar();
            }
            viewData.addAccounts((List<Account>) result.getData());
            getModel().getView().refreshViews(viewData);
        } else {
            getModel().getView().hideProgressBar();
            SLUtil.getActivityUnion().showMessage(new ShowMessageEvent(result.getErrorText()).setType(ApplicationUtils.MESSAGE_TYPE_ERROR));
        }
    }

    @Override
    public void onRefresh() {
        Repository.cancelRequests(NAME);
        viewData.clearAccounts();
        getModel().getView().refreshViews(viewData);
        getData();
    }

    @Override
    public void onStop() {
        viewData.clearAccounts();
        Repository.cancelRequests(NAME);
    }
}

