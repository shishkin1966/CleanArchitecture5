package shishkin.cleanarchitecture.mvi.app.screen.activity.main;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.design.widget.Snackbar;


import java.util.List;


import shishkin.cleanarchitecture.mvi.R;
import shishkin.cleanarchitecture.mvi.common.net.Connectivity;
import shishkin.cleanarchitecture.mvi.common.utils.ApplicationUtils;
import shishkin.cleanarchitecture.mvi.common.utils.StringUtils;
import shishkin.cleanarchitecture.mvi.common.utils.ViewUtils;
import shishkin.cleanarchitecture.mvi.sl.ObservableSubscriber;
import shishkin.cleanarchitecture.mvi.sl.ObservableUnionImpl;
import shishkin.cleanarchitecture.mvi.sl.SLUtil;
import shishkin.cleanarchitecture.mvi.sl.observe.NetworkBroadcastReceiverObservable;
import shishkin.cleanarchitecture.mvi.sl.ui.AbsContentActivity;

public class MainActivity extends AbsContentActivity<MainModel> implements ObservableSubscriber<Intent> {

    public static final String NAME = MainActivity.class.getName();

    private Snackbar mSnackbar;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        setModel(new MainModel(this));

        lockOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        getModel().getRouter().showMainFragment();
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
                ObservableUnionImpl.NAME
        );
    }


}
