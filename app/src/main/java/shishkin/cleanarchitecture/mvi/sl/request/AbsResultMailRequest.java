package shishkin.cleanarchitecture.mvi.sl.request;


import shishkin.cleanarchitecture.mvi.common.utils.StringUtils;

/**
 * Created by Shishkin on 04.12.2017.
 */

public abstract class AbsResultMailRequest extends AbsRequest implements ResultMailRequest {

    private String mListener;

    public AbsResultMailRequest(final String listener) {
        mListener = listener;
    }

    @Override
    public String getListener() {
        return mListener;
    }

    @Override
    public boolean validate() {
        return (!StringUtils.isNullOrEmpty(mListener) && !isCancelled());
    }

}
