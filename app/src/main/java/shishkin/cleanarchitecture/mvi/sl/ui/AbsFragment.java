package shishkin.cleanarchitecture.mvi.sl.ui;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;


import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import shishkin.cleanarchitecture.mvi.R;
import shishkin.cleanarchitecture.mvi.app.SLUtil;
import shishkin.cleanarchitecture.mvi.common.utils.ApplicationUtils;
import shishkin.cleanarchitecture.mvi.common.utils.Constant;
import shishkin.cleanarchitecture.mvi.common.utils.ViewUtils;
import shishkin.cleanarchitecture.mvi.sl.SL;
import shishkin.cleanarchitecture.mvi.sl.SpecialistSubscriber;
import shishkin.cleanarchitecture.mvi.sl.ViewUnion;
import shishkin.cleanarchitecture.mvi.sl.ViewUnionImpl;
import shishkin.cleanarchitecture.mvi.sl.data.Result;
import shishkin.cleanarchitecture.mvi.sl.event.ShowDialogEvent;
import shishkin.cleanarchitecture.mvi.sl.event.ShowMessageEvent;
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
    public String getPasport() {
        return getName();
    }

    @Override
    public M getModel() {
        if (mModel == null) {
            mModel = createModel();
        }
        return mModel;
    }

    @Override
    public void setModel(Model model) {
        if (!validate()) return;

        mModel = (M) model;
    }

    public abstract M createModel();

    @Override
    public <V extends View> V findView(@IdRes final int id) {
        final View root = getView();
        if (root != null) {
            return ViewUtils.findView(root, id);
        }
        return null;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setModel(createModel());

        mStateObservable.setState(ViewStateObserver.STATE_CREATE);

        if (SpecialistSubscriber.class.isInstance(this)) {
            SL.getInstance().register((SpecialistSubscriber) this);
        }
    }

    @Override
    public void onStart() {
        super.onStart();

        getModel().addStateObserver();

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

        if (SpecialistSubscriber.class.isInstance(this)) {
            SL.getInstance().unregister((SpecialistSubscriber) this);
        }
    }

    @Override
    public IActivity getActivitySubscriber() {
        if (validate()) {
            final Activity activity = getActivity();
            if (activity != null && IActivity.class.isInstance(activity)) {
                return (IActivity) activity;
            }
            final ViewUnion union = SL.getInstance().get(ViewUnionImpl.NAME);
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

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        for (int i = 0; i < permissions.length; i++) {
            if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                onPermisionGranted(permissions[i]);
            } else if (grantResults[i] == PackageManager.PERMISSION_DENIED) {
                onPermisionDenied(permissions[i]);
            }
        }
    }

    @Override
    public void onPermisionGranted(final String permission) {
    }

    @Override
    public void onPermisionDenied(final String permission) {
    }

    @Override
    public void grantPermission(String permission) {
        if (ApplicationUtils.hasMarshmallow()) {
            if (getState() != ViewStateObserver.STATE_CREATE && getState() != ViewStateObserver.STATE_DESTROY) {
                if (!ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), permission)) {
                    requestPermissions(new String[]{permission}, Constant.REQUEST_PERMISSIONS);
                }
            }
        }
    }

    @Override
    public void grantPermission(String listener, String permission, String helpMessage) {
        if (ApplicationUtils.hasMarshmallow()) {
            if (getState() != ViewStateObserver.STATE_CREATE && getState() != ViewStateObserver.STATE_DESTROY) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), permission)) {
                    SLUtil.getViewUnion().showDialog(new ShowDialogEvent(R.id.dialog_request_permissions, listener, null, helpMessage).setPositiveButton(R.string.setting).setNegativeButton(R.string.cancel).setCancelable(false));
                } else {
                    requestPermissions(new String[]{permission}, Constant.REQUEST_PERMISSIONS);
                }
            }
        }
    }

    @Override
    public void showMessage(ShowMessageEvent event) {
        if (event == null) return;

        ApplicationUtils.showToast(event.getMessage(), event.getDuration(), event.getType());
    }

}


