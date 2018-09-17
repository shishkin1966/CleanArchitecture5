package shishkin.cleanarchitecture.mvi.app;

import com.squareup.leakcanary.LeakCanary;


import shishkin.cleanarchitecture.mvi.app.db.MviDb;
import shishkin.cleanarchitecture.mvi.app.observe.DbObservable;
import shishkin.cleanarchitecture.mvi.sl.ApplicationSpecialistImpl;
import shishkin.cleanarchitecture.mvi.sl.SLUtil;
import shishkin.cleanarchitecture.mvi.sl.observe.NetworkBroadcastReceiverObservable;

/**
 * Created by Shishkin on 08.02.2018.
 */

public class ApplicationController extends ApplicationSpecialistImpl {
    @Override
    public boolean isKillOnFinish() {
        return true;
    }

    @Override
    public void onStart() {
        super.onStart();

        SLUtil.getDbProvider().getDb(MviDb.class, MviDb.NAME);
        SLUtil.getObservableUnion().register(new DbObservable());
        SLUtil.getObservableUnion().register(new NetworkBroadcastReceiverObservable());

        LeakCanary.install(this);
    }
}
