package shishkin.cleanarchitecture.mvi.app.request;

import java.util.List;


import shishkin.cleanarchitecture.mvi.app.db.MviDao;
import shishkin.cleanarchitecture.mvi.app.db.MviDb;
import shishkin.cleanarchitecture.mvi.sl.ErrorSpecialistImpl;
import shishkin.cleanarchitecture.mvi.sl.SLUtil;
import shishkin.cleanarchitecture.mvi.sl.data.ExtError;
import shishkin.cleanarchitecture.mvi.sl.request.AbsResultRequest;
import shishkin.cleanarchitecture.mvi.sl.request.ResponseListener;

/**
 * Created by Shishkin on 06.12.2017.
 */

public class GetBalanceRequest extends AbsResultRequest<List<MviDao.Balance>> {

    public static final String NAME = GetBalanceRequest.class.getName();

    public GetBalanceRequest(ResponseListener listener) {
        super(listener);
    }

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public boolean isDistinct() {
        return false;
    }

    @Override
    public void run() {
        try {
            final MviDb db = SLUtil.getDb();
            setData(db.MviDao().getBalance());
        } catch (Exception e) {
            ErrorSpecialistImpl.getInstance().onError(NAME, e);
            setError(new ExtError().addError(NAME, e));
        }
        response();
    }
}
