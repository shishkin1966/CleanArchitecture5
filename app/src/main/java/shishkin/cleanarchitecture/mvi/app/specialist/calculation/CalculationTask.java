package shishkin.cleanarchitecture.mvi.app.specialist.calculation;

import android.os.AsyncTask;


import shishkin.cleanarchitecture.mvi.app.SLUtil;
import shishkin.cleanarchitecture.mvi.app.screen.fragment.calculation.CalcViewData;
import shishkin.cleanarchitecture.mvi.common.utils.StringUtils;
import shishkin.cleanarchitecture.mvi.sl.data.Result;
import shishkin.cleanarchitecture.mvi.sl.request.ResponseListener;

/**
 * Created by V.Bokov on 08-Oct-15.
 */
public class CalculationTask extends AsyncTask<Void, Void, Result<CalcViewData>> {

    public static final String NAME = CalculationTask.class.getName();

    private static volatile CalculationTask sInstance;

    private ResponseListener listener;
    private CalcViewData viewData;

    public static void executeInstance(ResponseListener listener, CalcViewData viewData) {
        if (sInstance != null) {
            if (!sInstance.isCancelled()) {
                sInstance.cancel(true);
            }
        }

        sInstance = new CalculationTask(listener, viewData);
        sInstance.execute();
    }

    @Override
    protected void onPreExecute() {
    }

    private CalculationTask(ResponseListener listener, CalcViewData viewData) {
        this.listener = listener;
        this.viewData = viewData;
    }

    @Override
    protected Result<CalcViewData> doInBackground(Void... params) {
        if (isCancelled()) {
            return null;
        }

        try {
            Thread.sleep(3000);
            Thread.yield();
            
            if (isCancelled()) {
                return null;
            }

            int sum = 0;
            sum += StringUtils.toInt(viewData.getItem1());
            sum += StringUtils.toInt(viewData.getItem2());
            sum += StringUtils.toInt(viewData.getItem3());
            sum += StringUtils.toInt(viewData.getItem4());
            sum += StringUtils.toInt(viewData.getItem5());
            viewData.setSum(sum);
            return new Result<>(viewData);
        } catch (Exception e) {
            SLUtil.onError(NAME, e);
            return new Result<>(viewData).setError(NAME, e);
        }
    }

    @Override
    protected void onPostExecute(Result<CalcViewData> result) {
        if (isCancelled() || result == null) {
            return;
        }

        if (listener != null) {
            listener.response(result);
        }
    }
}