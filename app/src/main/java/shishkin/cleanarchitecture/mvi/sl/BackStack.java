package shishkin.cleanarchitecture.mvi.sl;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;


import java.util.List;


import shishkin.cleanarchitecture.mvi.common.utils.SafeUtils;
import shishkin.cleanarchitecture.mvi.common.utils.StringUtils;
import shishkin.cleanarchitecture.mvi.sl.ui.AbsActivity;
import shishkin.cleanarchitecture.mvi.sl.ui.AbsFragment;
import shishkin.cleanarchitecture.mvi.sl.ui.OnBackPressListener;

/**
 * Created by Shishkin on 14.02.2018.
 */

public class BackStack {
    private BackStack() {
    }

    public static void showFragment(final AbsActivity activity, final int idRes, final Fragment fragment, final boolean addToBackStack,
                                    final boolean clearBackStack,
                                    final boolean animate, final boolean allowingStateLoss) {
        if (activity == null) return;
        if (!activity.validate()) {
            return;
        }

        String tag;
        final FragmentManager fm = activity.getSupportFragmentManager();
        if (Subscriber.class.isInstance(fragment)) {
            tag = ((Subscriber) fragment).getName();
        } else {
            tag = fragment.getClass().getSimpleName();
        }
        if (clearBackStack) {
            fm.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        }
        final FragmentTransaction ft = fm.beginTransaction();
        if (addToBackStack) {
            ft.addToBackStack(tag);
        }
        if (animate) {
            ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        }
        ft.replace(idRes, fragment, tag);
        if (allowingStateLoss) {
            ft.commitAllowingStateLoss();
        } else {
            ft.commit();
        }
    }

    public static void switchToTopFragment(final AbsActivity activity) {
        if (activity == null) return;
        if (!activity.validate()) {
            return;
        }

        final FragmentManager fm = activity.getSupportFragmentManager();
        final int backStackEntryCount = fm.getBackStackEntryCount();
        if (backStackEntryCount > 0) {
            for (int i = backStackEntryCount - 1; i >= 0; i--) {
                final FragmentManager.BackStackEntry backStackEntry = fm
                        .getBackStackEntryAt(i);
                final Fragment fragment = fm.findFragmentByTag(backStackEntry.getName());
                if (OnBackPressListener.class.isInstance(fragment)) {
                    if (((OnBackPressListener) fragment).isTop()) {
                        fm.popBackStackImmediate(backStackEntry.getName(), 0);
                    }
                }
            }
        }
    }

    public static boolean switchToFragment(final AbsActivity activity, final String name) {
        if (activity == null) return false;
        if (!activity.validate()) {
            return false;
        }

        if (StringUtils.isNullOrEmpty(name)) {
            return false;
        }

        final FragmentManager fm = activity.getSupportFragmentManager();
        final List<Fragment> list = fm.getFragments();
        for (Fragment fragment : list) {
            if (AbsFragment.class.isInstance(fragment)) {
                AbsFragment abstractFragment = (AbsFragment) fragment;
                if (name.equalsIgnoreCase(abstractFragment.getName())) {
                    if (fm.popBackStackImmediate(abstractFragment.getName(), 0)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public static void onBackPressed(final AbsActivity activity) {
        if (activity == null) return;
        if (!activity.validate()) {
            return;
        }

        final FragmentManager fm = activity.getSupportFragmentManager();
        final int backStackEntryCount = fm.getBackStackEntryCount();
        if (backStackEntryCount > 0) {
            final FragmentManager.BackStackEntry backStackEntry = fm
                    .getBackStackEntryAt(backStackEntryCount - 1);
            final Fragment fragment = fm.findFragmentByTag(backStackEntry.getName());
            final OnBackPressListener onBackPressListener;
            if (OnBackPressListener.class.isInstance(fragment)) {
                onBackPressListener = SafeUtils.cast(fragment);
            } else {
                onBackPressListener = null;
            }

            if (onBackPressListener == null) {
                activity.onActivityBackPressed();
            } else {
                if (!onBackPressListener.onBackPressed()) {
                    activity.onActivityBackPressed();
                }
            }
        } else {
            activity.exit();
        }
    }

    public static boolean hasTopFragment(final AbsActivity activity) {
        final FragmentManager fm = activity.getSupportFragmentManager();
        final int backStackEntryCount = fm.getBackStackEntryCount();
        if (backStackEntryCount > 0) {
            for (int i = backStackEntryCount - 1; i >= 0; i--) {
                final FragmentManager.BackStackEntry backStackEntry = fm
                        .getBackStackEntryAt(i);
                final Fragment fragment = fm.findFragmentByTag(backStackEntry.getName());
                if (OnBackPressListener.class.isInstance(fragment)) {
                    if (((OnBackPressListener) fragment).isTop()) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public static boolean isBackStackEmpty(final AbsActivity activity) {
        return activity.getSupportFragmentManager().getBackStackEntryCount() == 0;
    }

    public static int getBackStackEntryCount(final AbsActivity activity) {
        return activity.getSupportFragmentManager().getBackStackEntryCount();
    }

    public static <F> F getFragment(final AbsActivity activity, final Class<F> cls, final int id) {
        if (activity == null) return null;
        if (!activity.validate()) return null;

        F f = null;
        try {
            f = cls.cast(activity.getSupportFragmentManager().findFragmentById(id));
        } catch (final ClassCastException e) {
        }
        return f;
    }

    public static void clearBackStack(final AbsActivity activity) {
        final FragmentManager fm = activity.getSupportFragmentManager();
        for (int i = 0; i < fm.getBackStackEntryCount(); i++) {
            fm.popBackStack();
        }
    }

}
