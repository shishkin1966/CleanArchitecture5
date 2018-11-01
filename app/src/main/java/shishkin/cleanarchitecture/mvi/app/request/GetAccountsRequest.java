package shishkin.cleanarchitecture.mvi.app.request;

import java.util.List;


import shishkin.cleanarchitecture.mvi.app.SLUtil;
import shishkin.cleanarchitecture.mvi.app.data.Account;
import shishkin.cleanarchitecture.mvi.app.db.MviDb;
import shishkin.cleanarchitecture.mvi.sl.data.ExtError;
import shishkin.cleanarchitecture.mvi.sl.request.AbsResultRequest;
import shishkin.cleanarchitecture.mvi.sl.request.Request;
import shishkin.cleanarchitecture.mvi.sl.request.ResponseListener;
import shishkin.cleanarchitecture.mvi.sl.task.RequestThreadPoolExecutor;

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
    public int getAction(Request oldRequest) {
        return RequestThreadPoolExecutor.ACTION_DELETE;
    }

    @Override
    public void run() {
        try {
            final MviDb db = SLUtil.getDb();
            setData(db.MviDao().getAccounts());
        } catch (Exception e) {
            SLUtil.onError(NAME, e);
            setError(new ExtError().addError(NAME, e));
        }
        response();
    }
}
