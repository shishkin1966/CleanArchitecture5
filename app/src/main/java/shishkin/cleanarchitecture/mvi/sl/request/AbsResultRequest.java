package shishkin.cleanarchitecture.mvi.sl.request;

import java.lang.ref.WeakReference;


import shishkin.cleanarchitecture.mvi.common.utils.ApplicationUtils;
import shishkin.cleanarchitecture.mvi.sl.data.ExtError;
import shishkin.cleanarchitecture.mvi.sl.data.Result;

/**
 * Created by Shishkin on 04.12.2017.
 */

public abstract class AbsResultRequest<T> extends AbsRequest implements ResultRequest {

    private WeakReference<ResponseListener> mListener;
    private T mData;
    private ExtError mError;

    public AbsResultRequest(final ResponseListener listener) {
        mListener = new WeakReference<>(listener);
    }

    @Override
    public ResponseListener getListener() {
        if (mListener != null) {
            return mListener.get();
        }
        return null;
    }

    @Override
    public boolean validate() {
        return (mListener != null && mListener.get() != null && mListener.get().validate() && !isCancelled());
    }

    public void response() {
        if (validate()) {
            ApplicationUtils.runOnUiThread(() -> getListener().response(new Result().setName(getName()).setData(getData()).setError(getError())));
        }
    }

    public T getData() {
        return mData;
    }

    public void setData(T data) {
        this.mData = data;
    }

    public ExtError getError() {
        return mError;
    }

    public void setError(ExtError error) {
        this.mError = error;
    }


}
