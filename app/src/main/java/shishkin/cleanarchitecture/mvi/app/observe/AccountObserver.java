package shishkin.cleanarchitecture.mvi.app.observe;

import java.util.List;


import shishkin.cleanarchitecture.mvi.BuildConfig;
import shishkin.cleanarchitecture.mvi.app.ApplicationController;
import shishkin.cleanarchitecture.mvi.app.SLUtil;
import shishkin.cleanarchitecture.mvi.app.data.Account;
import shishkin.cleanarchitecture.mvi.app.db.MviDao;
import shishkin.cleanarchitecture.mvi.app.sl.Repository;
import shishkin.cleanarchitecture.mvi.common.utils.SafeUtils;
import shishkin.cleanarchitecture.mvi.common.utils.StringUtils;
import shishkin.cleanarchitecture.mvi.sl.DbObservableSubscriber;
import shishkin.cleanarchitecture.mvi.sl.ObservableUnionImpl;
import shishkin.cleanarchitecture.mvi.sl.data.Result;
import shishkin.cleanarchitecture.mvi.sl.request.ResponseListener;
import shishkin.cleanarchitecture.mvi.sl.state.ViewStateObserver;

public class AccountObserver implements DbObservableSubscriber, ResponseListener {
    public static final String NAME = AccountObserver.class.getName();
    public static final String ACTION_CLICK = BuildConfig.APPLICATION_ID + ".ACTION_CLICK";

    private static volatile AccountObserver sInstance;

    public static void instantiate() {
        if (sInstance == null) {
            synchronized (AccountObserver.class) {
                if (sInstance == null) {
                    sInstance = new AccountObserver();
                }
            }
        }
    }

    public static AccountObserver getInstance() {
        if (sInstance == null) {
            instantiate();
        }
        return sInstance;
    }

    private AccountObserver() {
        Repository.getInstance().getBalance(this);
    }

    @Override
    public List<String> getTables() {
        return StringUtils.arrayToList(Account.TABLE);
    }

    @Override
    public List<String> getObservable() {
        return StringUtils.arrayToList(DbObservable.NAME);
    }

    @Override
    public void onChange(Object object) {
        Repository.getInstance().getBalance(this);
    }

    @Override
    public List<String> getSpecialistSubscription() {
        return StringUtils.arrayToList(
                ObservableUnionImpl.NAME
        );
    }

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public Result<Boolean> validateExt() {
        return new Result<>(true);
    }

    @Override
    public boolean validate() {
        return validateExt().getData();
    }

    @Override
    public int getState() {
        return ViewStateObserver.STATE_RESUME;
    }

    @Override
    public void setState(int state) {
    }

    @Override
    public void response(Result result) {
        if (!result.hasError()) {
            final List<MviDao.Balance> list = SafeUtils.cast(result.getData());
            SLUtil.getNotificationSpecialist().showBalance(list);
            ((ApplicationController) ApplicationController.getInstance()).updateWidget();
        }
    }
}
