package shishkin.cleanarchitecture.mvi.app.screen.activity.main;

import java.util.List;


import shishkin.cleanarchitecture.mvi.app.SLUtil;
import shishkin.cleanarchitecture.mvi.app.specialist.scanner.ScannerSubscriber;
import shishkin.cleanarchitecture.mvi.app.specialist.scanner.ScannerUnionImpl;
import shishkin.cleanarchitecture.mvi.common.net.Connectivity;
import shishkin.cleanarchitecture.mvi.common.utils.StringUtils;
import shishkin.cleanarchitecture.mvi.sl.ObservableSubscriber;
import shishkin.cleanarchitecture.mvi.sl.ObservableUnionImpl;
import shishkin.cleanarchitecture.mvi.sl.event.ShowDialogEvent;
import shishkin.cleanarchitecture.mvi.sl.observe.NetworkBroadcastReceiverObservable;
import shishkin.cleanarchitecture.mvi.sl.presenter.AbsPresenter;

/**
 * Created by Shishkin on 17.03.2018.
 */

public class MainPresenter extends AbsPresenter<MainModel> implements ObservableSubscriber, ScannerSubscriber {

    public static final String NAME = MainPresenter.class.getName();

    public MainPresenter(MainModel model) {
        super(model);
    }

    public void hideSideMenu() {
        getModel().getView().hideSideMenu();
    }

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public boolean isRegister() {
        return true;
    }

    @Override
    public List<String> getObservable() {
        return StringUtils.arrayToList(
                NetworkBroadcastReceiverObservable.NAME
        );
    }

    @Override
    public void onChange(Object object) {
        if (validate()) {
            if (Connectivity.isNetworkConnected(SLUtil.getContext())) {
                getModel().getView().onConnect();
            } else {
                getModel().getView().onDisconnect();
            }
        }
    }

    @Override
    public List<String> getSpecialistSubscription() {
        return StringUtils.arrayToList(
                super.getSpecialistSubscription(),
                ScannerUnionImpl.NAME,
                ObservableUnionImpl.NAME
        );
    }

    @Override
    public void onScan(String text) {
        SLUtil.getActivityUnion().showDialog(new ShowDialogEvent(-1, null, "Код", text));
    }
}



