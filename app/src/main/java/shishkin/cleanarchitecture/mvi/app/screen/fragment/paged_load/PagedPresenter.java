package shishkin.cleanarchitecture.mvi.app.screen.fragment.paged_load;

import java.util.List;


import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import shishkin.cleanarchitecture.mvi.app.SLUtil;
import shishkin.cleanarchitecture.mvi.app.data.Account;
import shishkin.cleanarchitecture.mvi.app.screen.fragment.portion_load.PagingViewData;
import shishkin.cleanarchitecture.mvi.common.utils.ApplicationUtils;
import shishkin.cleanarchitecture.mvi.sl.data.Result;
import shishkin.cleanarchitecture.mvi.sl.event.ShowMessageEvent;
import shishkin.cleanarchitecture.mvi.sl.presenter.AbsPresenter;
import shishkin.cleanarchitecture.mvi.sl.request.ResponseListener;

/**
 * Created by Shishkin on 17.03.2018.
 */

public class PagedPresenter extends AbsPresenter<PagedModel> implements SwipeRefreshLayout.OnRefreshListener, ResponseListener {

    public static final String NAME = PagedPresenter.class.getName();

    private PagingViewData viewData;

    public PagedPresenter(PagedModel model) {
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

    public boolean onBackPressed() {
        SLUtil.getPaginatorUnion().unregister(AccountsPaginator.NAME);
        SLUtil.getViewUnion().switchToTopFragment();
        return true;
    }
}

