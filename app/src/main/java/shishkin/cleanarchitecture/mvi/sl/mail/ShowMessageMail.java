package shishkin.cleanarchitecture.mvi.sl.mail;

import android.widget.Toast;


import shishkin.cleanarchitecture.mvi.common.utils.ApplicationUtils;
import shishkin.cleanarchitecture.mvi.sl.MailSubscriber;
import shishkin.cleanarchitecture.mvi.sl.event.ShowMessageEvent;
import shishkin.cleanarchitecture.mvi.sl.ui.Messager;

public class ShowMessageMail extends AbsMail {

    private static final String NAME = ShowMessageMail.class.getName();

    private String mMessage;
    private int mDuration = Toast.LENGTH_LONG;
    private int mType = ApplicationUtils.MESSAGE_TYPE_INFO;

    public ShowMessageMail(final String address, final String message) {
        super(address);

        mMessage = message;
    }

    public ShowMessageMail setType(int type) {
        mType = type;
        return this;
    }

    public ShowMessageMail setDuration(int duration) {
        this.mDuration = duration;
        return this;
    }

    public String getMessage() {
        return mMessage;
    }

    public int getDuration() {
        return mDuration;
    }

    public int getType() {
        return mType;
    }

    @Override
    public void read(MailSubscriber subscriber) {
        if (subscriber instanceof Messager) {
            ((Messager) subscriber).showMessage(new ShowMessageEvent(mMessage)
                    .setDuration(mDuration)
                    .setType(mType));
        }
    }

    @Override
    public Mail copy() {
        return new ShowMessageMail(getAddress(), getMessage())
                .setDuration(getDuration())
                .setType(getType());
    }

    @Override
    public String getName() {
        return NAME;
    }

}
