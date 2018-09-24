package shishkin.cleanarchitecture.mvi.app.preference;


import java.util.Map;


import shishkin.cleanarchitecture.mvi.sl.Specialist;

/**
 * Интерфейс Preferences
 */
public interface PreferencesSpecialist extends Specialist {

    void putString(final String key, final String value);

    String getString(final String key);

    String getString(final String key, final String defaultValue);

    void putInt(final String key, final int value);

    int getInt(final String key);

    int getInt(final String key, final int defaultValue);

    void putLong(final String key, final long value);

    long getLong(final String key);

    long getLong(final String key, final long defaultValue);

    void putFloat(final String key, final float value);

    float getFloat(final String key);

    float getFloat(String key, float defaultValue);

    void putBoolean(final String key, final boolean value);

    boolean getBoolean(final String key);

    boolean getBoolean(final String key, final boolean defaultValue);

    Map<String, ?> getAll();

    void remove(final String key);

    boolean contains(final String key);

    void clear();

}
