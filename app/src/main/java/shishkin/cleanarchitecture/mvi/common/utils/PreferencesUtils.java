package shishkin.cleanarchitecture.mvi.common.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;


import java.util.Map;

/**
 * Preferences приложения
 */
@SuppressWarnings("unused")
public class PreferencesUtils {

    private PreferencesUtils() {
    }

    public static void putString(final Context context, final String key, final String value) {
        if (context != null) {
            final SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
            final SharedPreferences.Editor editor = preferences.edit();
            editor.putString(key, value).apply();
        }
    }

    public static String getString(final Context context, final String key) {
        return getString(context, key, null);
    }

    public static String getString(final Context context, final String key, final String defaultValue) {
        if (context != null) {
            final SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
            return preferences.getString(key, defaultValue);
        }
        return defaultValue;
    }

    public static void putInt(final Context context, final String key, final int value) {
        if (context != null) {
            final SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
            final SharedPreferences.Editor editor = preferences.edit();
            editor.putInt(key, value).apply();
        }
    }

    public static int getInt(final Context context, final String key) {
        return getInt(context, key, -1);
    }

    public static int getInt(final Context context, final String key, final int defaultValue) {
        if (context != null) {
            final SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
            return preferences.getInt(key, defaultValue);
        }
        return defaultValue;
    }

    public static void putLong(final Context context, final String key, final long value) {
        if (context != null) {
            final SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
            final SharedPreferences.Editor editor = preferences.edit();
            editor.putLong(key, value).apply();
        }
    }

    public static long getLong(final Context context, final String key) {
        return getLong(context, key, -1L);
    }

    public static long getLong(final Context context, final String key, final long defaultValue) {
        if (context != null) {
            final SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
            return preferences.getLong(key, defaultValue);
        }
        return defaultValue;
    }

    public static void putFloat(final Context context, final String key, final float value) {
        if (context != null) {
            final SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
            final SharedPreferences.Editor editor = preferences.edit();
            editor.putFloat(key, value).apply();
        }
    }

    public static float getFloat(final Context context, final String key) {
        return getFloat(context, key, -1f);
    }

    public static float getFloat(final Context context, String key, float defaultValue) {
        if (context != null) {
            final SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
            return preferences.getFloat(key, defaultValue);
        }
        return defaultValue;
    }

    public static void putBoolean(final Context context, final String key, final boolean value) {
        if (context != null) {
            final SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
            final SharedPreferences.Editor editor = preferences.edit();
            editor.putBoolean(key, value).apply();
        }
    }

    public static boolean getBoolean(final Context context, final String key) {
        return getBoolean(context, key, false);
    }

    public static boolean getBoolean(final Context context, final String key, final boolean defaultValue) {
        if (context != null) {
            final SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
            return preferences.getBoolean(key, defaultValue);
        }
        return defaultValue;
    }

    public static Map<String, ?> getAll(final Context context) {
        if (context != null) {
            final SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
            return preferences.getAll();
        }
        return null;
    }

    public static void remove(final Context context, final String key) {
        if (context != null) {
            final SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
            final SharedPreferences.Editor editor = preferences.edit();
            editor.remove(key).commit();
        }
    }

    public static boolean contains(final Context context, final String key) {
        if (context != null) {
            final SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
            return preferences.contains(key);
        }
        return false;
    }

    public static void clear(final Context context) {
        if (context != null) {
            final SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
            final SharedPreferences.Editor editor = preferences.edit();
            editor.clear().commit();
        }
    }
}

