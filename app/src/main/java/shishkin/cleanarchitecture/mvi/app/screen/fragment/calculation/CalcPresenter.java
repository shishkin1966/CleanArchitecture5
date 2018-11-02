package shishkin.cleanarchitecture.mvi.app.screen.fragment.calculation;

import android.widget.EditText;


import java.util.List;
import java.util.Observable;
import java.util.Observer;


import shishkin.cleanarchitecture.mvi.app.SLUtil;
import shishkin.cleanarchitecture.mvi.app.specialist.calculation.CalculationSubscriber;
import shishkin.cleanarchitecture.mvi.app.specialist.calculation.CalculationUnionImpl;
import shishkin.cleanarchitecture.mvi.common.utils.StringUtils;
import shishkin.cleanarchitecture.mvi.sl.data.Result;
import shishkin.cleanarchitecture.mvi.sl.observe.EditTextObservable;
import shishkin.cleanarchitecture.mvi.sl.presenter.AbsPresenter;
import shishkin.cleanarchitecture.mvi.sl.request.ResponseListener;

/**
 * Created by Shishkin on 17.03.2018.
 */

public class CalcPresenter extends AbsPresenter<CalcModel> implements Observer, CalculationSubscriber, ResponseListener {

    public static final String NAME = CalcPresenter.class.getName();

    private CalcViewData viewData;

    CalcPresenter(CalcModel model) {
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
            viewData = new CalcViewData();
        }
        getModel().getView().refreshViews(viewData);
    }

    @Override
    public void update(Observable o, Object arg) {
        final EditText editText = ((EditTextObservable) o).getView();
        final String tag = (String) editText.getTag();
        final String value = StringUtils.allTrim((String) arg);
        switch (tag) {
            case "1":
                viewData.setItem1(value);
                calc();
                break;
            case "2":
                viewData.setItem2(value);
                calc();
                break;
            case "3":
                viewData.setItem3(value);
                calc();
                break;
            case "4":
                viewData.setItem4(value);
                calc();
                break;
            case "5":
                viewData.setItem5(value);
                calc();
                break;
        }
    }

    private void calc() {
        getModel().getView().showProgressBar();
        SLUtil.getCalculationUnion().execute(viewData);
    }

    @Override
    public List<String> getSpecialistSubscription() {
        return StringUtils.arrayToList(
                super.getSpecialistSubscription(),
                CalculationUnionImpl.NAME
        );
    }

    @Override
    public void response(Result result) {
        getModel().getView().hideProgressBar();
        this.viewData = (CalcViewData) result.getData();

        getModel().getView().refreshViews(viewData);
    }
}

