package shishkin.cleanarchitecture.mvi.sl;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.AppCompatButton;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;


import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


import es.dmoral.toasty.Toasty;
import shishkin.cleanarchitecture.mvi.common.BaseSnackbar;
import shishkin.cleanarchitecture.mvi.common.utils.ApplicationUtils;
import shishkin.cleanarchitecture.mvi.common.utils.SafeUtils;
import shishkin.cleanarchitecture.mvi.common.utils.StringUtils;
import shishkin.cleanarchitecture.mvi.sl.event.OnActionEvent;
import shishkin.cleanarchitecture.mvi.sl.event.ShowFragmentEvent;
import shishkin.cleanarchitecture.mvi.sl.event.ShowKeyboardEvent;
import shishkin.cleanarchitecture.mvi.sl.event.ShowMessageEvent;
import shishkin.cleanarchitecture.mvi.sl.event.StartActivityEvent;
import shishkin.cleanarchitecture.mvi.sl.event.StartActivityForResultEvent;
import shishkin.cleanarchitecture.mvi.sl.event.StartChooseActivityEvent;
import shishkin.cleanarchitecture.mvi.sl.ui.AbsActivity;
import shishkin.cleanarchitecture.mvi.sl.ui.AbsContentActivity;
import shishkin.cleanarchitecture.mvi.sl.ui.AbsContentFragment;
import shishkin.cleanarchitecture.mvi.sl.ui.IActivity;

/**
 * Объединение activities
 */
@SuppressWarnings("unused")
public class ActivityUnionImpl extends AbsUnion<IActivity> implements ActivityUnion {

    public static final String NAME = ActivityUnionImpl.class.getName();
    private List<WeakReference<IActivity>> mActivities = Collections.synchronizedList(new ArrayList<>());

    public ActivityUnionImpl() {
        super();
    }

    @Override
    public void register(final IActivity subscriber) {
        super.register(subscriber);

        for (int i = mActivities.size() - 1; i >= 0; i--) {
            if (mActivities.get(i) == null) {
                mActivities.remove(i);
                continue;
            }
            if (mActivities.get(i).get() == null) {
                mActivities.remove(i);
                continue;
            }

            if (subscriber != null) {
                if (mActivities.get(i).get().getName().equals(subscriber.getName())) {
                    mActivities.get(i).get().exit();
                    mActivities.remove(i);
                }
            }
        }

        if (subscriber != null) {
            mActivities.add(new WeakReference<>(subscriber));
        }
    }

    @Override
    public void unregister(final IActivity subscriber) {
        super.unregister(subscriber);

        for (int i = mActivities.size() - 1; i >= 0; i--) {
            if (mActivities.get(i) == null) {
                mActivities.remove(i);
                continue;
            }
            if (mActivities.get(i).get() == null) {
                mActivities.remove(i);
                continue;
            }

            if (subscriber != null) {
                if (mActivities.get(i).get().equals(subscriber)) {
                    mActivities.remove(i);
                }
            }
        }
    }

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public void showSnackbar(ShowMessageEvent event) {
        final IActivity subscriber = getCurrentSubscriber();
        View view = null;
        if (subscriber != null && subscriber.validate()) {
            if (AbsContentActivity.class.isInstance(subscriber)) {
                final AbsContentFragment fragment = SafeUtils.cast(((AbsContentActivity) subscriber).getContentFragment(AbsContentFragment.class));
                if (fragment != null) {
                    view = fragment.getRootView();
                }
            }
            if (view == null) {
                view = subscriber.getRootView();
            }
            if (view != null) {
                final String action = event.getAction();
                if (StringUtils.isNullOrEmpty(action)) {
                    BaseSnackbar.make(view, event.getMessage(), event
                            .getDuration(), event.getType())
                            .show();
                } else {
                    BaseSnackbar.make(view, event.getMessage(), event.getDuration(), event.getType())
                            .setAction(action, this::onSnackbarClick)
                            .show();
                }
            }
        }
    }

    private void onSnackbarClick(final View view) {
        String action = null;
        if (AppCompatButton.class.isInstance(view)) {
            action = ((AppCompatButton) view).getText().toString();
        } else if (Button.class.isInstance(view)) {
            action = ((Button) view).getText().toString();
        }
        if (!StringUtils.isNullOrEmpty(action)) {
            ((UseCasesSpecialist) SL.getInstance().get(UseCasesSpecialistImpl.NAME)).onAction(new OnActionEvent(action));
        }
    }

    @Override
    public void showToast(ShowMessageEvent event) {
        final IActivity activity = getCurrentSubscriber();
        if (activity == null || !activity.validate()) {
            return;
        }

        final int type = event.getType();
        final int duration = event.getDuration();
        final String message = event.getMessage();

        switch (type) {
            case ApplicationUtils.MESSAGE_TYPE_INFO:
                Toasty.info(activity.getActivity(), message, duration).show();
                break;

            case ApplicationUtils.MESSAGE_TYPE_ERROR:
                Toasty.error(activity.getActivity(), message, duration).show();
                break;

            case ApplicationUtils.MESSAGE_TYPE_WARNING:
                Toasty.warning(activity.getActivity(), message, duration).show();
                break;

            case ApplicationUtils.MESSAGE_TYPE_SUCCESS:
                Toasty.success(activity.getActivity(), message, duration).show();
                break;

            default:
                Toasty.info(activity.getActivity(), message, duration).show();
                break;

        }
    }

    @Override
    public void hideKeyboard() {
        final IActivity subscriber = getCurrentSubscriber();
        if (subscriber != null) {
            subscriber.hideKeyboard();
        }
    }

