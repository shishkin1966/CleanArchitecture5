package shishkin.cleanarchitecture.mvi.app.request;

import java.util.List;


import shishkin.cleanarchitecture.mvi.app.SLUtil;
import shishkin.cleanarchitecture.mvi.app.db.MviDb;
import shishkin.cleanarchitecture.mvi.sl.data.ExtError;
import shishkin.cleanarchitecture.mvi.sl.request.AbsResultRequest;
import shishkin.cleanarchitecture.mvi.sl.request.Request;
import shishkin.cleanarchitecture.mvi.sl.request.ResponseListener;
import shishkin.cleanarchitecture.mvi.sl.task.RequestThreadPoolExecutor;

/**
 * Created by Shishkin on 06.12.2017.
 */

public class GetCurrencyRequest extends AbsResultRequest<List<String>> {

    public static final String NAME = GetCurrencyRequest.class.getName();

    public GetCurrencyRequest(ResponseListener listener) {
        super(listener);
    }

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public boolean isDistinct() {
        return true;
    }

    @Override
    public int getAction(Request oldRequest) {
        return RequestThreadPoolExecutor.ACTION_DELETE;
    }

    @Override
    public void run() {
        try {
            final MviDb db = SLUtil.getDb();
            setData(db.MviDao().getCurrency());
        } catch (Exception e) {
            SLUtil.onError(NAME, e);
            setError(new ExtError().addError(NAME, e));
        }
        response();
    }
}
