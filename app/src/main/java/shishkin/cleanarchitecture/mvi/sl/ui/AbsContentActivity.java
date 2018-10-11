package shishkin.cleanarchitecture.mvi.sl.ui;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;


import shishkin.cleanarchitecture.mvi.sl.ActivityUnion;
import shishkin.cleanarchitecture.mvi.sl.ActivityUnionImpl;
import shishkin.cleanarchitecture.mvi.sl.BackStack;
import shishkin.cleanarchitecture.mvi.sl.Router;
import shishkin.cleanarchitecture.mvi.sl.SL;
import shishkin.cleanarchitecture.mvi.sl.model.AbsModel;

public abstract class AbsContentActivity<M extends AbsModel> extends AbsActivity<M>
        implements Router {

    @Override
    protected void onPause() {
        super.onPause();

        ((ActivityUnion) SL.getInstance().get(ActivityUnionImpl.NAME)).hideKeyboard();
    }

    @Override
    public void showFragment(final Fragment fragment) {
        showFragment(fragment, true, false, true, false);
    }

    @Override
    public void showFragment(final Fragment fragment, final boolean allowingStateLoss) {
        showFragment(fragment, true, false, true, allowingStateLoss);
    }

    @Override
    public void showFragment(final Fragment fragment, final boolean addToBackStack,
                             final boolean clearBackStack,
                             final boolean animate, final boolean allowingStateLoss) {

        BackStack.showFragment(this, getContentResId(), fragment, addToBackStack, clearBackStack, animate, allowingStateLoss);
    }

    @Override
    public boolean switchToFragment(@NonNull final String name) {
        return BackStack.switchToFragment(this, name);
    }


    /**
     * Take care of popping the fragment back stack or finishing the activity
     * as appropriate. Notice that you should add any {@link Fragment} that implements
     * {@link OnBackPressListener} to the back stack if you want {@link OnBackPressListener#onBackPressed()}
     * to be invoked.
     */
    @Override
    public void onBackPressed() {
        BackStack.onBackPressed(this);
    }

    @Override
    public void switchToTopFragment() {
        BackStack.switchToTopFragment(this);
    }

    @Override
    public boolean hasTopFragment() {
        return BackStack.hasTopFragment(this);
    }

    @Override
    public boolean isBackStackEmpty() {
        return BackStack.isBackStackEmpty(this);
    }

    @Override
    public int getBackStackEntryCount() {
        return BackStack.getBackStackEntryCount(this);
    }

    @Nullable
    @Override
    public <F> F getContentFragment(final Class<F> cls) {
        return getFragment(cls, getContentResId());
    }

    @Nullable
    @Override
    public <F> F getFragment(final Class<F> cls, final int id) {
        return BackStack.getFragment(this, cls, id);
    }

    @Override
    public void onPermisionGranted(final String permission) {
        super.onPermisionGranted(permission);

        final AbsFragment fragment = getContentFragment(AbsFragment.class);
        if (fragment != null) {
            fragment.onPermisionGranted(permission);
        }
    }

    @Override
    public void onPermisionDenied(final String permission) {
        super.onPermisionDenied(permission);

        final AbsFragment fragment = getContentFragment(AbsFragment.class);
        if (fragment != null) {
            fragment.onPermisionDenied(permission);
        }
    }


}