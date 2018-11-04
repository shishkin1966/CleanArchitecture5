package shishkin.cleanarchitecture.mvi.sl.mail;

import shishkin.cleanarchitecture.mvi.common.utils.ApplicationUtils;
import shishkin.cleanarchitecture.mvi.sl.MessagerSubscriber;
import shishkin.cleanarchitecture.mvi.sl.data.Result;
import shishkin.cleanarchitecture.mvi.sl.request.ResponseListener;

/**
 * Created by Shishkin on 25.01.2018.
 */

public class ResultMail extends AbsMail {

    private Result mResult;
    private String mName;

    public ResultMail(final String address, final Result result) {
        super(address);

        mResult = result;
    }

    public ResultMail(ResultMail mail, final Result result, final String name) {
        super(mail);

        mResult = result;
        mName = name;
    }

    @Override
    public String getName() {
        return mName;
    }

    public ResultMail setName(String name) {
        this.mName = name;
        return this;
    }

    @Override
    public void read(MessagerSubscriber subscriber) {
        if (ResponseListener.class.isInstance(subscriber)) {
            ApplicationUtils.runOnUiThread(() -> ((ResponseListener) subscriber).response(mResult));
        }
    }

    @Override
    public Mail copy() {
        return new ResultMail(this, mResult, mName);
    }

    @Override
    public boolean isCheckDublicate() {
        return true;
    }


}
