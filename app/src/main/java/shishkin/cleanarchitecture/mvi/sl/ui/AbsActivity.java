package shishkin.cleanarchitecture.mvi.sl.ui;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.view.Surface;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;


import java.util.List;


import shishkin.cleanarchitecture.mvi.R;
import shishkin.cleanarchitecture.mvi.common.KeyboardRunnable;
import shishkin.cleanarchitecture.mvi.common.utils.ApplicationUtils;
import shishkin.cleanarchitecture.mvi.common.utils.PreferencesUtils;
import shishkin.cleanarchitecture.mvi.common.utils.StringUtils;
import shishkin.cleanarchitecture.mvi.common.utils.ViewUtils;
import shishkin.cleanarchitecture.mvi.sl.ActivityUnionImpl;
import shishkin.cleanarchitecture.mvi.sl.BackStack;
import shishkin.cleanarchitecture.mvi.sl.SL;
import shishkin.cleanarchitecture.mvi.sl.data.Result;
import shishkin.cleanarchitecture.mvi.sl.model.Model;
import shishkin.cleanarchitecture.mvi.sl.model.ModelView;
import shishkin.cleanarchitecture.mvi.sl.state.StateObservable;
import shishkin.cleanarchitecture.mvi.sl.state.Stateable;
import shishkin.cleanarchitecture.mvi.sl.state.ViewStateObserver;

public abstract class AbsActivity<M extends Model> extends AppCompatActivity
        implements IActivity, ModelView {

    private StateObservable mStateObservable = new StateObservable(ViewStateObserver.STATE_CREATE);
    private M mModel;

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

    @Override
    protected void onCreate(@Nullable final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);

        mStateObservable.setState(ViewStateObserver.STATE_CREATE);

        SL.getInstance().register(this);
    }

    public abstract M createModel();

    @Override
    public <V extends View> V findView(@IdRes final int id) {
        return ViewUtils.findView(this, id);
    }

    @Override
    protected void onStart() {
        super.onStart();

        setModel(createModel());

        mStateObservable.setState(ViewStateObserver.STATE_READY);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        mStateObservable.setState(ViewStateObserver.STATE_DESTROY);
        mStateObservable.clear();

        SL.getInstance().unregister(this);
    }

    @Override
    protected void onResume() {
        super.onResume();

        mStateObservable.setState(ViewStateObserver.STATE_RESUME);

        SL.getInstance().setCurrentSubscriber(this);
    }

    @Override
    protected void onPause() {
        super.onPause();

        mStateObservable.setState(ViewStateObserver.STATE_PAUSE);
    }

    @Override
    public List<String> getSpecialistSubscription() {
        return StringUtils.arrayToList(
                ActivityUnionImpl.NAME
        );
    }

    @Override
    public void clearBackStack() {
        BackStack.clearBackStack(this);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        for (int i = 0; i < permissions.length; i++) {
            PreferencesUtils.putInt(this, permissions[i], grantResults[i]);
            if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                onPermisionGranted(permissions[i]);
            } else if (grantResults[i] == PackageManager.PERMISSION_DENIED) {
                onPermisionDenied(permissions[i]);
            }
        }
    }

    @Override
    public int getState() {
        return mStateObservable.getState();
    }

    @Override
    public void setState(int state) {
    }

    @Override
    public void lockOrientation() {
        if (!validate()) return;

        switch (((WindowManager) getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getRotation()) {
            // Portrait
            case Surface.ROTATION_0:
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                break;

            //Landscape
            case Surface.ROTATION_90:
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                break;

            // Reversed portrait
            case Surface.ROTATION_180:
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_REVERSE_PORTRAIT);
                break;

            // Reversed landscape
            case Surface.ROTATION_270:
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE);
                break;

            default:
                break;
        }
    }

    @Override
    public void lockOrientation(int orientation) {
        if (!validate()) return;

        setRequestedOrientation(orientation);
    }

    @Override
    public void unlockOrientation() {
        if (!validate()) return;

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
    }

    @Override
    public Result<Boolean> validateExt() {
        return new Result<>(getState() != ViewStateObserver.STATE_DESTROY && !isFinishing());
    }

    @Override
    public boolean validate() {
        return validateExt().getData();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void setStatusBarColor(final int color) {
        if (!validate()) return;

        if (ApplicationUtils.hasLollipop()) {
            getWindow().setStatusBarColor(color);
        }
    }

    @Override
    public void exit() {
        if (ApplicationUtils.hasLollipop()) {
            super.finishAndRemoveTask();
        } else if (ApplicationUtils.hasJellyBean()) {
            super.finishAffinity();
        } else {
            super.finish();
        }
    }

    @Override
    public void onPermisionGranted(final String permission) {
    }

    @Override
    public void onPermisionDenied(final String permission) {
    }

    @Override
    public View getRootView() {
        final View view = findView(R.id.root);
        if (view != null) {
            return view;
        }

        return ((ViewGroup) findViewById(android.R.id.content)).getChildAt(0);
    }

    @Override
    public AbsActivity getActivity() {
        return this;
    }

    @Override
    public void setFullScreen() {
        if (!validate()) return;

        if (!ApplicationUtils.hasLollipop()) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }
    }

    @Override
    public void addStateObserver(final Stateable stateable) {
        mStateObservable.addObserver(stateable);
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
    public void hideKeyboard() {
        if (!validate()) return;

        final InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
        if (imm != null) {
            View view = getCurrentFocus();
            if (view == null) {
                view = getRootView();
            }
            if (view != null) {
                getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }
        }
    }

    @Override
    public void showKeyboard(View view) {
        if (!validate()) return;

        if (view != null) {
            new KeyboardRunnable(this, view).run();
        }
    }

    @Override
    public void showKeyboard(View view, int mode) {
        if (!validate()) return;

        if (view != null) {
            new KeyboardRunnable(this, view, mode).run();
        }
    }

    /**
     * Called when the activity has detected the user's press of the back
     * key. The default implementation simply finishes the current activity,
     * but you can override this to do whatever you want.
     */
    public void onActivityBackPressed() {
        super.onBackPressed();
    }
}


