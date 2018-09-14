package shishkin.cleanarchitecture.mvi.sl.event;

import android.support.v4.app.Fragment;


import shishkin.cleanarchitecture.mvi.R;

/**
 * Событие - выполнить команду "показать указанный фрагмент"
 */
@SuppressWarnings("unused")
public class ShowFragmentEvent extends AbsEvent {
    private Fragment mFragment;

    private boolean mAllowingStateLoss = false;
    private boolean mAddToBackStack = true;
    private boolean mClearBackStack = false;
    private boolean mAnimate = true;

    private int mIdRes = R.id.content;

    public ShowFragmentEvent(final Fragment fragment) {
        mFragment = fragment;
    }

    public ShowFragmentEvent(final Fragment fragment, final boolean allowingStateLoss) {
        mFragment = fragment;
        mAllowingStateLoss = allowingStateLoss;
    }

    public ShowFragmentEvent(final Fragment fragment, final boolean allowingStateLoss, final boolean addToBackStack, final boolean clearBackStack, final boolean animate) {
        mFragment = fragment;
        mAllowingStateLoss = allowingStateLoss;
        mAddToBackStack = addToBackStack;
        mClearBackStack = clearBackStack;
        mAnimate = animate;
    }

    public ShowFragmentEvent(final Fragment fragment, int idRes, final boolean allowingStateLoss, final boolean addToBackStack, final boolean clearBackStack, final boolean animate) {
        mFragment = fragment;
        mIdRes = idRes;
        mAllowingStateLoss = allowingStateLoss;
        mAddToBackStack = addToBackStack;
        mClearBackStack = clearBackStack;
        mAnimate = animate;
    }

    public Fragment getFragment() {
        return mFragment;
    }

    public boolean isAllowingStateLoss() {
        return mAllowingStateLoss;
    }

    public boolean isAddToBackStack() {
        return mAddToBackStack;
    }

    public boolean isClearBackStack() {
        return mClearBackStack;
    }

    public boolean isAnimate() {
        return mAnimate;
    }

    public ShowFragmentEvent setAllowingStateLoss(boolean value) {
        mAllowingStateLoss = value;
        return this;
    }

    public ShowFragmentEvent setAddToBackStack(boolean value) {
        mAddToBackStack = value;
        return this;
    }

    public ShowFragmentEvent setClearBackStack(boolean value) {
        mClearBackStack = value;
        return this;
    }

    public int getIdRes() {
        return mIdRes;
    }

    public ShowFragmentEvent setIdRes(final int idRes) {
        mIdRes = idRes;
        return this;
    }
}
