package shishkin.cleanarchitecture.mvi.app.specialist.calculation;

import androidx.annotation.NonNull;


import shishkin.cleanarchitecture.mvi.app.SLUtil;
import shishkin.cleanarchitecture.mvi.app.screen.fragment.calculation.CalcViewData;
import shishkin.cleanarchitecture.mvi.sl.AbsSmallUnion;
import shishkin.cleanarchitecture.mvi.sl.data.Result;
import shishkin.cleanarchitecture.mvi.sl.mail.ResultMail;
import shishkin.cleanarchitecture.mvi.sl.request.ResponseListener;

public class CalculationUnionImpl extends AbsSmallUnion<CalculationSubscriber> implements CalculationUnion, ResponseListener {

    public static final String NAME = CalculationUnionImpl.class.getName();

    @Override
    public int compareTo(@NonNull Object o) {
        return (CalculationUnion.class.isInstance(o)) ? 0 : 1;
    }

    @Override
    public void execute(CalcViewData viewData) {
        if (hasSubscribers()) {
            CalculationTask.executeInstance(this, viewData);
        }
    }

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public void response(Result result) {
        for (CalculationSubscriber subscriber : getSubscribers()) {
            SLUtil.addMail(new ResultMail(subscriber.getName(), result));
        }
    }
}
