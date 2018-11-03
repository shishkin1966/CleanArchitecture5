package shishkin.cleanarchitecture.mvi.common.recyclerview;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.ActionMode;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

/**
 * Represents a contextual mode of the user interface. Action modes can be used to provide
 * alternative interaction modes and replace parts of the normal UI until finished.
 * Examples of good action modes include text selection and contextual actions.
 * <div class="special reference">
 * <p>
 * <h3>Developer Guides</h3>
 * <p>For information about how to provide contextual actions with {@code ActionMode},
 * read the <a href="{@docRoot}guide/topics/ui/menus.html#context-menu">Menus</a>
 * developer guide.</p>
 * <p>
 * </div>
 */
public abstract class ActionModeCompat {

    /**
     * Creates ActionModeCompat for {@link Fragment}.
     * Notice that fragment should be attached to the {@link AppCompatActivity}.
     */
    @NonNull
    public static ActionModeCompat from(@NonNull final Fragment fragment) {
        return new FragmentActionModeCompat(fragment);
    }

    /**
     * Creates ActionModeCompat for {@link AppCompatActivity}.
     */
    @NonNull
    public static ActionModeCompat from(@NonNull final AppCompatActivity activity) {
        return new ActivityActionModeCompat(activity);
    }

    private ActionModeCompat() {
    }

    /**
     * Start an action mode.
     *
     * @param callback Callback that will manage lifecycle events for this context mode.
     * @return The ContextMode that was started, or null if it was canceled.
     */
    @Nullable
    public abstract ActionMode startActionMode(@NonNull final ActionMode.Callback callback);

    private static class FragmentActionModeCompat extends ActionModeCompat {

        @NonNull
        private final Fragment mFragment;

        /* package */ FragmentActionModeCompat(@NonNull final Fragment fragment) {
            super();
            mFragment = fragment;
        }

        @Nullable
        @Override
        public ActionMode startActionMode(@NonNull final ActionMode.Callback callback) {
            final FragmentActivity activity = mFragment.getActivity();
            if (AppCompatActivity.class.isInstance(activity)) {
                final AppCompatActivity appCompatActivity = (AppCompatActivity) activity;
                return appCompatActivity.startSupportActionMode(callback);
            } else {
                throw new IllegalStateException("Your activity should extend AppCompatActivity.");
            }
        }

    }

    private static class ActivityActionModeCompat extends ActionModeCompat {

        @NonNull
        private final AppCompatActivity mActivity;

        /* package */  ActivityActionModeCompat(@NonNull final AppCompatActivity activity) {
            super();
            mActivity = activity;
        }

        @Nullable
        @Override
        public ActionMode startActionMode(@NonNull final ActionMode.Callback callback) {
            return mActivity.startSupportActionMode(callback);
        }

    }

}
