package shishkin.cleanarchitecture.mvi.common.utils;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;


import java.util.ArrayList;
import java.util.List;

/**
 * {@code CollectionsExt} contains static methods which operate on
 * {@code Collection} classes.
 */
public class CollectionsUtils {

    /**
     * Joins two or more {@link List}s together.
     *
     * @param lists The lists to be joined.
     * @return A non-null list that contains items for the initial lists.
     */
    @NonNull
    @SafeVarargs
    public static <T> List<T> join(@NonNull final List<T>... lists) {
        int totalSize = 0;
        for (final List<T> list : lists) {
            totalSize += list.size();
        }

        final List<T> result = new ArrayList<T>(totalSize);
        for (final List<T> list : lists) {
            result.addAll(list);
        }
        return result;
    }

    public static <T> boolean equals(T[] a, T[] a2) {
        if (a == null || a2 == null)
            return false;

        final int length = a.length;
        if (a2.length != length)
            return false;

        for (int i = 0; i < length; i++) {
            T o1 = a[i];
            T o2 = a2[i];
            if (!(o1 == null ? o2 == null : o1.equals(o2))) {
                return false;
            }
        }

        return true;
    }

    public static boolean equals(byte[] a, byte[] a2) {
        if (a == null || a2 == null)
            return false;

        final int length = a.length;
        if (a2.length != length)
            return false;

        for (int i = 0; i < length; i++) {
            if (a[i] != a2[i]) {
                return false;
            }
        }

        return true;
    }

    /**
     * Returns first item in the list if list is not empty.
     *
     * @param list The list to get first item from.
     */
    @Nullable
    public static <T> T first(@Nullable final List<T> list) {
        return (list == null || list.isEmpty() ? null : list.get(0));
    }

    private CollectionsUtils() {
    }

}
