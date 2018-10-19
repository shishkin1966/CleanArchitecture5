package shishkin.cleanarchitecture.mvi.common.utils;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.AttrRes;
import android.support.annotation.ColorRes;
import android.support.annotation.DrawableRes;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.Size;
import android.support.graphics.drawable.VectorDrawableCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.res.ResourcesCompat;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;


import java.util.Locale;

/**
 * {@code ViewUtils} contains static methods which operate on {@code View}.
 */
@SuppressWarnings("unused")
public class ViewUtils {

    private ViewUtils() {
    }

    /**
     * Finds a view that was identified by the id attribute from the XML that
     * was processed in {@link Activity#onCreate(Bundle)}.
     *
     * @return The casted view if found or null otherwise.
     */
    @Nullable
    public static <V extends View> V findView(@NonNull final Activity activity, @IdRes final int id) {
        return SafeUtils.cast(activity.findViewById(id));
    }

    /**
     * Look for a child view with the given id.  If provided view has the given
     * id, return this view.
     *
     * @param root The view to search for.
     * @param id   The id to search for.
     * @return The view that has the given id in the hierarchy or null
     */
    @Nullable
    public static <V extends View> V findView(@NonNull final View root, @IdRes final int id) {
        return SafeUtils.cast(root.findViewById(id));
    }

    /**
     * Get Drawable by id
     *
     * @return The drawable
     */
    public static Drawable getDrawable(final Context context, final int id) {
        return ResourcesCompat.getDrawable(context.getResources(), id, ApplicationUtils.hasMarshmallow() ? context.getTheme() : null);
    }

    public static VectorDrawableCompat getVectorDrawable(final Context context, final int id, Resources.Theme theme) {
        return VectorDrawableCompat.create(context.getResources(), id, theme);
    }

    /**
     * Get int Color by id
     *
     * @return The color
     */
    public static int getColor(final Context context, final int id) {
        return ResourcesCompat.getColor(context.getResources(), id, ApplicationUtils.hasMarshmallow() ? context.getTheme() : null);
    }

    /**
     * Get Smartphone screen size in inch
     *
     * @return The smartphone screen size
     */
    public static double diagonalInch(final Context context) {
        final int widthPixels = context.getResources().getDisplayMetrics().widthPixels;
        final int heightPixels = context.getResources().getDisplayMetrics().heightPixels;

        final float widthDpi = context.getResources().getDisplayMetrics().xdpi;
        final float heightDpi = context.getResources().getDisplayMetrics().ydpi;

        final float widthInches = widthPixels / widthDpi;
        final float heightInches = heightPixels / heightDpi;

        final double diagonal = Math.sqrt((widthInches * widthInches) + (heightInches * heightInches));
        return Math.floor(diagonal + 0.5);
    }

    public static boolean isPhone(final Context context) {
        final double diagonalInches = diagonalInch(context);
        return diagonalInches < 7;
    }

    public static boolean is6inchPhone(final Context context) {
        final double diagonalInches = diagonalInch(context);
        return diagonalInches >= 6 && diagonalInches < 7;
    }

    public static boolean is7inchTablet(final Context context) {
        final double diagonalInches = diagonalInch(context);
        return diagonalInches >= 7 && diagonalInches < 9;
    }

    public static boolean is10inchTablet(final Context context) {
        final double diagonalInches = diagonalInch(context);
        return diagonalInches >= 9;
    }

    public static String getLanguage() {
        return Locale.getDefault().getLanguage();
    }

    public static int getOrientation(final Context context) {
        return context.getResources().getConfiguration().orientation;
    }

    public static boolean isLandscapeOrientation(final Context context) {
        return (Configuration.ORIENTATION_LANDSCAPE == getOrientation(context));
    }

    public static boolean isPortraitOrientation(final Context context) {
        return (Configuration.ORIENTATION_PORTRAIT == getOrientation(context));
    }

    public static void setStatusBarColor(Activity activity, int color) {
        if (activity == null) return;

        if (ApplicationUtils.hasLollipop()) {
            final Window window = activity.getWindow();

            // clear FLAG_TRANSLUCENT_STATUS flag:
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

            // add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

            // finally change the color
            window.setStatusBarColor(color);
        }
    }

