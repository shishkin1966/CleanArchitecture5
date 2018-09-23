package shishkin.cleanarchitecture.mvi.sl.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.View;


import java.util.List;


import shishkin.cleanarchitecture.mvi.sl.ActivityUnion;
import shishkin.cleanarchitecture.mvi.sl.ActivityUnionImpl;
import shishkin.cleanarchitecture.mvi.sl.SL;
import shishkin.cleanarchitecture.mvi.sl.model.AbsModel;

@SuppressWarnings("unused")
public abstract class AbsContentFragment<M extends AbsModel> extends AbsFragment<M> implements
        OnBackPressListener {

    @Override
    public void onViewCreated(final View view, @Nullable final Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

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

    public void onRequestPermissions(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
    }

    @Override
    public boolean isTop() {
        return false;
    }

    @Override
    public void exit() {
        ((ActivityUnion) SL.getInstance().get(ActivityUnionImpl.NAME)).back();
    }

}
