package shishkin.cleanarchitecture.mvi.app.screen.fragment.val_curs;

import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;


import java.util.ArrayList;


import shishkin.cleanarchitecture.mvi.R;
import shishkin.cleanarchitecture.mvi.app.SLUtil;
import shishkin.cleanarchitecture.mvi.app.data.ValCurs;
import shishkin.cleanarchitecture.mvi.app.data.Valute;
import shishkin.cleanarchitecture.mvi.common.utils.StringUtils;
import shishkin.cleanarchitecture.mvi.sl.data.Result;
import shishkin.cleanarchitecture.mvi.sl.presenter.AbsPresenter;
import shishkin.cleanarchitecture.mvi.sl.request.ResponseListener;

/**
 * Created by Shishkin on 17.03.2018.
 */

public class ValCursPresenter extends AbsPresenter<ValCursModel> implements ResponseListener, SwipeRefreshLayout.OnRefreshListener, View.OnClickListener {

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
        getModel().getView().refreshBottomNavigation(viewData);
        getData();
    }

    private void getData() {
        getModel().getView().showProgressBar();
        getModel().getInteractor().getValCurs(NAME, StringUtils.formatDate(System.currentTimeMillis(), "dd/MM/yyyy"));
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

    public boolean isSelected(Valute item) {
        return viewData.getSelected().containsKey(item.getName());
    }

    public void onClickItems(Valute item) {
        if (viewData.getSelected().containsKey(item.getName())) {
            viewData.getSelected().remove(item.getName());
        } else {
            viewData.getSelected().put(item.getName(), item);
        }
        getModel().getView().refreshSelected(viewData);
    }

    public void swipeItem(Valute item) {
        if (viewData.getSelected().containsKey(item.getName())) {
            viewData.getSelected().remove(item.getName());
            getModel().getView().refreshBottomNavigation(viewData);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.delete:
                getModel().getView().removeItems(new ArrayList<>(viewData.getSelected().values()));
                viewData.clearSelected();
                break;

        }
    }
}

