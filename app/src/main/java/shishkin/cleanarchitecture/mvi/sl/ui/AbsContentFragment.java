package shishkin.cleanarchitecture.mvi.sl.ui;

import android.content.Intent;


import java.util.List;


import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import shishkin.cleanarchitecture.mvi.sl.SL;
import shishkin.cleanarchitecture.mvi.sl.ViewUnion;
import shishkin.cleanarchitecture.mvi.sl.ViewUnionImpl;
import shishkin.cleanarchitecture.mvi.sl.model.AbsModel;

@SuppressWarnings("unused")
public abstract class AbsContentFragment<M extends AbsModel> extends AbsFragment<M> implements
        OnBackPressListener {

    /**
     * @return true if fragment itself or its children correctly handle back press event.
     */
    @Override
    public boolean onBackPressed() {
        boolean backPressedHandled = false;

        final FragmentManager fragmentManager = getChildFragmentManager();
        final List<Fragment> children = fragmentManager.getFragments();
        if (children != null) {
            for (final Fragment child : children) {
                if (child != null && OnBackPressListener.class.isInstance(child) && child.getUserVisibleHint()) {
                    backPressedHandled |= ((OnBackPressListener) child).onBackPressed();
                }
            }
        }
        return backPressedHandled;
    }

    /**
     * Dispatches result of activity launch to child fragments.
     */
    @Override
    public void onActivityResult(final int requestCode, final int resultCode, final Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);

        final List<Fragment> fragments = getChildFragmentManager().getFragments();
        if (fragments != null) {
            for (final Fragment child : fragments) {
                if (child != null) {
                    child.onActivityResult(requestCode, resultCode, intent);
                }
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        final List<Fragment> fragments = getChildFragmentManager().getFragments();
        if (fragments != null) {
            for (final Fragment child : fragments) {
                if (child != null) {
                    child.onRequestPermissionsResult(requestCode, permissions, grantResults);
                }
            }
        }
    }

    @Override
    public boolean isTop() {
        return false;
    }

    @Override
    public void exit() {
        ((ViewUnion) SL.getInstance().get(ViewUnionImpl.NAME)).back();
    }

}
