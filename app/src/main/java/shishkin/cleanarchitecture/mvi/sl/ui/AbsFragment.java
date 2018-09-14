package shishkin.cleanarchitecture.mvi.sl.ui;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.View;


import shishkin.cleanarchitecture.mvi.R;
import shishkin.cleanarchitecture.mvi.common.utils.ViewUtils;
import shishkin.cleanarchitecture.mvi.sl.ActivityUnion;
import shishkin.cleanarchitecture.mvi.sl.SLUtil;
import shishkin.cleanarchitecture.mvi.sl.data.Result;
import shishkin.cleanarchitecture.mvi.sl.model.AbsModel;
import shishkin.cleanarchitecture.mvi.sl.model.Model;
import shishkin.cleanarchitecture.mvi.sl.model.ModelView;
import shishkin.cleanarchitecture.mvi.sl.state.StateObservable;
import shishkin.cleanarchitecture.mvi.sl.state.Stateable;
import shishkin.cleanarchitecture.mvi.sl.state.ViewStateObserver;

@SuppressWarnings("unused")
public abstract class AbsFragment<M extends AbsModel> extends Fragment
        implements IFragment, ModelView {

    private StateObservable mStateObservable = new StateObservable(ViewStateObserver.STATE_CREATE);
    private M mModel;

    @Override
    public M getModel() {
        return mModel;
    }

    @Override
    public void setModel(Model model) {
        if (!validate()) return;

        mModel = (M) model;
    }

    @Override
    public <V extends View> V findView(@IdRes final int id) {
        final View root = getView();
        if (root != null) {
            return ViewUtils.findView(root, id);
        }
        return null;
    }

    @Override
    public void onViewCreated(final View view, @Nullable final Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mStateObservable.setState(ViewStateObserver.STATE_READY);
    }

    @Override
    public void onPause() {
        super.onPause();

        mStateObservable.setState(ViewStateObserver.STATE_PAUSE);
    }

    @Override
    public void onResume() {
        super.onResume();

        mStateObservable.setState(ViewStateObserver.STATE_RESUME);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mStateObservable.setState(ViewStateObserver.STATE_ACTIVITY_CREATED);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        mStateObservable.setState(ViewStateObserver.STATE_DESTROY);
        mStateObservable.clear();
    }

    @Override
    public IActivity getActivitySubscriber() {
        if (validate()) {
            final Activity activity = getActivity();
            if (activity != null && IActivity.class.isInstance(activity)) {
                return (IActivity) activity;
            }
            final ActivityUnion union = SLUtil.getActivityUnion();
            if (union != null) {
                return union.getCurrentSubscriber();
            }
        }
        return null;
    }

    @Override
    public int getState() {
        return mStateObservable.getState();
    }

    @Override
    public void setState(int state) {
    }

    @Override
    public void exit() {
    }

    @Override
    public Result<Boolean> validateExt() {
        return new Result<>(getState() != ViewStateObserver.STATE_DESTROY);
    }

    @Override
    public boolean validate() {
        return validateExt().getData();
    }

    @Override
    public void showProgressBar() {
        if (validate()) {
            final View view = findView(R.id.progressBar);
            if (view != null) {
                view.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    public void hideProgressBar() {
        if (validate()) {
            final View view = findView(R.id.progressBar);
            if (view != null) {
                view.setVisibility(View.INVISIBLE);
            }
        }
    }

    @Override
    public View getRootView() {
        final View view = findView(R.id.root);
        if (view != null) {
            return view;
        }
        return getView();
    }

    @Override
    public void addStateObserver(final Stateable stateable) {
        mStateObservable.addObserver(stateable);
    }
}
