package shishkin.cleanarchitecture.mvi.sl.request;

import androidx.annotation.NonNull;
import shishkin.cleanarchitecture.mvi.sl.task.RequestThreadPoolExecutor;

@SuppressWarnings("unused")
public abstract class AbsRequest implements Request {

    private int mRank = Rank.MIDDLE_RANK;
    private boolean isCanceled = false;
    private int mId = 0;

    public AbsRequest() {
    }

    public AbsRequest(int rank) {
        mRank = rank;
    }

    @Override
    public int getRank() {
        return mRank;
    }

    @Override
    public void setRank(int rank) {
        this.mRank = rank;
    }


    @Override
    public boolean isCancelled() {
        return isCanceled;
    }

    @Override
    public void setCanceled() {
        isCanceled = true;
    }

    @Override
    public boolean validate() {
        return (!isCancelled());
    }

    public int getId() {
        return mId;
    }

    public Request setId(int id) {
        mId = id;
        return this;
    }

    @Override
    public int compareTo(@NonNull Request o) {
        return o.getRank() - this.getRank();
    }

    @Override
    public String getName() {
        return this.getClass().getName();
    }

    @Override
    public String getPasport() {
        return getName();
    }

    @Override
    public int getAction(Request oldRequest) {
        return RequestThreadPoolExecutor.ACTION_DELETE;
    }

}