    /**
     * Set the background to a given Drawable, or remove the background.
     * If the background has padding, this View's padding is set to the background's padding.
     * However, when a background is removed, this View's padding isn't touched.
     * If setting the padding is desired, please use {@link View#setPadding(int, int, int, int)}.
     *
     * @param v        The view to set background to.
     * @param drawable The Drawable to use as the background, or null to remove the background.
     */
    public static void setBackground(@NonNull final View v, @Nullable final Drawable drawable) {
        EXT_IMPL.setBackground(v, drawable);
    }

    /**
     * Sets the Drawables (if any) to appear to the left of the text. Use null if you do not want
     * a Drawable there. The Drawables' bounds will be set to their intrinsic bounds.
     *
     * @param v     The view to set drawables to.
     * @param start Drawable.
     */
    public static void setCompoundDrawableStart(@NonNull final TextView v, @Nullable final Drawable start) {
        EXT_IMPL.setCompoundDrawableStart(v, start);
    }

    /**
     * Sets the Drawables (if any) to appear above the text. Use null if you do not want
     * a Drawable there. The Drawables' bounds will be set to their intrinsic bounds.
     *
     * @param v   The view to set drawables to.
     * @param top Drawable.
     */
    public static void setCompoundDrawableTop(@NonNull final TextView v, @Nullable final Drawable top) {
        EXT_IMPL.setCompoundDrawableTop(v, top);
    }

    /**
     * Sets the Drawables (if any) to appear to the right of the text. * Use null if you do not want
     * a Drawable there. The Drawables' bounds will be set to their intrinsic bounds.
     *
     * @param v   The view to set drawables to.
     * @param end Drawable.
     */
    public static void setCompoundDrawableEnd(@NonNull final TextView v, @Nullable final Drawable end) {
        EXT_IMPL.setCompoundDrawableEnd(v, end);
    }

    /**
     * Sets the Drawables (if any) to appear below the text. Use null if you do not want
     * a Drawable there. The Drawables' bounds will be set to their intrinsic bounds.
     *
     * @param v      The view to set drawables to.
     * @param bottom Drawable.
     */
    public static void setCompoundDrawableBottom(@NonNull final TextView v,
                                                 @Nullable final Drawable bottom) {
        EXT_IMPL.setCompoundDrawableBottom(v, bottom);
    }

    /**
     * Sets the Drawables (if any) to appear to the left of and to the right of the text.
     * Use null if you do not want a Drawable there. The Drawables' bounds will be set
     * to their intrinsic bounds.
     *
     * @param v     The view to set drawables to.
     * @param start Drawable.
     * @param end   Drawable.
     */
    public static void setCompoundDrawablesHorizontal(@NonNull final TextView v,
                                                      @Nullable final Drawable start,
                                                      @Nullable final Drawable end) {
        EXT_IMPL.setCompoundDrawablesHorizontal(v, start, end);
    }

    /**
     * Sets the Drawables (if any) to appear above and below the text. Use null if you do not want
     * a Drawable there. The Drawables' bounds will be set to their intrinsic bounds.
     *
     * @param v      The view to set drawables to.
     * @param top    Drawable.
     * @param bottom Drawable.
     */
    public static void setCompoundDrawablesVertical(@NonNull final TextView v,
                                                    @Nullable final Drawable top,
                                                    @Nullable final Drawable bottom) {
        EXT_IMPL.setCompoundDrawablesVertical(v, top, bottom);
    }


    interface ViewCompatExtImpl {

        void setBackground(@NonNull final View v, @Nullable final Drawable drawable);

        void setCompoundDrawableStart(@NonNull final TextView v, @Nullable final Drawable start);

        void setCompoundDrawableTop(@NonNull final TextView v, @Nullable final Drawable top);

        void setCompoundDrawableEnd(@NonNull final TextView v, @Nullable final Drawable end);

        void setCompoundDrawableBottom(@NonNull final TextView v, @Nullable final Drawable bottom);

        void setCompoundDrawablesHorizontal(@NonNull final TextView v, @Nullable final Drawable start,
                                            @Nullable final Drawable end);

        void setCompoundDrawablesVertical(@NonNull final TextView v, @Nullable final Drawable top,
                                          @Nullable final Drawable bottom);

    }

    private static final ViewCompatExtImpl EXT_IMPL;

