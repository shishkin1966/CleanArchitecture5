package shishkin.cleanarchitecture.mvi.sl;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;

/**
 * Специалист Preferences
 */
@SuppressWarnings("unused")
public class PreferencesSpecialistImpl extends AbsSpecialist implements PreferencesSpecialist {

    public static final String NAME = PreferencesSpecialistImpl.class.getName();

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public boolean isPersistent() {
        return false;
    }

    @Override
    public int compareTo(@NonNull Object o) {
        return (o instanceof PreferencesSpecialist) ? 0 : 1;
    }

    @Override
    public void putString(final String key, final String value) {
        final Context context = ApplicationSpecialistImpl.getInstance();
        if (context != null) {
            final SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context);
            final SharedPreferences.Editor editor = settings.edit();
            editor.putString(key, value).apply();
        }
    }

    @Override
    public String getString(final String key) {
        return getString(key, null);
    }

    @Override
    public String getString(final String key, final String defaultValue) {
        final Context context = ApplicationSpecialistImpl.getInstance();
        if (context != null) {
            final SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context);
            return settings.getString(key, defaultValue);
        }
        return defaultValue;
    }

    @Override
    public void putInt(final String key, final int value) {
        final Context context = ApplicationSpecialistImpl.getInstance();
        if (context != null) {
            final SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context);
            final SharedPreferences.Editor editor = settings.edit();
            editor.putInt(key, value).apply();
        }
    }

    @Override
    public int getInt(final String key) {
        return getInt(key, -1);
    }

    @Override
    public int getInt(final String key, final int defaultValue) {
        final Context context = ApplicationSpecialistImpl.getInstance();
        if (context != null) {
            final SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context);
            return settings.getInt(key, defaultValue);
        }
        return defaultValue;
    }

    @Override
    public void putLong(final String key, final long value) {
        final Context context = ApplicationSpecialistImpl.getInstance();
        if (context != null) {
            final SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context);
            final SharedPreferences.Editor editor = settings.edit();
            editor.putLong(key, value).apply();
        }
    }

    @Override
    public long getLong(final String key) {
        return getLong(key, -1L);
    }

    @Override
    public long getLong(final String key, final long defaultValue) {
        final Context context = ApplicationSpecialistImpl.getInstance();
        if (context != null) {
            final SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context);
            return settings.getLong(key, defaultValue);
        }
        return defaultValue;
    }

    @Override
    public void putFloat(final String key, final float value) {
        final Context context = ApplicationSpecialistImpl.getInstance();
        if (context != null) {
            final SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context);
            final SharedPreferences.Editor editor = settings.edit();
            editor.putFloat(key, value).apply();
        }
    }

    @Override
    public float getFloat(final String key) {
        return getFloat(key, -1f);
    }

    @Override
    public float getFloat(String key, float defaultValue) {
        final Context context = ApplicationSpecialistImpl.getInstance();
        if (context != null) {
            final SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context);
            return settings.getFloat(key, defaultValue);
        }
        return defaultValue;
    }

    @Override
    public void putBoolean(final String key, final boolean value) {
        final Context context = ApplicationSpecialistImpl.getInstance();
        if (context != null) {
            final SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context);
            final SharedPreferences.Editor editor = settings.edit();
            editor.putBoolean(key, value).apply();
        }
    }

    @Override
    public boolean getBoolean(final String key) {
        return getBoolean(key, false);
    }

    @Override
    public boolean getBoolean(final String key, final boolean defaultValue) {
        final Context context = ApplicationSpecialistImpl.getInstance();
        if (context != null) {
            final SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context);
            return settings.getBoolean(key, defaultValue);
        }
        return defaultValue;
    }

    @Override
    public void remove(final String key) {
        final Context context = ApplicationSpecialistImpl.getInstance();
        if (context != null) {
            final SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context);
            final SharedPreferences.Editor editor = settings.edit();
            editor.remove(key).apply();
        }
    }

    @Override
    public boolean contains(final String key) {
        final Context context = ApplicationSpecialistImpl.getInstance();
        if (context != null) {
            final SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context);
            return settings.contains(key);
        }
        return false;
    }

    @Override
    public void clear() {
        final Context context = ApplicationSpecialistImpl.getInstance();
        if (context != null) {
            final SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context);
            final SharedPreferences.Editor editor = settings.edit();
            editor.clear().apply();
        }
    }

}
