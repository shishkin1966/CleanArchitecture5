package shishkin.cleanarchitecture.mvi.common.utils;

import android.util.Log;

/**
 * {@code SafeUtils} contains static methods to perform safe operation like object casting.
 */
public class SafeUtils {

    private static final String NAME = SafeUtils.class.getName();

    public static <C> C cast(final Object o) {
        if (o == null) {
            return null;
        }

        try {
            return (C) o;
        } catch (final ClassCastException cce) {
            Log.e(NAME, cce.getMessage());
            return null;
        } catch (final Exception e) {
            Log.e(NAME, e.getMessage());
            return null;
        }
    }

    private SafeUtils() {
    }

}
