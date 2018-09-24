package shishkin.cleanarchitecture.mvi.app.request;

import java.util.List;


import retrofit2.Call;
import shishkin.cleanarchitecture.mvi.app.SLUtil;
import shishkin.cleanarchitecture.mvi.app.data.Ticker;
import shishkin.cleanarchitecture.mvi.sl.request.AbsNetResultMailRequest;

/**
 * Created by Shishkin on 06.12.2017.
 */

public class GetTickerRequest extends AbsNetResultMailRequest<List<Ticker>> {

    public static final String NAME = GetTickerRequest.class.getName();

    public GetTickerRequest(final String listener) {
        super(listener);
    }

    @Override
    public Call<List<Ticker>> getData() {
        return SLUtil.getNetProvider().getApi().getTicker();
    }

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public boolean isDistinct() {
        return true;
    }
}
