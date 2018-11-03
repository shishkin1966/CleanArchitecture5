package shishkin.cleanarchitecture.mvi.sl;

import java.util.List;


import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
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

    /**
     * Показать фрагмент
     */
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
            tag = fragment.getClass().getName();
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

    /**
     * Переключиться на фрагмент верхнего уровня
     */
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

    /**
     * Переключиться на фрагмент
     */
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

    /**
     * Отработать нажатие на BackPressed
     */
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

    /**
     * Проверить наличие фрагмента верхнего уровня
     */
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

    /**
     * Проверить наличие фрагмента
     */
    public static boolean hasFragment(final AbsActivity activity, String name) {
        if (activity == null || StringUtils.isNullOrEmpty(name)) return false;

        final FragmentManager fm = activity.getSupportFragmentManager();
        final int backStackEntryCount = fm.getBackStackEntryCount();
        if (backStackEntryCount > 0) {
            for (int i = backStackEntryCount - 1; i >= 0; i--) {
                final FragmentManager.BackStackEntry backStackEntry = fm
                        .getBackStackEntryAt(i);
                final Fragment fragment = fm.findFragmentByTag(backStackEntry.getName());
                String tag;
                if (fragment instanceof AbsFragment) {
                    tag = ((AbsFragment) fragment).getName();
                } else {
                    tag = fragment.getClass().getName();
                }
                if (name.equals(tag)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Проверить является ли фрагмент текущим
     */
    public static boolean isCurrentFragment(final AbsActivity activity, String name) {
        if (activity == null || StringUtils.isNullOrEmpty(name)) return false;

        final FragmentManager fm = activity.getSupportFragmentManager();
        final int backStackEntryCount = fm.getBackStackEntryCount();
        if (backStackEntryCount > 0) {
            final FragmentManager.BackStackEntry backStackEntry = fm
                    .getBackStackEntryAt(backStackEntryCount - 1);
            final Fragment fragment = fm.findFragmentByTag(backStackEntry.getName());
            String tag;
            if (fragment instanceof AbsFragment) {
                tag = ((AbsFragment) fragment).getName();
            } else {
                tag = fragment.getClass().getName();
            }
            if (name.equals(tag)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Проверить BackStack пуст
     */
    public static boolean isBackStackEmpty(final AbsActivity activity) {
        return activity.getSupportFragmentManager().getBackStackEntryCount() == 0;
    }

    /**
     * Получить количество записей в BackStack
     */
    public static int getBackStackEntryCount(final AbsActivity activity) {
        return activity.getSupportFragmentManager().getBackStackEntryCount();
    }

    /**
     * Получить фрагмент
     */
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

    /**
     * Получить текущий фрагмент
     */
    public static <F> F getCurrentFragment(final AbsActivity activity) {
        if (activity == null) return null;
        if (!activity.validate()) return null;

        final FragmentManager fm = activity.getSupportFragmentManager();
        final int backStackEntryCount = fm.getBackStackEntryCount();
        if (backStackEntryCount > 0) {
            final FragmentManager.BackStackEntry backStackEntry = fm
                    .getBackStackEntryAt(backStackEntryCount - 1);
            return (F) fm.findFragmentByTag(backStackEntry.getName());
        }
        return null;
    }

    /**
     * Очистить BackStack
     */
    public static void clearBackStack(final AbsActivity activity) {
        final FragmentManager fm = activity.getSupportFragmentManager();
        for (int i = 0; i < fm.getBackStackEntryCount(); i++) {
            fm.popBackStack();
        }
    }

}
