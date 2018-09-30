package shishkin.cleanarchitecture.mvi.app.screen.activity.main;

import android.Manifest;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.location.Address;
import android.location.Location;
import android.os.Bundle;
import android.support.design.widget.Snackbar;

import com.firebase.jobdispatcher.Job;
import com.firebase.jobdispatcher.Lifetime;
import com.firebase.jobdispatcher.RetryStrategy;
import com.firebase.jobdispatcher.Trigger;


import java.util.List;


import shishkin.cleanarchitecture.mvi.R;
import shishkin.cleanarchitecture.mvi.app.SLUtil;
import shishkin.cleanarchitecture.mvi.app.job.JobSpecialistService;
import shishkin.cleanarchitecture.mvi.app.location.LocationSubscriber;
import shishkin.cleanarchitecture.mvi.app.location.LocationUnionImpl;
import shishkin.cleanarchitecture.mvi.app.observe.AccountObserver;
import shishkin.cleanarchitecture.mvi.common.net.Connectivity;
import shishkin.cleanarchitecture.mvi.common.utils.ApplicationUtils;
import shishkin.cleanarchitecture.mvi.common.utils.StringUtils;
import shishkin.cleanarchitecture.mvi.common.utils.ViewUtils;
import shishkin.cleanarchitecture.mvi.sl.ObservableSubscriber;
import shishkin.cleanarchitecture.mvi.sl.ObservableUnionImpl;
import shishkin.cleanarchitecture.mvi.sl.observe.NetworkBroadcastReceiverObservable;
import shishkin.cleanarchitecture.mvi.sl.ui.AbsContentActivity;

public class MainActivity extends AbsContentActivity<MainModel> implements ObservableSubscriber<Intent>, LocationSubscriber {

    public static final String NAME = MainActivity.class.getName();

    private Snackbar mSnackbar;
    private Intent mIntent;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        lockOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        final Job.Builder builder = SLUtil.getJobSpecialist().getJobBuilder();
        final Job job = builder
                .setService(JobSpecialistService.class)
                .setLifetime(Lifetime.FOREVER)
                .setRecurring(false)
                .setTag(JobSpecialistService.NAME)
                .setTrigger(Trigger.executionWindow(5, 15))
                .setRetryStrategy(RetryStrategy.DEFAULT_EXPONENTIAL)
                .setReplaceCurrent(false)
                .build();
        SLUtil.getJobSpecialist().schedule(job);

        onNewIntent(getIntent());
    }

    @Override
    public MainModel createModel() {
        return new MainModel(this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        mIntent = intent;
    }

    @Override
    protected void onStart() {
        super.onStart();

        if (mIntent != null) {
            final String action = mIntent.getAction();
            if ("android.intent.action.MAIN".equalsIgnoreCase(action)) {
                getModel().getRouter().showMainFragment();
            } else if (AccountObserver.ACTION_CLICK.equalsIgnoreCase(action)) {
                getModel().getRouter().showMainFragment();
            }
        } else {
            getModel().getRouter().showMainFragment();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (!ApplicationUtils.checkPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
            SLUtil.getActivityUnion().grantPermission(Manifest.permission.ACCESS_FINE_LOCATION);
        }
    }

    @Override
    public void onPermisionGranted(final String permission) {
        if (Manifest.permission.ACCESS_FINE_LOCATION.equals(permission)) {
            SLUtil.getLocationUnion().start();
        }
    }

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public int getContentResId() {
        return R.id.content;
    }

    @Override
    public List<String> getObservable() {
        return StringUtils.arrayToList(NetworkBroadcastReceiverObservable.NAME);
    }

    @Override
    public void onChange(Intent object) {
        if (validate()) {
            if (Connectivity.isNetworkConnected(SLUtil.getContext())) {
                onConnect();
            } else {
                onDisconnect();
            }
        }
    }

    private void onConnect() {
        if (mSnackbar != null) {
            mSnackbar.dismiss();
        }
    }

    private void onDisconnect() {
        mSnackbar = ViewUtils.showSnackbar(getRootView(), getString(R.string.network_disconnected), Snackbar.LENGTH_INDEFINITE, ApplicationUtils.MESSAGE_TYPE_WARNING);
    }

    @Override
    public List<String> getSpecialistSubscription() {
        return StringUtils.arrayToList(
                super.getSpecialistSubscription(),
                ObservableUnionImpl.NAME,
                LocationUnionImpl.NAME
        );
    }


    @Override
    public void setLocation(Location location) {
        final List<Address> list = SLUtil.getLocationUnion().getAddress(location);
        if (list != null && !list.isEmpty()) {
            final Address address = list.get(0);
            final StringBuilder sb = new StringBuilder();
            for (int i = 0; i <= address.getMaxAddressLineIndex(); i++) {
                sb.append(address.getAddressLine(i)).append("\n");
            }
            //SLUtil.getNotificationSpecialist().showMessage(ApplicationSpecialistImpl.getInstance().getString(R.string.location), sb.toString());
        }
    }
}
