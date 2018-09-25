package shishkin.cleanarchitecture.mvi.app.viewdata;

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

    @Override
    public Request getRequest() {
        return new SaveTickerRequest(NAME, getData());
    }

}
