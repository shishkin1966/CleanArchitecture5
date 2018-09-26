package shishkin.cleanarchitecture.mvi.app.request;

import android.os.Parcelable;


import java.util.List;


import shishkin.cleanarchitecture.mvi.app.SLUtil;
import shishkin.cleanarchitecture.mvi.sl.request.AbsRequest;

public class SaveViewDataRequest<T extends Parcelable> extends AbsRequest {

    private List<T> mData;
    private String mName;

    public SaveViewDataRequest(String name, List<T> list) {
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
}
