package shishkin.cleanarchitecture.mvi.app.request;

import java.util.List;


import shishkin.cleanarchitecture.mvi.app.SLUtil;
import shishkin.cleanarchitecture.mvi.app.data.Ticker;
import shishkin.cleanarchitecture.mvi.app.screen.fragment.digital_currencies.DigitalCurrenciesPresenter;
import shishkin.cleanarchitecture.mvi.sl.request.AbsRequest;

public class SaveTickerRequest extends AbsRequest {

    public static final String NAME = SaveTickerRequest.class.getName();

    private List<Ticker> mData;

    public SaveTickerRequest(List<Ticker> list) {
        mData = list;
    }

    @Override
    public boolean isDistinct() {
        return true;
    }

    @Override
    public void run() {
        if (mData != null) {
            SLUtil.getStorageSpecialist().put(DigitalCurrenciesPresenter.NAME, mData);
        }
    }

    @Override
    public String getName() {
        return NAME;
    }
}
