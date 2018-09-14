package shishkin.cleanarchitecture.mvi.sl.data;

import android.support.annotation.Nullable;


import shishkin.cleanarchitecture.mvi.common.utils.StringUtils;

@SuppressWarnings("unused")
public class ExtError {
    private StringBuilder mErrorText = new StringBuilder();
    private String mSender = null;

    @Nullable
    public String getErrorText() {
        if (mErrorText.length() == 0) {
            return null;
        }
        return mErrorText.toString();
    }

    public ExtError addError(final String sender, final String error) {
        mSender = sender;
        addError(error);
        return this;
    }

    private void addError(final String error) {
        if (!StringUtils.isNullOrEmpty(error)) {
            if (mErrorText.length() > 0) {
                mErrorText.append("\n");
            }
            mErrorText.append(error);
        }
    }

    public ExtError addError(final String sender, final Exception e) {
        if (e != null) {
            mSender = sender;
            addError(e.getMessage());
        }
        return this;
    }

    public boolean hasError() {
        return mErrorText.length() > 0;
    }

    public String getSender() {
        return mSender;
    }

    public ExtError setSender(final String sender) {
        mSender = sender;
        return this;
    }
}
