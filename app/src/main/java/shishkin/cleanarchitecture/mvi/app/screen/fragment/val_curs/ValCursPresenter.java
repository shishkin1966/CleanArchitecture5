package shishkin.cleanarchitecture.mvi.app.screen.fragment.val_curs;

import android.support.v4.widget.SwipeRefreshLayout;


import shishkin.cleanarchitecture.mvi.app.SLUtil;
import shishkin.cleanarchitecture.mvi.app.data.ValCurs;
import shishkin.cleanarchitecture.mvi.app.repository.Repository;
import shishkin.cleanarchitecture.mvi.common.utils.StringUtils;
import shishkin.cleanarchitecture.mvi.sl.data.Result;
import shishkin.cleanarchitecture.mvi.sl.presenter.AbsPresenter;
import shishkin.cleanarchitecture.mvi.sl.request.ResponseListener;

/**
 * Created by Shishkin on 17.03.2018.
 */

public class ValCursPresenter extends AbsPresenter<ValCursModel> implements ResponseListener, SwipeRefreshLayout.OnRefreshListener {

    public static final String NAME = ValCursPresenter.class.getName();

    private ValCursViewData viewData;

    public ValCursPresenter(ValCursModel model) {
        super(model);

        viewData = SLUtil.getCacheSpecialist().get(ValCursViewData.NAME, ValCursViewData.class);
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
            viewData = SLUtil.getCacheSpecialist().get(ValCursViewData.NAME, ValCursViewData.class);
            if (viewData == null) {
                viewData = new ValCursViewData();
            }
        }
        getModel().getView().refreshViews(viewData);
        getData();
    }

    private void getData() {
        getModel().getView().showProgressBar();
        Repository.getValCurs(NAME, StringUtils.formatDate(System.currentTimeMillis(), "dd/MM/yyyy"));
    }

    @Override
    public void response(Result result) {
        if (!validate()) return;

        getModel().getView().hideProgressBar();
        if (!result.hasError()) {
            viewData.setValCurs((ValCurs) result.getData());
            getModel().getView().refreshViews(viewData);
        } else {
            SLUtil.onError(NAME, result.getErrorText(), true);
        }
    }

    @Override
    public void onStop() {
        SLUtil.getCacheSpecialist().put(ValCursViewData.NAME, viewData);
    }

    @Override
    public void onRefresh() {
        getData();
    }
}