    @Override
    public void showKeyboard(ShowKeyboardEvent event) {
        final IActivity subscriber = getCurrentSubscriber();
        if (subscriber != null && subscriber.validate()) {
            subscriber.showKeyboard(event.getView());
        }
    }

    @Override
    public void showProgressBar() {
        final IActivity subscriber = getCurrentSubscriber();
        if (subscriber != null && subscriber.validate()) {
            if (AbsContentActivity.class.isInstance(subscriber)) {
                final AbsContentActivity activity = (AbsContentActivity) subscriber;
                final AbsContentFragment fragment = SafeUtils.cast(activity.getContentFragment(AbsContentFragment.class));
                if (fragment != null) {
                    fragment.showProgressBar();
                }
            }
        }
    }

    @Override
    public void hideProgressBar() {
        final IActivity subscriber = getCurrentSubscriber();
        if (subscriber != null) {
            if (AbsContentActivity.class.isInstance(subscriber)) {
                final AbsContentActivity activity = (AbsContentActivity) subscriber;
                final AbsContentFragment fragment = SafeUtils.cast(activity.getContentFragment(AbsContentFragment.class));
                if (fragment != null) {
                    fragment.hideProgressBar();
                }
            }
        }
    }

    @Override
    public void onFinishApplication() {
        for (WeakReference<IActivity> ref : mActivities) {
            if (ref != null && ref.get() != null) {
                ref.get().exit();
            }
        }
    }

    @Override
    public void onUnRegisterLastSubscriber() {
        if (ApplicationSpecialistImpl.getInstance().isFinished()) {
            if (ApplicationSpecialistImpl.getInstance().isKillOnFinish()) {
                android.os.Process.killProcess(android.os.Process.myPid());
            }
        }
    }

    @Override
    public <F> F getFragment(Class<F> cls, int id) {
        final AbsActivity activity = getActivity();
        if (activity != null) {
            return BackStack.getFragment(activity, cls, id);
        }
        return null;
    }

    @Override
    public boolean isBackStackEmpty() {
        final AbsActivity activity = getActivity();
        if (activity != null) {
            return BackStack.isBackStackEmpty(activity);
        }
        return true;
    }

    @Override
    public int getBackStackEntryCount() {
        final AbsActivity activity = getActivity();
        if (activity != null) {
            BackStack.getBackStackEntryCount(activity);
        }
        return 0;
    }

    @Override
    public void switchToTopFragment() {
        final AbsActivity activity = getActivity();
        if (activity != null) {
            BackStack.switchToTopFragment(activity);
        }
    }

    @Override
    public boolean hasTop() {
        final AbsActivity activity = getActivity();
        if (activity != null) {
            return BackStack.hasTopFragment(activity);
        }
        return false;
    }

    @Override
    public <C> C getActivity() {
        final IActivity subscriber = getCurrentSubscriber();
        if (subscriber != null && AbsActivity.class.isInstance(subscriber)) {
            return (C) subscriber;
        }
        return null;
    }

    @Override
    public LayoutInflater getInflater() {
        final AbsActivity activity = getActivity();
        if (activity != null) {
            return LayoutInflater.from(activity);
        }
        return null;
    }

    @Override
    public <C> C getActivity(final String name) {
        return getActivity(name, false);
    }

    @Override
    public <C> C getActivity(final String name, final boolean validate) {
        final IActivity subscriber = getSubscriber(name);
        if (subscriber != null && AbsActivity.class.isInstance(subscriber)) {
            if (!validate || (validate && subscriber.validate())) {
                return (C) subscriber;
            }
        }
        return null;
    }

    @Override
    public void startActivity(final StartActivityEvent event) {
        if (event != null) {
            final AbsActivity activity = getActivity();
            if (activity != null) {
                activity.startActivity(event.getIntent());
            }
        }
    }

    @Override
    public void startChooseActivity(final StartChooseActivityEvent event) {
        if (event != null) {
            final AbsActivity activity = getActivity();
            if (activity != null) {
                activity.startActivity(Intent.createChooser(event.getIntent(), event.getTitle()));
            }
        }
    }

    @Override
    public void startActivityForResult(final StartActivityForResultEvent event) {
        if (event != null) {
            final AbsActivity activity = getActivity();
            if (activity != null) {
                activity.startActivityForResult(event.getIntent(), event.getRequestCode());
            }
        }
    }

    @Override
    public void showFragment(ShowFragmentEvent event) {
        final AbsActivity activity = getActivity();
        if (activity != null) {
            BackStack.showFragment(activity, event.getIdRes(), event.getFragment(), event.isAddToBackStack(), event.isClearBackStack(), event.isAnimate(), event.isAllowingStateLoss());
        }
    }

    @Override
    public void switchToFragment(String name) {
        if (StringUtils.isNullOrEmpty(name)) return;

        final IActivity subscriber = getCurrentSubscriber();
        if (subscriber != null && subscriber instanceof AbsContentActivity) {
            ((AbsContentActivity) subscriber).switchToFragment(name);
        }
    }

    @Override
    public void back() {
        final IActivity subscriber = getCurrentSubscriber();
        if (subscriber != null && subscriber instanceof AbsActivity) {
            ((AbsActivity) subscriber).onBackPressed();
        }
    }

    @Override
    public void showActivity(AbsActivity activity) {
        if (activity == null) return;

        startActivity(new StartActivityEvent(new Intent(ApplicationSpecialistImpl.getInstance(), activity.getClass())));
    }

    @Override
    public int compareTo(@NonNull Object o) {
        return (ActivityUnion.class.isInstance(o)) ? 0 : 1;
    }

}
