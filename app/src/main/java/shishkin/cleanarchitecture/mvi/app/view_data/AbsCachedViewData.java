package shishkin.cleanarchitecture.mvi.app.view_data;

import android.os.Parcelable;


import java.util.List;


import shishkin.cleanarchitecture.mvi.app.SLUtil;
import shishkin.cleanarchitecture.mvi.sl.request.Request;

public abstract class AbsCachedViewData<T extends Parcelable> extends AbsViewData<T>{
    private Class mClass;

    public AbsCachedViewData(Class cls) {
        mClass = cls;
    }

    @Override
    public List<T> getData() {
        if (super.getData() == null) {
            super.setData(SLUtil.getStorageSpecialist().getList(getName(), mClass));
        }
        return super.getData();
    }

    @Override
    public void setData(List<T> data) {
        super.setData(data);
        SLUtil.getRequestSpecialist().request(this, getRequest());
    }

    public abstract Request getRequest();

}