    static {
        if (ApplicationUtils.hasJellyBeanMR1()) {
            EXT_IMPL = new JellyBeanMR1ViewCompatExtImpl();
        } else if (ApplicationUtils.hasJellyBean()) {
            EXT_IMPL = new JellyBeanViewCompatExtImpl();
        } else {
            EXT_IMPL = new BaseViewCompatExtImpl();
        }
    }

    @SuppressWarnings("deprecation")
    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    private static class BaseViewCompatExtImpl implements ViewCompatExtImpl {

        @Override
        public void setBackground(@NonNull final View v, @Nullable final Drawable drawable) {
            v.setBackgroundDrawable(drawable);
        }

        @Override
        public void setCompoundDrawableStart(@NonNull final TextView v, @Nullable final Drawable start) {
            @Size(4) final Drawable[] drawables = v.getCompoundDrawables();
            v.setCompoundDrawablesWithIntrinsicBounds(start, drawables[1], drawables[2], drawables[3]);
        }

        @Override
        public void setCompoundDrawableTop(@NonNull final TextView v, @Nullable final Drawable top) {
            @Size(4) final Drawable[] drawables = v.getCompoundDrawables();
            v.setCompoundDrawablesWithIntrinsicBounds(drawables[0], top, drawables[2], drawables[3]);
        }

        @Override
        public void setCompoundDrawableEnd(@NonNull final TextView v, @Nullable final Drawable end) {
            @Size(4) final Drawable[] drawables = v.getCompoundDrawables();
            v.setCompoundDrawablesWithIntrinsicBounds(drawables[0], drawables[1], end, drawables[3]);
        }

        @Override
        public void setCompoundDrawableBottom(@NonNull final TextView v, @Nullable final Drawable bottom) {
            @Size(4) final Drawable[] drawables = v.getCompoundDrawables();
            v.setCompoundDrawablesWithIntrinsicBounds(drawables[0], drawables[1], drawables[2], bottom);
        }

        @Override
        public void setCompoundDrawablesHorizontal(@NonNull final TextView v, @Nullable final Drawable start,
                                                   @Nullable final Drawable end) {
            @Size(4) final Drawable[] drawables = v.getCompoundDrawables();
            v.setCompoundDrawablesWithIntrinsicBounds(start, drawables[1], end, drawables[3]);
        }

        @Override
        public void setCompoundDrawablesVertical(@NonNull final TextView v, @Nullable final Drawable top,
                                                 @Nullable final Drawable bottom) {
            @Size(4) final Drawable[] drawables = v.getCompoundDrawables();
            v.setCompoundDrawablesWithIntrinsicBounds(drawables[0], top, drawables[2], bottom);
        }

    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private static class JellyBeanViewCompatExtImpl extends BaseViewCompatExtImpl {

        @Override
        public void setBackground(@NonNull final View v, @Nullable final Drawable drawable) {
            v.setBackground(drawable);
        }

    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
    private static class JellyBeanMR1ViewCompatExtImpl extends JellyBeanViewCompatExtImpl {

        @Override
        public void setCompoundDrawableStart(@NonNull final TextView v, @Nullable final Drawable start) {
            @Size(4) final Drawable[] drawables = v.getCompoundDrawables();
            v.setCompoundDrawablesRelativeWithIntrinsicBounds(start, drawables[1], drawables[2], drawables[3]);
        }

        @Override
        public void setCompoundDrawableTop(@NonNull final TextView v, @Nullable final Drawable top) {
            @Size(4) final Drawable[] drawables = v.getCompoundDrawables();
            v.setCompoundDrawablesRelativeWithIntrinsicBounds(drawables[0], top, drawables[2], drawables[3]);
        }

        @Override
        public void setCompoundDrawableEnd(@NonNull final TextView v, @Nullable final Drawable end) {
            @Size(4) final Drawable[] drawables = v.getCompoundDrawables();
            v.setCompoundDrawablesRelativeWithIntrinsicBounds(drawables[0], drawables[1], end, drawables[3]);
        }

        @Override
        public void setCompoundDrawableBottom(@NonNull final TextView v, @Nullable final Drawable bottom) {
            @Size(4) final Drawable[] drawables = v.getCompoundDrawables();
            v.setCompoundDrawablesRelativeWithIntrinsicBounds(drawables[0], drawables[1], drawables[2], bottom);
        }

        @Override
        public void setCompoundDrawablesHorizontal(@NonNull final TextView v, @Nullable final Drawable start,
                                                   @Nullable final Drawable end) {
            @Size(4) final Drawable[] drawables = v.getCompoundDrawables();
            v.setCompoundDrawablesRelativeWithIntrinsicBounds(start, drawables[1], end, drawables[3]);
        }

        @Override
        public void setCompoundDrawablesVertical(@NonNull final TextView v, @Nullable final Drawable top,
                                                 @Nullable final Drawable bottom) {
            @Size(4) final Drawable[] drawables = v.getCompoundDrawables();
            v.setCompoundDrawablesRelativeWithIntrinsicBounds(drawables[0], top, drawables[2], bottom);
        }
    }

    /**
     * Retrieve the value of an drawable attribute in the Theme or default drawable resource.
     */
    @DrawableRes
    public static int resolveDrawableAttr(@NonNull final Resources.Theme theme, @AttrRes final int attr,
                                          @DrawableRes final int defaultDrawableRes) {
        return resolveAttr(theme, attr, defaultDrawableRes);
    }

    /**
     * Retrieve the value of an color attribute in the Theme or default color resource.
     */
    @ColorRes
    public static int resolveColorAttr(@NonNull final Resources.Theme theme, @AttrRes final int attr,
                                       @ColorRes final int defaultColorRes) {
        return resolveAttr(theme, attr, defaultColorRes);
    }

    private static int resolveAttr(@NonNull final Resources.Theme theme, @AttrRes final int attr,
                                   final int defaultDrawableRes) {
        final TypedValue outValue = new TypedValue();
        return (theme.resolveAttribute(attr, outValue, true) ? outValue.resourceId : defaultDrawableRes);
    }

    public static int dp2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    public static int px2dp(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    public static int sp2px(Context context, float spValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }

    public static int px2sp(Context context, float pxValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (pxValue / fontScale + 0.5f);
    }

    public static int getScreenWidth(Context context) {
        final WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        final Display display = wm.getDefaultDisplay();
        final DisplayMetrics metrics = new DisplayMetrics();
        display.getMetrics(metrics);
        return metrics.widthPixels;
    }

    public static int getScreenHeight(Context context) {
        final WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        final Display display = wm.getDefaultDisplay();
        final DisplayMetrics metrics = new DisplayMetrics();
        display.getMetrics(metrics);
        return metrics.heightPixels;
    }

    public static void enableDisableView(final View view, boolean enabled) {
        view.setEnabled(enabled);
        if (ViewGroup.class.isInstance(view)) {
            final ViewGroup group = (ViewGroup) view;

            for (int i = 0; i < group.getChildCount(); i++) {
                enableDisableView(group.getChildAt(i), enabled);
            }
        }
    }

    public static void enableDisableTouchView(final View view, final boolean enabled) {
        view.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return !enabled;
            }
        });
        if (ViewGroup.class.isInstance(view)) {
            final ViewGroup group = (ViewGroup) view;

            for (int i = 0; i < group.getChildCount(); i++) {
                enableDisableTouchView(group.getChildAt(i), enabled);
            }
        }
    }

    public static int getDimensionPx(Context context, int resId) {
        return (int) context.getResources().getDimension(resId);
    }

    public static int getDimensionDp(Context context, int resId) {
        return (int) (context.getResources().getDimension(resId) / context.getResources().getDisplayMetrics().density);
    }

    public static int getDimensionSp(Context context, int resId) {
        return (int) (context.getResources().getDimension(resId) / context.getResources().getDisplayMetrics().scaledDensity);
    }

    public static BitmapDescriptor generateBitmapDescriptorFromRes(
            Context context, int resId) {
        final Drawable drawable = ContextCompat.getDrawable(context, resId);
        drawable.setBounds(
                0,
                0,
                drawable.getIntrinsicWidth(),
                drawable.getIntrinsicHeight());
        final Bitmap bitmap = Bitmap.createBitmap(
                drawable.getIntrinsicWidth(),
                drawable.getIntrinsicHeight(),
                Bitmap.Config.ARGB_8888);
        final Canvas canvas = new Canvas(bitmap);
        drawable.draw(canvas);
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }

}



