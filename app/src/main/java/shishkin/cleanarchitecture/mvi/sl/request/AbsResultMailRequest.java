package shishkin.cleanarchitecture.mvi.sl.request;


import java.util.ArrayList;
import java.util.List;


import shishkin.cleanarchitecture.mvi.app.SLUtil;
import shishkin.cleanarchitecture.mvi.common.utils.ApplicationUtils;
import shishkin.cleanarchitecture.mvi.common.utils.StringUtils;
import shishkin.cleanarchitecture.mvi.sl.data.ExtError;
import shishkin.cleanarchitecture.mvi.sl.data.Result;
import shishkin.cleanarchitecture.mvi.sl.mail.ResultMail;

/**
 * Created by Shishkin on 04.12.2017.
 */

public abstract class AbsResultMailRequest<T> extends AbsRequest implements ResultMailRequest<T> {

    private String owner;
    private List<String> copyTo = new ArrayList<>();
    private T data;
    private ExtError error;

    public AbsResultMailRequest(final String owner) {
        this.owner = owner;
    }

    @Override
    public String getOwnerName() {
        return owner;
    }

    @Override
    public List<String> getCopyTo() {
        return copyTo;
    }

    @Override
    public void setCopyTo(List<String> copyTo) {
        this.copyTo = copyTo;
    }

    @Override
    public boolean validate() {
        return (!StringUtils.isNullOrEmpty(owner) && !isCancelled());
    }

    @Override
    public T getData() {
        return data;
    }

    @Override
    public void setData(T data) {
        this.data = data;
    }

    @Override
    public ExtError getError() {
        return error;
    }

    @Override
    public void setError(ExtError error) {
        this.error = error;
    }

    @Override
    public void response() {
        if (validate()) {
            final Result<T> result = new Result().setName(getName()).setData(getData()).setError(getError());
            ApplicationUtils.runOnUiThread(() -> SLUtil.addMail(new ResultMail(getOwnerName(), result).setCopyTo(getCopyTo())));
        }
    }

}
