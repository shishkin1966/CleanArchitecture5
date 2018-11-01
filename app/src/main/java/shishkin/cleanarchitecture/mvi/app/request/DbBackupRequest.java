package shishkin.cleanarchitecture.mvi.app.request;

import shishkin.cleanarchitecture.mvi.app.SLUtil;
import shishkin.cleanarchitecture.mvi.app.db.MviDb;
import shishkin.cleanarchitecture.mvi.sl.ApplicationSpecialistImpl;
import shishkin.cleanarchitecture.mvi.sl.request.AbsRequest;
import shishkin.cleanarchitecture.mvi.sl.request.Request;
import shishkin.cleanarchitecture.mvi.sl.task.RequestThreadPoolExecutor;

public class DbBackupRequest extends AbsRequest {

    @Override
    public boolean isDistinct() {
        return true;
    }

    @Override
    public int getAction(Request oldRequest) {
        return RequestThreadPoolExecutor.ACTION_IGNORE;
    }

    @Override
    public void run() {
        SLUtil.getDbProvider().backup(MviDb.NAME, ApplicationSpecialistImpl.getInstance().getExternalDataPath());
    }
}
