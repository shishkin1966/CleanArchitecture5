package shishkin.cleanarchitecture.mvi.sl.mail;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;


import java.util.LinkedList;
import java.util.List;


import shishkin.cleanarchitecture.mvi.common.utils.StringUtils;

public abstract class AbsMail implements Mail {

    @SerializedName("address")
    private String mAddress;

    @SerializedName("sender")
    private String mSender;

    @SerializedName("copyTo")
    private List<String> mCopyTo = new LinkedList<>();

    @SerializedName("id")
    private long mId = 0;

    @SerializedName("endTime")
    private long mEndTime = -1;

    public AbsMail(Mail mail) {
        if (mail != null) {
            mAddress = mail.getAddress();
            mSender = mail.getSender();
            mCopyTo = mail.getCopyTo();
            mId = mail.getId();
            mEndTime = mail.getEndTime();
        }
    }

    public AbsMail(final String address) {
        mAddress = address;
    }

    @Override
    public String getAddress() {
        return mAddress;
    }

    @Override
    public Mail setAddress(final String address) {
        this.mAddress = address;
        return this;
    }

    @Override
    public List<String> getCopyTo() {
        return mCopyTo;
    }

    @Override
    public Mail setCopyTo(final List<String> copyTo) {
        this.mCopyTo = copyTo;
        return this;
    }

    @Override
    public boolean contains(final String address) {
        if (StringUtils.isNullOrEmpty(address)) {
            return false;
        }

        if (address.equalsIgnoreCase(mAddress)) {
            return true;
        }

        for (String copyto : mCopyTo) {
            if (copyto.equalsIgnoreCase(address)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public Long getId() {
        return mId;
    }

    @Override
    public Mail setId(final Long id) {
        if (id != null) {
            this.mId = id;
        }
        return this;
    }

    @Override
    public long getEndTime() {
        return mEndTime;
    }

    @Override
    public Mail setEndTime(final long endTime) {
        mEndTime = endTime;
        return this;
    }

    @Override
    public boolean isCheckDublicate() {
        return false;
    }

    @Override
    public String getSender() {
        return mSender;
    }

    @Override
    public Mail setSender(final String sender) {
        this.mSender = sender;
        return this;
    }

    @Override
    public boolean isSticky() {
        return false;
    }

    @Override
    public Mail copy() {
        final Gson gson = new Gson();
        return gson.fromJson(gson.toJson(this), getClass());
    }

    @Override
    public String getPasport() {
        return getName();
    }

}
