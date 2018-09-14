package shishkin.cleanarchitecture.mvi.sl.ui;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;


import shishkin.cleanarchitecture.mvi.sl.BackStack;
import shishkin.cleanarchitecture.mvi.sl.Router;
import shishkin.cleanarchitecture.mvi.sl.SLUtil;
import shishkin.cleanarchitecture.mvi.sl.model.AbsModel;

public abstract class AbsContentActivity<M extends AbsModel> extends AbsActivity<M>
        implements ActivityResultListener, Router {

    private static final String NAME = AbsContentActivity.class.getName();

    @Override
    protected void onPause() {
        super.onPause();

        SLUtil.getActivityUnion().hideKeyboard();
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
    public abstract int getContentResId();

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

    /**
     * Called when an activity you launched exits, giving you the requestCode
     * you started it with, the resultCode it returned, and any additional
     * data from it.  The <var>resultCode</var> will be
     * {@link #RESULT_CANCELED} if the activity explicitly returned that,
     * didn't return any result, or crashed during its operation.
     * <p/>
     * <p>You will receive this call immediately before onResume() when your
     * activity is re-starting.
     *
     * @param requestCode The integer request code originally supplied to
     *                    startActivityForResult(), allowing you to identify who this
     *                    result came from.
     * @param resultCode  The integer result code returned by the child activity
     *                    through its setResult().
     * @param data        An Intent, which can return result data to the caller
     *                    (various data can be attached to Intent "extras").
     * @see #startActivityForResult
     * @see Fragment#startActivityForResult
     * @see #createPendingResult
     * @see #setResult(int)
     */
    @Override
    public void onActivityResult(final int requestCode, final int resultCode,
                                 final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        final AbsContentFragment fragment = getContentFragment(AbsContentFragment.class);
        if (fragment != null) {
            fragment.onActivityResult(requestCode, 0, data);
        }
    }

}