package shishkin.cleanarchitecture.mvi.app.request;

import java.util.List;


import shishkin.cleanarchitecture.mvi.app.SLUtil;
import shishkin.cleanarchitecture.mvi.app.data.Ticker;
import shishkin.cleanarchitecture.mvi.sl.request.AbsRequest;

public class SaveTickerRequest extends AbsRequest {

    public static final String NAME = SaveTickerRequest.class.getName();

    private List<Ticker> mData;
    private String mName;

    public SaveTickerRequest(String name, List<Ticker> list) {
        mName = name;
        mData = list;
    }

    @Override
    public boolean isDistinct() {
        return true;
    }

    @Override
    public void run() {
        if (mData != null) {
            SLUtil.getStorageSpecialist().put(mName, mData);
        }
    }

    @Override
    public String getName() {
        return NAME;
    }
}
