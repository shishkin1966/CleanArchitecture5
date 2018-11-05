package shishkin.cleanarchitecture.mvi.app.screen.fragment.val_curs;

import android.view.View;


import java.util.ArrayList;
import java.util.List;


import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import shishkin.cleanarchitecture.mvi.R;
import shishkin.cleanarchitecture.mvi.app.SLUtil;
import shishkin.cleanarchitecture.mvi.app.data.ValCurs;
import shishkin.cleanarchitecture.mvi.app.data.Valute;
import shishkin.cleanarchitecture.mvi.common.utils.StringUtils;
import shishkin.cleanarchitecture.mvi.sl.data.Result;
import shishkin.cleanarchitecture.mvi.sl.presenter.AbsPresenter;
import shishkin.cleanarchitecture.mvi.sl.request.ResponseListener;
import shishkin.cleanarchitecture.mvi.sl.viewaction.ViewAction;

/**
 * Created by Shishkin on 17.03.2018.
 */

public class ValCursPresenter extends AbsPresenter<ValCursModel> implements ResponseListener, SwipeRefreshLayout.OnRefreshListener, View.OnClickListener {

    public static final String NAME = ValCursPresenter.class.getName();

    private ValCursViewData viewData;

    ValCursPresenter(ValCursModel model) {
        super(model);

        viewData = SLUtil.getCacheSpecialist().get(ValCursViewData.NAME, ValCursViewData.class);
        if (viewData == null) {
            viewData = new ValCursViewData();
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
    public void onStart() {
        getModel().getView().doViewAction(new ViewAction("refreshViews", viewData));
        getModel().getView().doViewAction(new ViewAction("refreshBottomNavigation", viewData));
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
            getModel().getView().doViewAction(new ViewAction("refreshViews", viewData));
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

    void onClickItem(Valute item) {
        if (viewData.getSelected().containsKey(item.getName())) {
            viewData.getSelected().remove(item.getName());
        } else {
            viewData.getSelected().put(item.getName(), item);
        }
        getModel().getView().doViewAction(new ViewAction("refreshSelectedItems", viewData));
    }

    void onSwipedItem(Valute item) {
        viewData.getSelected().remove(item.getName());
        final List<Valute> list = new ArrayList<>();
        list.add(item);
        getModel().getView().doViewAction(new ViewAction("removeItems", list, viewData));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.delete:
                final List<Valute> list = new ArrayList<>(viewData.getSelected().values());
                viewData.clearSelected();
                getModel().getView().doViewAction(new ViewAction("removeItems", list, viewData));
                break;

            case R.id.clear:
                viewData.clearSelected();
                getModel().getView().doViewAction(new ViewAction("refreshSelectedItems", viewData));
                break;

        }
    }

    public void onBackPressed() {
        viewData.clearSelected();
    }
}

