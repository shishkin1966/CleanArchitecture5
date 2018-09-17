package shishkin.cleanarchitecture.mvi.sl.request;

import shishkin.cleanarchitecture.mvi.sl.SLUtil;
import shishkin.cleanarchitecture.mvi.sl.data.ExtError;
import shishkin.cleanarchitecture.mvi.sl.data.Result;
import shishkin.cleanarchitecture.mvi.sl.mail.ResultMail;

/**
 * Created by Shishkin on 04.12.2017.
 */

@SuppressWarnings("unused")
public abstract class AbsResultMailRequest<T> extends AbsRequest {

    private String mListener;
    private T mData;
    private ExtError mError;

    public AbsResultMailRequest(final String listener) {
        mListener = listener;
    }

    @Override
    public boolean validate() {
        return (!isCancelled());
    }

    public void response() {
        if (validate()) {
            SLUtil.addMail(new ResultMail(mListener, new Result().setName(getName()).setData(getData()).setError(getError())));
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
