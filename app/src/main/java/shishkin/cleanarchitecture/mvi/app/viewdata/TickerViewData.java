package shishkin.cleanarchitecture.mvi.app.viewdata;

import shishkin.cleanarchitecture.mvi.app.data.Ticker;

public class TickerViewData extends AbsCachedViewData<Ticker> {

    private static final String NAME = TickerViewData.class.getName();

    public TickerViewData() {
        super(Ticker.class);
    }

    @Override
    public String getName() {
        return NAME;
    }

}
