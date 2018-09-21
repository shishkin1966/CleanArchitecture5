package shishkin.cleanarchitecture.mvi.app.observe;

import java.util.List;


import shishkin.cleanarchitecture.mvi.app.data.Account;
import shishkin.cleanarchitecture.mvi.app.notification.NotificationSpecialist;
import shishkin.cleanarchitecture.mvi.app.notification.NotificationSpecialistImpl;
import shishkin.cleanarchitecture.mvi.common.utils.StringUtils;
import shishkin.cleanarchitecture.mvi.sl.DbObservableSubscriber;
import shishkin.cleanarchitecture.mvi.sl.ObservableUnionImpl;
import shishkin.cleanarchitecture.mvi.sl.SL;
import shishkin.cleanarchitecture.mvi.sl.data.Result;
import shishkin.cleanarchitecture.mvi.sl.state.ViewStateObserver;

public class BalanceObserver implements DbObservableSubscriber {

    public static final String NAME = BalanceObserver.class.getName();

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
        final NotificationSpecialist specialist = SL.getInstance().get(NotificationSpecialistImpl.NAME);
        if (specialist != null) {
            specialist.getBalance();
        }
    }

    @Override
    public List<String> getSpecialistSubscription() {
        return StringUtils.arrayToList(ObservableUnionImpl.NAME);
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
}
