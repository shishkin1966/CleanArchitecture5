package shishkin.cleanarchitecture.mvi.sl.model;

import android.app.Activity;


import androidx.fragment.app.Fragment;
import shishkin.cleanarchitecture.mvi.sl.ApplicationSpecialistImpl;
import shishkin.cleanarchitecture.mvi.sl.ErrorSpecialistImpl;
import shishkin.cleanarchitecture.mvi.sl.SL;
import shishkin.cleanarchitecture.mvi.sl.ViewUnion;
import shishkin.cleanarchitecture.mvi.sl.ViewUnionImpl;
import shishkin.cleanarchitecture.mvi.sl.event.ShowFragmentEvent;
import shishkin.cleanarchitecture.mvi.sl.ui.AbsContentActivity;
import shishkin.cleanarchitecture.mvi.sl.ui.AbsFragment;

public abstract class AbsModelRouter implements ModelRouter {

    private static final String NAME = AbsModelRouter.class.getName();

    private AbsModel mModel;

    public AbsModelRouter(AbsModel model) {
        mModel = model;
    }

    @Override
    public void finishApplication() {
        ApplicationSpecialistImpl.getInstance().stop();
    }

    @Override
    public void switchToTop() {
        final ModelView view = getModel().getView();
        if (AbsContentActivity.class.isInstance(view)) {
            final AbsContentActivity activity = (AbsContentActivity) view;
            activity.switchToTopFragment();
            return;
        } else if (AbsFragment.class.isInstance(view)) {
            final Activity activity = ((AbsFragment) view).getActivity();
            if (activity != null && AbsContentActivity.class.isInstance(activity)) {
                ((AbsContentActivity) activity).switchToTopFragment();
                return;
            }
        }
        if (((ViewUnion) SL.getInstance().get(ViewUnionImpl.NAME)).hasTop()) {
            ((ViewUnion) SL.getInstance().get(ViewUnionImpl.NAME)).switchToTopFragment();
        } else {
            showMainFragment();
        }
    }

    public abstract void showMainFragment();

    public AbsModel getModel() {
        return mModel;
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
        final ModelView view = getModel().getView();
        if (AbsContentActivity.class.isInstance(view)) {
            final AbsContentActivity activity = (AbsContentActivity) view;
            activity.showFragment(fragment, addToBackStack, clearBackStack, animate, allowingStateLoss);
            return;
        } else if (AbsFragment.class.isInstance(view)) {
            final Activity activity = ((AbsFragment) view).getActivity();
            if (activity != null && AbsContentActivity.class.isInstance(activity)) {
                ((AbsContentActivity) activity).showFragment(fragment, addToBackStack, clearBackStack, animate, allowingStateLoss);
                return;
            } else {
                ErrorSpecialistImpl.getInstance().onError(NAME, "Fragment is not attached to activity", false);
            }
        } else {
            ErrorSpecialistImpl.getInstance().onError(NAME, "Object is not AbsContentActivity and AbsFragment", false);
        }
        ((ViewUnion) SL.getInstance().get(ViewUnionImpl.NAME)).showFragment(new ShowFragmentEvent(fragment, allowingStateLoss, addToBackStack, clearBackStack, animate));
    }
}
