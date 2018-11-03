package shishkin.cleanarchitecture.mvi.sl.request;

import java.lang.ref.WeakReference;


import androidx.annotation.NonNull;
import shishkin.cleanarchitecture.mvi.common.utils.ApplicationUtils;
import shishkin.cleanarchitecture.mvi.sl.data.Result;

/**
 * Created by Shishkin on 04.12.2017.
 */

public abstract class AbsResultRequest<T> extends AbsResultMailRequest<T> implements ResultRequest<T> {

    private WeakReference<ResponseListener> ref;

    public AbsResultRequest(@NonNull final ResponseListener listener) {
        super(listener.getName());
        this.ref = new WeakReference<>(listener);
    }

    @Override
    public ResponseListener getOwner() {
        if (ref != null) {
            return ref.get();
        }
        return null;
    }

    @Override
    public boolean validate() {
        return (ref != null && ref.get() != null && ref.get().validate() && !isCancelled());
    }

    @Override
    public void response() {
        if (validate()) {
            ApplicationUtils.runOnUiThread(() -> getOwner().response(new Result().setName(getName()).setData(getData()).setError(getError())));
        }
    }
}
