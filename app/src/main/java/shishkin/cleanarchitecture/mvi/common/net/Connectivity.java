package shishkin.cleanarchitecture.mvi.common.net;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;


import shishkin.cleanarchitecture.mvi.common.utils.ApplicationUtils;

/**
 * {@code Connectivity} contains static methods which operate with connectivity.
 */
public final class Connectivity {

    /**
     * Indicates whether network connectivity exists and it is possible to establish
     * connections and pass data.
     * <p>Always call this before attempting to perform data transactions.
     *
     * @return {@code true} if network connectivity exists, {@code false} otherwise.
     */
    public static boolean isNetworkConnected(final Context context) {
        if (context == null) {
            return false;
        }

        final NetworkInfo activeNetwork = getActiveNetworkInfo(context);
        return isNetworkConnected(activeNetwork);
    }

    /**
     * Indicates whether network connectivity exists and it is possible to establish
     * connections and pass data.
     * <p>Always call this before attempting to perform data transactions.
     *
     * @return {@code true} if network connectivity exists, {@code false} otherwise.
     */
    public static boolean isNetworkConnected(@Nullable final NetworkInfo activeNetwork) {
        return (activeNetwork != null && activeNetwork.isConnected());
    }

    /**
     * Indicates whether network connectivity exists or is in the process
     * of being established. This is good for applications that need to
     * do anything related to the network other than read or write data.
     *
     * @return {@code true} if network connectivity exists or is in the process
     * of being established, {@code false} otherwise.
     */
    public static boolean isNetworkConnectedOrConnecting(final Context context) {
        if (context == null) {
            return false;
        }

        final NetworkInfo activeNetwork = getActiveNetworkInfo(context);
        return isNetworkConnectedOrConnecting(activeNetwork);
    }

    /**
     * Indicates whether network connectivity exists or is in the process
     * of being established. This is good for applications that need to
     * do anything related to the network other than read or write data.
     *
     * @return {@code true} if network connectivity exists or is in the process
     * of being established, {@code false} otherwise.
     */
    public static boolean isNetworkConnectedOrConnecting(@Nullable final NetworkInfo activeNetwork) {
        return (activeNetwork != null && activeNetwork.isConnectedOrConnecting());
    }

    /**
     * Returns details about the currently active default data network. When
     * connected, this network is the default route for outgoing connections.
     * You should always check {@link NetworkInfo#isConnected()} before initiating
     * network traffic. This may return {@code null} when there is no default
     * network.
     * <p>This method requires the caller to hold the permission
     * {@link android.Manifest.permission#ACCESS_NETWORK_STATE}.
     *
     * @return a {@link NetworkInfo} object for the current default network
     * or {@code null} if no default network is currently active
     */
    @Nullable
    public static NetworkInfo getActiveNetworkInfo(@NonNull final Context context) {
        final ConnectivityManager connectivityManager = getConnectivityManager(context);
        if (connectivityManager != null) {
            return connectivityManager.getActiveNetworkInfo();
        } else {
            return null;
        }
    }

    @Nullable
    private static ConnectivityManager getConnectivityManager(@NonNull final Context context) {
        return ApplicationUtils.getSystemService(context, Context.CONNECTIVITY_SERVICE);
    }

    public static boolean isWiFiConnected(@NonNull final Context context) {
        if (context != null) {
            final WifiManager wifiManager = ApplicationUtils.getSystemService(context, Context.WIFI_SERVICE);
            if (wifiManager.isWifiEnabled()) { // WiFi adapter is ON
                final WifiInfo wifiInfo = wifiManager.getConnectionInfo();
                return wifiInfo.getNetworkId() != -1;
            } else {
                return false; // WiFi adapter is OFF
            }
        } else {
            return false;
        }
    }

    private Connectivity() {
    }

}
