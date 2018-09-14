package shishkin.cleanarchitecture.mvi.sl.model;

import shishkin.cleanarchitecture.mvi.sl.data.Result;
import shishkin.cleanarchitecture.mvi.sl.presenter.Presenter;
import shishkin.cleanarchitecture.mvi.sl.state.Stateable;
import shishkin.cleanarchitecture.mvi.sl.state.ViewStateObserver;

/**
 * Created by Shishkin on 27.11.2017.
 */

@SuppressWarnings("unused")
public abstract class AbsModel implements Model {

    private ModelView mModelView = null;
    private Presenter mPresenter;
    private ModelInteractor mInteractor;
    private ModelRouter mRouter;
    private ViewStateObserver mLifecycle = new ViewStateObserver(this);

    public AbsModel(ModelView view) {
        mModelView = view;
        mModelView.addStateObserver(this);
    }

    @Override
    public <V> V getView() {
        return (V) mModelView;
    }

    @Override
    public void setView(ModelView view) {
        mModelView = view;
    }

    @Override
    public void setPresenter(Presenter presenter) {
        mPresenter = presenter;
        if (presenter != null) {
            addStateObserver(presenter);
        }
    }

    @Override
    public void setInteractor(ModelInteractor interactor) {
        mInteractor = interactor;
    }

    @Override
    public void setRouter(ModelRouter router) {
        mRouter = router;
    }

    @Override
    public <C> C getPresenter() {
        return (C) mPresenter;
    }

    @Override
    public <C> C getInteractor() {
        return (C) mInteractor;
    }

    @Override
    public <C> C getRouter() {
        return (C) mRouter;
    }

    @Override
    public Result<Boolean> validateExt() {
        if (mModelView != null) {
            return mModelView.validateExt();
        }
        return new Result<>(false);
    }

    @Override
    public boolean validate() {
        return validateExt().getData();
    }

    @Override
    public void addStateObserver(final Stateable stateable) {
        if (mModelView != null) {
            mModelView.addStateObserver(stateable);
        }
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
    }

    @Override
    public void onResumeView() {
    }

    @Override
    public void onPauseView() {
    }

    @Override
    public void onDestroyView() {
    }

    @Override
    public void onActivityCreated() {
    }

}
