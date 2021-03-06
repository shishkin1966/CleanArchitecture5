package shishkin.cleanarchitecture.mvi.app.request;

import shishkin.cleanarchitecture.mvi.app.SLUtil;
import shishkin.cleanarchitecture.mvi.app.data.Account;
import shishkin.cleanarchitecture.mvi.app.db.MviDb;
import shishkin.cleanarchitecture.mvi.app.observe.DbObservable;
import shishkin.cleanarchitecture.mvi.sl.data.ExtError;
import shishkin.cleanarchitecture.mvi.sl.request.AbsResultRequest;
import shishkin.cleanarchitecture.mvi.sl.request.ResponseListener;

/**
 * Created by Shishkin on 06.12.2017.
 */

public class AddAccountRequest extends AbsResultRequest<Account> {

    public static final String NAME = AddAccountRequest.class.getName();

    private Account mAccount;

    public AddAccountRequest(ResponseListener listener, final Account account) {
        super(listener);

        mAccount = account;
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
            db.beginTransaction();
            mAccount.setOpenDate(System.currentTimeMillis());
            db.MviDao().insertAccount(mAccount);
            db.setTransactionSuccessful();
            db.endTransaction();
            setData(mAccount);
            final DbObservable observable = (DbObservable) SLUtil.getObservableUnion().get(DbObservable.NAME);
            observable.onChange(Account.TABLE);
        } catch (Exception e) {
            SLUtil.onError(NAME, e);
            setError(new ExtError().addError(NAME, e));
        }
        response();
    }

}
