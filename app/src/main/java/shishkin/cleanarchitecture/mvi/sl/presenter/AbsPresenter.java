package shishkin.cleanarchitecture.mvi.sl.presenter;

import android.os.Bundle;


import java.util.List;


import shishkin.cleanarchitecture.mvi.common.utils.StringUtils;
import shishkin.cleanarchitecture.mvi.sl.MailUnionImpl;
import shishkin.cleanarchitecture.mvi.sl.PresenterUnion;
import shishkin.cleanarchitecture.mvi.sl.PresenterUnionImpl;
import shishkin.cleanarchitecture.mvi.sl.SLUtil;
import shishkin.cleanarchitecture.mvi.sl.data.Result;
import shishkin.cleanarchitecture.mvi.sl.state.ViewStateObserver;

@SuppressWarnings("unused")
public abstract class AbsPresenter<M> implements Presenter<M> {

    private M mModel = null;
    private ViewStateObserver mLifecycle = new ViewStateObserver(this);
    private boolean mLostStateData = false;

    public AbsPresenter() {
    }

    public AbsPresenter(M model) {
        setModel(model);
    }

    @Override
    public int getState() {
        return mLifecycle.getState();
    }

    @Override
    public void setState(final int state) {
        mLifecycle.setState(state);
    }

    @Override
    public void onCreateView() {
    }

    @Override
    public void onReadyView() {
        SLUtil.register(this);

        onStart();
    }

    @Override
    public void onResumeView() {
        SLUtil.readMail(this);
    }

    @Override
    public void onPauseView() {
    }

    @Override
    public void onDestroyView() {
        SLUtil.unregister(this);

        final PresenterUnion union = SLUtil.getPresenterUnion();
        if (union != null) {
            if (!mLostStateData) {
                union.saveStateData(this, getStateData());
            } else {
                union.clearStateData(this);
            }
        }

        onStop();
    }

    @Override
    public void setModel(final M model) {
        mModel = model;
    }

    @Override
    public M getModel() {
        return mModel;
    }

    @Override
    public boolean validate() {
        return validateExt().getData();
    }

    @Override
    public Result<Boolean> validateExt() {
        return new Result<>(mLifecycle.getState() != ViewStateObserver.STATE_DESTROY);
    }

    @Override
    public List<String> getSpecialistSubscription() {
        return StringUtils.arrayToList(
                PresenterUnionImpl.NAME,
                MailUnionImpl.NAME
        );
    }

    @Override
    public Bundle getStateData() {
        return null;
    }

    public void setLostStateData(boolean lostStateData) {
        mLostStateData = lostStateData;
    }

    @Override
    public void onStart() {
    }

    @Override
    public void onStop() {
    }

    @Override
    public void onModelUpdated() {
    }

    @Override
    public void onActivityCreated() {
    }


}
