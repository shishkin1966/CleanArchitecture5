package shishkin.cleanarchitecture.mvi.app.view_data;

import shishkin.cleanarchitecture.mvi.app.data.Ticker;
import shishkin.cleanarchitecture.mvi.app.request.SaveTickerRequest;
import shishkin.cleanarchitecture.mvi.sl.request.Request;

public class TickerViewData extends AbsCachedViewData<Ticker> {

    private static final String NAME = TickerViewData.class.getName();

    public TickerViewData() {
        super(Ticker.class);
    }

    @Override
    public String getName() {
        return NAME;
    }

    public boolean isCacheEnabled() {
        return true;
    }

    @Override
    public Request getRequest() {
        return new SaveTickerRequest(NAME, getData());
    }

}
