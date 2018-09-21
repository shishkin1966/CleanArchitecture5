package shishkin.cleanarchitecture.mvi.app.notification;

import android.support.annotation.NonNull;


import java.util.List;


import shishkin.cleanarchitecture.mvi.app.data.Account;
import shishkin.cleanarchitecture.mvi.app.db.MviDao;
import shishkin.cleanarchitecture.mvi.app.observe.DbObservable;
import shishkin.cleanarchitecture.mvi.app.sl.Repository;
import shishkin.cleanarchitecture.mvi.common.utils.StringUtils;
import shishkin.cleanarchitecture.mvi.sl.AbsSpecialist;
import shishkin.cleanarchitecture.mvi.sl.DbObservableSubscriber;
import shishkin.cleanarchitecture.mvi.sl.ObservableUnionImpl;
import shishkin.cleanarchitecture.mvi.sl.data.Result;
import shishkin.cleanarchitecture.mvi.sl.request.ResponseListener;
import shishkin.cleanarchitecture.mvi.sl.state.ViewStateObserver;

public class NotificationSpecialistImpl extends AbsSpecialist implements NotificationSpecialist, ResponseListener, DbObservableSubscriber {

    public static final String NAME = NotificationSpecialistImpl.class.getName();

    private NotificationSpecialist mSpecialist = NotificationSpecialistFactory.get();

    @Override
    public int compareTo(@NonNull Object o) {
        return (NotificationSpecialist.class.isInstance(o)) ? 0 : 1;
    }

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public void showBalance(List<MviDao.Balance> list) {
        mSpecialist.showBalance(list);
    }

    @Override
    public void clear() {
        mSpecialist.clear();
    }

    private void getBalance() {
        Repository.getInstance().getBalance(this);
    }

    @Override
    public void onRegister() {
        getBalance();
    }


    @Override
    public void response(Result result) {
        if (!result.hasError()) {
            showBalance((List<MviDao.Balance>) result.getData());
        }
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
        getBalance();
    }

    @Override
    public List<String> getSpecialistSubscription() {
        return StringUtils.arrayToList(ObservableUnionImpl.NAME);
    }

    @Override
    public int getState() {
        return ViewStateObserver.STATE_RESUME;
    }

    @Override
    public void setState(int state) {
    }
}
