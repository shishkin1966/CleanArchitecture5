package shishkin.cleanarchitecture.mvi.app.request;

import java.util.List;


import retrofit2.Call;
import shishkin.cleanarchitecture.mvi.app.SLUtil;
import shishkin.cleanarchitecture.mvi.app.data.Ticker;
import shishkin.cleanarchitecture.mvi.sl.request.AbsNetResultMailRequest;
import shishkin.cleanarchitecture.mvi.sl.request.Request;
import shishkin.cleanarchitecture.mvi.sl.task.RequestThreadPoolExecutor;

/**
 * Created by Shishkin on 06.12.2017.
 */

public class GetTickerRequest extends AbsNetResultMailRequest<List<Ticker>> {

    public GetTickerRequest(final String listener) {
        super(listener);
    }

    @Override
    public Call<List<Ticker>> getData() {
        return SLUtil.getNetProvider().getApi().getTicker();
    }

    @Override
    public boolean isDistinct() {
        return true;
    }

    @Override
    public int getAction(Request oldRequest) {
        return RequestThreadPoolExecutor.ACTION_DELETE;
    }

}
