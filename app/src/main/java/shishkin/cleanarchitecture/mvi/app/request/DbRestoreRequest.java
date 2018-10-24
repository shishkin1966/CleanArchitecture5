package shishkin.cleanarchitecture.mvi.app.request;

import shishkin.cleanarchitecture.mvi.app.SLUtil;
import shishkin.cleanarchitecture.mvi.app.db.MviDb;
import shishkin.cleanarchitecture.mvi.app.observe.AccountObserver;
import shishkin.cleanarchitecture.mvi.sl.ApplicationSpecialistImpl;
import shishkin.cleanarchitecture.mvi.sl.request.AbsRequest;

public class DbRestoreRequest extends AbsRequest {

    @Override
    public boolean isDistinct() {
        return true;
    }

    @Override
    public void run() {
        SLUtil.getDbProvider().restore(MviDb.NAME, ApplicationSpecialistImpl.getInstance().getExternalDataPath());
        AccountObserver.getInstance().onChange(null);
    }
}
