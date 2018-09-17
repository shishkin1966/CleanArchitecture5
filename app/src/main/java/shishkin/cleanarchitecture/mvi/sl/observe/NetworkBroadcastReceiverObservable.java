package shishkin.cleanarchitecture.mvi.sl.observe;

import android.content.IntentFilter;
import android.net.ConnectivityManager;

/**
 * Created by Shishkin on 16.12.2017.
 */

public class NetworkBroadcastReceiverObservable extends AbsBroadcastReceiverObservable {

    public static final String NAME = NetworkBroadcastReceiverObservable.class.getName();

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public IntentFilter getIntentFilter() {
        final IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        return intentFilter;
    }
}
