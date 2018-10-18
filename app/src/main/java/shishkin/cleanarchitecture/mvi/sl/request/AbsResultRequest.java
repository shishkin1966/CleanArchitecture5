package shishkin.cleanarchitecture.mvi.sl.request;

import java.lang.ref.WeakReference;


import shishkin.cleanarchitecture.mvi.common.utils.ApplicationUtils;
import shishkin.cleanarchitecture.mvi.sl.data.ExtError;
import shishkin.cleanarchitecture.mvi.sl.data.Result;

/**
 * Created by Shishkin on 04.12.2017.
 */

public abstract class AbsResultRequest<T> extends AbsRequest implements ResultRequest {

    private WeakReference<ResponseListener> listener;
    private T data;
    private ExtError error;
    private String listenerName;

    public AbsResultRequest(final ResponseListener listener) {
        if (listener != null) {
            this.listener = new WeakReference<>(listener);
            this.listenerName = listener.getName();
        }
    }

    @Override
    public ResponseListener getListener() {
        if (listener != null) {
            return listener.get();
        }
        return null;
    }

    @Override
    public boolean validate() {
        return (listener != null && listener.get() != null && listener.get().validate() && !isCancelled());
    }

    public void response() {
        if (validate()) {
            ApplicationUtils.runOnUiThread(() -> getListener().response(new Result().setName(getName()).setData(getData()).setError(getError())));
        }
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public ExtError getError() {
        return error;
    }

    public void setError(ExtError error) {
        this.error = error;
    }

    @Override
    public String getListenerName() {
        return listenerName;
    }
}
