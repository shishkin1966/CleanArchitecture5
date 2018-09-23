package shishkin.cleanarchitecture.mvi.common.utils;

import android.util.Log;


import java.io.Closeable;
import java.io.IOException;

public class CloseUtils {
    private static final String NAME = CloseUtils.class.getName();

    private CloseUtils() {
    }

    public static void close(Closeable... closeables) {
        if (closeables == null) return;
        try {
            for (Closeable closeable : closeables) {
                if (closeable != null) {
                    closeable.close();
                }
            }
        } catch (IOException e) {
            Log.e(NAME, e.getMessage());
        }
    }

}
