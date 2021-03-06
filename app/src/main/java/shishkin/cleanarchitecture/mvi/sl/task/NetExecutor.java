package shishkin.cleanarchitecture.mvi.sl.task;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.telephony.TelephonyManager;


import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.TimeUnit;


import shishkin.cleanarchitecture.mvi.common.net.Connectivity;
import shishkin.cleanarchitecture.mvi.common.utils.StringUtils;
import shishkin.cleanarchitecture.mvi.sl.ApplicationSpecialistImpl;
import shishkin.cleanarchitecture.mvi.sl.ObservableSubscriber;
import shishkin.cleanarchitecture.mvi.sl.ObservableUnionImpl;
import shishkin.cleanarchitecture.mvi.sl.SL;
import shishkin.cleanarchitecture.mvi.sl.data.Result;
import shishkin.cleanarchitecture.mvi.sl.observe.NetworkBroadcastReceiverObservable;
import shishkin.cleanarchitecture.mvi.sl.request.AbsRequest;
import shishkin.cleanarchitecture.mvi.sl.state.ViewStateObserver;

@SuppressWarnings("unused")
public class NetExecutor extends AbsRequestExecutor implements ObservableSubscriber<Intent> {

    public static final String NAME = NetExecutor.class.getName();

    private static int QUEUE_CAPACITY = 1024;
    private int mThreadCount = 2;
    private int mMaxThreadCount = 2;
    private long mKeepAliveTime = 10; // 10 мин
    private TimeUnit mUnit = TimeUnit.MINUTES;
    private RequestThreadPoolExecutor mExecutor;
    private static volatile NetExecutor sInstance;

    public static NetExecutor getInstance() {
        if (sInstance == null) {
            synchronized (NetExecutor.class) {
                if (sInstance == null) {
                    sInstance = new NetExecutor();
                }
            }
        }
        return sInstance;
    }

    private NetExecutor() {
        setThreadCount(Connectivity.getActiveNetworkInfo(ApplicationSpecialistImpl.getInstance()));

        final BlockingQueue queue = new PriorityBlockingQueue<AbsRequest>(QUEUE_CAPACITY);
        mExecutor = new RequestThreadPoolExecutor(mThreadCount, mMaxThreadCount, mKeepAliveTime, mUnit, queue);

        SL.getInstance().register(this);
    }

    @Override
    protected RequestThreadPoolExecutor getExecutor() {
        return mExecutor;
    }

    public void setThreadCount() {
        final Context context = ApplicationSpecialistImpl.getInstance();
        if (context != null) {
            setThreadCount(Connectivity.getActiveNetworkInfo(context));
        }
    }

    private void setThreadCount(final NetworkInfo info) {
        if (info == null || !info.isConnectedOrConnecting()) {
            mThreadCount = 2;
            mMaxThreadCount = 2;
            return;
        }

        switch (info.getType()) {
            case ConnectivityManager.TYPE_WIFI:
            case ConnectivityManager.TYPE_WIMAX:
            case ConnectivityManager.TYPE_ETHERNET:
                mThreadCount = 6;
                mMaxThreadCount = 6;
                return;

            case ConnectivityManager.TYPE_MOBILE:
                switch (info.getSubtype()) {
                    case TelephonyManager.NETWORK_TYPE_LTE:  // 4G
                    case TelephonyManager.NETWORK_TYPE_HSPAP:
                    case TelephonyManager.NETWORK_TYPE_EHRPD:
                        mThreadCount = 4;
                        mMaxThreadCount = 4;
                        return;

                    case TelephonyManager.NETWORK_TYPE_UMTS: // 3G
                    case TelephonyManager.NETWORK_TYPE_CDMA:
                    case TelephonyManager.NETWORK_TYPE_EVDO_0:
                    case TelephonyManager.NETWORK_TYPE_EVDO_A:
                    case TelephonyManager.NETWORK_TYPE_EVDO_B:
                        mThreadCount = 2;
                        mMaxThreadCount = 2;
                        return;

                    case TelephonyManager.NETWORK_TYPE_GPRS: // 2G
                    case TelephonyManager.NETWORK_TYPE_EDGE:
                        mThreadCount = 1;
                        mMaxThreadCount = 1;
                        return;

                    default:
                        mThreadCount = 2;
                        mMaxThreadCount = 2;
                        return;
                }

            default:
                mThreadCount = 2;
                mMaxThreadCount = 2;
                return;
        }
    }

    @Override
    public void shutdown() {
        super.shutdown();

        SL.getInstance().unregister(this);
    }

    @Override
    public int getState() {
        return ViewStateObserver.STATE_RESUME;
    }

    @Override
    public void setState(int state) {
    }

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public String getPasport() {
        return getName();
    }

    @Override
    public List<String> getObservable() {
        return StringUtils.arrayToList(NetworkBroadcastReceiverObservable.NAME);
    }

    @Override
    public void onChange(Intent object) {
        if (Connectivity.isNetworkConnected(ApplicationSpecialistImpl.getInstance())) {
            setThreadCount();
        }
    }

    @Override
    public List<String> getSpecialistSubscription() {
        return StringUtils.arrayToList(ObservableUnionImpl.NAME);
    }

    @Override
    public Result<Boolean> validateExt() {
        return new Result<>(true);
    }

    @Override
    public boolean validate() {
        return validateExt().getData();
    }

}
