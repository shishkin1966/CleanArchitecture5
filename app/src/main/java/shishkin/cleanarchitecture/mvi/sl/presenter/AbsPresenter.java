package shishkin.cleanarchitecture.mvi.sl.presenter;

import java.util.List;


import shishkin.cleanarchitecture.mvi.common.utils.StringUtils;
import shishkin.cleanarchitecture.mvi.sl.MessagerUnion;
import shishkin.cleanarchitecture.mvi.sl.MessagerUnionImpl;
import shishkin.cleanarchitecture.mvi.sl.PresenterUnionImpl;
import shishkin.cleanarchitecture.mvi.sl.SL;
import shishkin.cleanarchitecture.mvi.sl.data.Result;
import shishkin.cleanarchitecture.mvi.sl.state.ViewStateObserver;

@SuppressWarnings("unused")
public abstract class AbsPresenter<M> implements Presenter<M> {

    private M mModel = null;
    private ViewStateObserver mLifecycle = new ViewStateObserver(this);

    public AbsPresenter() {
    }

    public AbsPresenter(M model) {
        setModel(model);
    }

    @Override
    public String getPasport() {
        return getName();
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
        SL.getInstance().register(this);

        onStart();

        ((MessagerUnion) SL.getInstance().get(MessagerUnionImpl.NAME)).readMail(this);
    }

    @Override
    public void onResumeView() {
        ((MessagerUnion) SL.getInstance().get(MessagerUnionImpl.NAME)).readMail(this);
    }

    @Override
    public void onPauseView() {
    }

    @Override
    public void onDestroyView() {
        SL.getInstance().unregister(this);

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
                MessagerUnionImpl.NAME
        );
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
