package shishkin.cleanarchitecture.mvi.app.request;

import java.util.List;


import shishkin.cleanarchitecture.mvi.app.SLUtil;
import shishkin.cleanarchitecture.mvi.app.data.Account;
import shishkin.cleanarchitecture.mvi.app.db.MviDb;
import shishkin.cleanarchitecture.mvi.sl.ErrorSpecialistImpl;
import shishkin.cleanarchitecture.mvi.sl.data.ExtError;
import shishkin.cleanarchitecture.mvi.sl.request.AbsResultRequest;
import shishkin.cleanarchitecture.mvi.sl.request.ResponseListener;

/**
 * Created by Shishkin on 06.12.2017.
 */

public class GetAccountsRequest extends AbsResultRequest<List<Account>> {

    public static final String NAME = GetAccountsRequest.class.getName();

    public GetAccountsRequest(ResponseListener listener) {
        super(listener);
    }

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public boolean isDistinct() {
        return true;
    }

    @Override
    public void run() {
        try {
            final MviDb db = SLUtil.getDb();
            setData(db.MviDao().getAccounts());
            Thread.sleep(500);
        } catch (Exception e) {
            ErrorSpecialistImpl.getInstance().onError(NAME, e);
            setError(new ExtError().addError(NAME, e));
        }
        response();
    }
}
