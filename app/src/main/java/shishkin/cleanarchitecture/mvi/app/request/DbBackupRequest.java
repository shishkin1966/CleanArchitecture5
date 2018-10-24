package shishkin.cleanarchitecture.mvi.app.request;

import shishkin.cleanarchitecture.mvi.app.SLUtil;
import shishkin.cleanarchitecture.mvi.app.db.MviDb;
import shishkin.cleanarchitecture.mvi.sl.ApplicationSpecialistImpl;
import shishkin.cleanarchitecture.mvi.sl.request.AbsRequest;

public class DbBackupRequest extends AbsRequest {

    @Override
    public boolean isDistinct() {
        return true;
    }

    @Override
    public void run() {
        SLUtil.getDbProvider().backup(MviDb.NAME, ApplicationSpecialistImpl.getInstance().getExternalDataPath());
    }
}
