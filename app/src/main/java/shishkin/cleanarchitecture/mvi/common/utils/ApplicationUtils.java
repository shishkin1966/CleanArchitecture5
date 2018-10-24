package shishkin.cleanarchitecture.mvi.common.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.view.menu.MenuPopupHelper;
import android.support.v7.widget.PopupMenu;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.andrognito.flashbar.Flashbar;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;


import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;


import es.dmoral.toasty.Toasty;
import shishkin.cleanarchitecture.mvi.R;
import shishkin.cleanarchitecture.mvi.common.BaseSnackbar;
import shishkin.cleanarchitecture.mvi.sl.ApplicationSpecialistImpl;

@SuppressWarnings("unused")
public class ApplicationUtils {

    private static final String NAME = ApplicationUtils.class.getName();

    public static final int REQUEST_PERMISSIONS = 10000;
    public static final int REQUEST_GOOGLE_PLAY_SERVICES = 10001;
    public static final int MESSAGE_TYPE_INFO = 0;
    public static final int MESSAGE_TYPE_ERROR = 1;
    public static final int MESSAGE_TYPE_WARNING = 2;
    public static final int MESSAGE_TYPE_SUCCESS = 3;

    public static boolean hasHoneycomb() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB;
    }

    public static boolean hasJellyBean() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN;
    }

    public static boolean hasJellyBeanMR1() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1;
    }

    public static boolean hasJellyBeanMR2() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2;
    }

    public static boolean hasKitKat() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;
    }

    public static boolean isLollipop() {
        return Build.VERSION.SDK_INT == Build.VERSION_CODES.LOLLIPOP ||
                Build.VERSION.SDK_INT == Build.VERSION_CODES.LOLLIPOP_MR1;
    }

    public static boolean hasLollipop() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP;
    }

    public static boolean hasMarshmallow() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.M;
    }

    public static boolean hasN() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.N;
    }

    public static boolean hasNMR1() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.N_MR1;
    }

    public static boolean hasO() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.O;
    }

    public static boolean hasNougat() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.N;
    }

    public static String getPhoneInfo() {
        final StringBuilder sb = new StringBuilder();
        sb.append("\n");
        sb.append("Android version : " + Build.VERSION.RELEASE);
        sb.append("\n");
        sb.append("Board:" + Build.BOARD);
        sb.append("\n");
        sb.append("Manufacturer:" + Build.MANUFACTURER);
        sb.append("\n");
        sb.append("Model:" + Build.MODEL);
        sb.append("\n");
        sb.append("Product:" + Build.PRODUCT);
        sb.append("\n");
        sb.append("Device:" + Build.DEVICE);
        sb.append("\n");
        sb.append("ROM:" + Build.DISPLAY);
        sb.append("\n");
        sb.append("Hardware:" + Build.HARDWARE);
        sb.append("\n");
        sb.append("Id:" + Build.ID);
        sb.append("\n");
        sb.append("Tags:" + Build.TAGS);
        sb.append("\n");
        return sb.toString();
    }

    public static void runOnUiThread(Runnable action) {
        new Handler(Looper.getMainLooper()).post(action);
    }

    public static int getStatusPermission(final Context context, final String permission) {
        if (context != null) {
            return ActivityCompat.checkSelfPermission(context, permission);
        } else {
            return PackageManager.PERMISSION_DENIED;
        }
    }

    public static boolean checkPermission(final Context context, final String permission) {
        return getStatusPermission(context, permission) == PackageManager.PERMISSION_GRANTED;
    }

    public static boolean grantPermisions(final String[] permissions, final Activity activity) {
        if (activity != null && permissions != null) {
            if (hasMarshmallow()) {
                final List<String> listPermissionsNeeded = new ArrayList();

                for (String permission : permissions) {
                    if (ActivityCompat.checkSelfPermission(activity.getApplicationContext(),
                            permission)
                            != PackageManager.PERMISSION_GRANTED) {
                        listPermissionsNeeded.add(permission);
                    }
                }

                if (!listPermissionsNeeded.isEmpty()) {
                    String[] arrayPermissionsNeeded = new String[listPermissionsNeeded.size()];
                    listPermissionsNeeded.toArray(arrayPermissionsNeeded);
                    ActivityCompat.requestPermissions(activity,
                            arrayPermissionsNeeded,
                            REQUEST_PERMISSIONS);
                    return false;
                }
            } else {
                return true;
            }
        }
        return false;
    }

    public static int getResourceId(final Context context, final String typeResource, final String nameResource) {
        // Example: context.getResources().getIdentifier("widget_blue", "layout", context.getPackageName())
        if (context != null) {
            return context.getResources().getIdentifier(nameResource, typeResource, context.getPackageName());
        }
        return 0;
    }

    /**
     * Получить возможность службы геолокации
     */
    public static boolean isLocationEnabled(Context context) {
        if (hasKitKat()) {
            int locationMode = 0;
            try {
                locationMode = Settings.Secure.getInt(context.getContentResolver(), Settings.Secure.LOCATION_MODE);
            } catch (Settings.SettingNotFoundException e) {
                return false;
            }
            return locationMode != Settings.Secure.LOCATION_MODE_OFF;
        } else {
            final String locationProviders = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
            return !StringUtils.isNullOrEmpty(locationProviders);
        }
    }

    public static void showToast(final String message, final int duration, final int type) {

        if (StringUtils.isNullOrEmpty(message)) return;

        final Context context = ApplicationSpecialistImpl.getInstance();
        switch (type) {
            case MESSAGE_TYPE_INFO:
                Toasty.info(context, message, duration, false).show();
                break;

            case MESSAGE_TYPE_ERROR:
                Toasty.error(context, message, duration, false).show();
                break;

            case MESSAGE_TYPE_WARNING:
                Toasty.warning(context, message, duration, false).show();
                break;

            case MESSAGE_TYPE_SUCCESS:
                Toasty.success(context, message, duration, false).show();
                break;

            default:
                Toasty.info(context, message, duration, false).show();
                break;

        }
    }

    public static void showFlashbar(final Activity activity, final String title, final String message, final int duration, final int type) {
        final Flashbar.Builder builder = new Flashbar.Builder(activity)
                .gravity(Flashbar.Gravity.TOP)
                .duration(duration == Toast.LENGTH_LONG ? TimeUnit.SECONDS.toMillis(6) : TimeUnit.SECONDS.toMillis(3))
                .message(message)
                .enableSwipeToDismiss();
        if (!StringUtils.isNullOrEmpty(title)) {
            builder.title(title);
        }
        switch (type) {
            case ApplicationUtils.MESSAGE_TYPE_INFO:
                builder.backgroundColorRes(R.color.gray_dark);
                break;

            case ApplicationUtils.MESSAGE_TYPE_ERROR:
                builder.backgroundColorRes(R.color.red);
                break;

            case ApplicationUtils.MESSAGE_TYPE_WARNING:
                builder.backgroundColorRes(R.color.colorAccent);
                break;

            case MESSAGE_TYPE_SUCCESS:
                builder.backgroundColorRes(R.color.blue);
                break;

            default:
                builder.backgroundColorRes(R.color.gray_dark);
                break;

        }
        builder.build().show();
    }

    /**
     * Return the handle to a system-level service by name. The class of the
     * returned object varies by the requested name.
     */
    public static <S> S getSystemService(final Context context, final String serviceName) {
        if (context != null) {
            return SafeUtils.cast(context.getSystemService(serviceName));
        }
        return null;
    }

    /**
     * Контролировать наличие и версию Google Play Services
     */
    public static boolean isGooglePlayServices(final Context context) {
        if (context != null) {
            int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(context);
            if (ConnectionResult.SUCCESS == resultCode) {
                return true;
            }
        }
        return false;
    }

    public static Snackbar showSnackbar(View view, String message, int duration, int type) {
        if (view != null) {
            final Snackbar snackbar = BaseSnackbar.make(view, message, duration, type);
            snackbar.show();
            return snackbar;
        }
        return null;
    }

    public static void showMenu(final Context context, final View view, final int menuId, PopupMenu.OnMenuItemClickListener onMenuItemClickListener, PopupMenu.OnDismissListener onDismissListener) {
        try {
            final PopupMenu popupMenu = new PopupMenu(context, view);
            final Field mFieldPopup = popupMenu.getClass().getDeclaredField("mPopup");
            mFieldPopup.setAccessible(true);
            final MenuPopupHelper mPopup = (MenuPopupHelper) mFieldPopup.get(popupMenu);
            mPopup.setForceShowIcon(true);
            popupMenu.inflate(menuId);
            popupMenu.setOnMenuItemClickListener(onMenuItemClickListener);
            popupMenu.setOnDismissListener(onDismissListener);
            popupMenu.show();
        } catch (Exception e) {
            Log.e("ViewUtils", e.getMessage());
        }
    }

    public static void showUrl(final Context context, final String url) {
        if (context == null || StringUtils.isNullOrEmpty(url)) {
            return;
        }

        final Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(url));
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);

        if (canStartActivity(context, intent)) {
            context.startActivity(intent);
        }
    }

    /**
     * Return true if activity that can handle a given intent is found.
     *
     * @param context The context.
     * @param intent  An intent containing all of the desired specification
     *                (action, data, type, category, and/or component).
     * @return true if activity that can handle a given intent is found, false otherwise.
     */
    public static boolean canStartActivity(final Context context, final Intent intent) {
        if (context == null || intent == null) return false;

        final PackageManager packageManager = context.getPackageManager();
        return (packageManager != null && packageManager.resolveActivity(intent, 0) != null);
    }

    public static Intent sendEmailIntent(
            @NonNull String recipient,
            @NonNull String subject,
            @NonNull String body
    ) {
        final Intent intent = new Intent();
        intent.setAction(Intent.ACTION_SENDTO);
        intent.setType("plain/text");
        intent.setData(Uri.parse("mailto:" + recipient));
        intent.putExtra(Intent.EXTRA_SUBJECT, subject);
        intent.putExtra(Intent.EXTRA_TEXT, body);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        return intent;
    }

    private ApplicationUtils() {
    }

}
