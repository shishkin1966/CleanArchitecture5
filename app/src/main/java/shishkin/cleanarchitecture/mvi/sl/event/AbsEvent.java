package shishkin.cleanarchitecture.mvi.sl.event;

import shishkin.cleanarchitecture.mvi.sl.data.ExtError;

public abstract class AbsEvent implements Event {

    private int mId = -1;
    private ExtError mError;
    private String mSender = null;

    public AbsEvent() {
    }

    public AbsEvent(int id) {
        mId = id;
    }

    @Override
    public ExtError getError() {
        return mError;
    }

    @Override
    public Event setError(final ExtError error) {
        this.mError = error;
        return this;
    }

    @Override
    public boolean hasError() {
        if (mError == null) {
            return false;
        }
        return mError.hasError();
    }

    @Override
    public int getId() {
        return mId;
    }

    @Override
    public Event setId(final int id) {
        mId = id;
        return this;
    }

    @Override
    public String getSender() {
        return mSender;
    }

    @Override
    public Event setSender(final String sender) {
        mSender = sender;
        return this;
    }

}
