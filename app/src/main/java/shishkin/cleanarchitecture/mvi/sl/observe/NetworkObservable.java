package shishkin.cleanarchitecture.mvi.sl.observe;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkRequest;


import shishkin.cleanarchitecture.mvi.app.SLUtil;
import shishkin.cleanarchitecture.mvi.common.net.Connectivity;
import shishkin.cleanarchitecture.mvi.common.utils.ApplicationUtils;

public class NetworkObservable extends AbsObservable<Boolean> {

    public static final String NAME = NetworkObservable.class.getName();

    private ConnectivityManager.NetworkCallback callback = null;

    @Override
    public void register() {
        if (ApplicationUtils.hasN()) {
            Context context = SLUtil.getContext();
            final ConnectivityManager connectivityManager = ApplicationUtils.getSystemService(context, context.CONNECTIVITY_SERVICE);
            final NetworkRequest networkRequest = new NetworkRequest.Builder().addTransportType(NetworkCapabilities.TRANSPORT_CELLULAR).addTransportType(NetworkCapabilities.TRANSPORT_WIFI).build();
            callback = new ConnectivityManager.NetworkCallback() {
                @Override
                public void onAvailable(Network network) {
                    onChange(true);
                }

                @Override
                public void onUnavailable() {
                    onChange(false);
                }

                @Override
                public void onLost(Network network) {
                    onChange(false);
                }
            };
            if (!Connectivity.isNetworkConnected(SLUtil.getContext())) {
                onChange(false);
            }
            connectivityManager.registerNetworkCallback(networkRequest, callback);
        }
    }

    @Override
    public void unregister() {
        if (ApplicationUtils.hasN() && callback != null) {
            Context context = SLUtil.getContext();
            final ConnectivityManager connectivityManager = ApplicationUtils.getSystemService(context, context.CONNECTIVITY_SERVICE);
            connectivityManager.unregisterNetworkCallback(callback);
        }
    }

    @Override
    public String getName() {
        return NAME;
    }
}
