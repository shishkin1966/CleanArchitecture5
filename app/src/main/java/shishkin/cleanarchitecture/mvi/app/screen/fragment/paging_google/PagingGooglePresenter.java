package shishkin.cleanarchitecture.mvi.app.screen.fragment.paging_google;

import android.support.v4.widget.SwipeRefreshLayout;


import shishkin.cleanarchitecture.mvi.sl.presenter.AbsPresenter;

/**
 * Created by Shishkin on 17.03.2018.
 */

public class PagingGooglePresenter extends AbsPresenter<PagingGoogleModel> implements SwipeRefreshLayout.OnRefreshListener {

    public static final String NAME = PagingGooglePresenter.class.getName();

    PagingGooglePresenter(PagingGoogleModel model) {
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
    public void onRefresh() {
        getModel().getView().onRefresh();
    }

    public void showProgressBar() {
        getModel().getView().showProgressBar();
    }

    public void hideProgressBar() {
        getModel().getView().hideProgressBar();
    }
}

