package shishkin.cleanarchitecture.mvi.sl.observe;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;


import shishkin.cleanarchitecture.mvi.sl.SLUtil;

/**
 * Created by Shishkin on 15.12.2017.
 */

public abstract class AbsBroadcastReceiverObservable extends AbsObservable<Intent> {

    private IntentFilter mIntentFilter;
    private BroadcastReceiver mBroadcastReceiver = null;

    public AbsBroadcastReceiverObservable() {
        mIntentFilter = getIntentFilter();
    }

    public abstract IntentFilter getIntentFilter();

    @Override
    public void register() {
        final Context context = SLUtil.getContext();
        if (context != null) {
            mBroadcastReceiver = new BroadcastReceiver() {
                @Override
                public void onReceive(final Context context, final Intent intent) {
                    onChange(intent);
                }
            };

            context.registerReceiver(mBroadcastReceiver, mIntentFilter);
        }
    }

    @Override
    public void unregister() {
        if (mBroadcastReceiver != null) {
            final Context context = SLUtil.getContext();
            if (context != null && mBroadcastReceiver != null) {
                context.unregisterReceiver(mBroadcastReceiver);
                mBroadcastReceiver = null;
            }
        }
    }
}
