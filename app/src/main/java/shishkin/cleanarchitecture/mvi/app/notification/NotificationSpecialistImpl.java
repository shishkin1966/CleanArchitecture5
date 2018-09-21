package shishkin.cleanarchitecture.mvi.app.notification;

import android.support.annotation.NonNull;


import java.util.List;


import shishkin.cleanarchitecture.mvi.app.db.MviDao;
import shishkin.cleanarchitecture.mvi.app.observe.BalanceObserver;
import shishkin.cleanarchitecture.mvi.app.sl.Repository;
import shishkin.cleanarchitecture.mvi.sl.AbsSpecialist;
import shishkin.cleanarchitecture.mvi.sl.SLUtil;
import shishkin.cleanarchitecture.mvi.sl.data.Result;
import shishkin.cleanarchitecture.mvi.sl.request.ResponseListener;

public class NotificationSpecialistImpl extends AbsSpecialist implements NotificationSpecialist, ResponseListener {

    public static final String NAME = NotificationSpecialistImpl.class.getName();

    private NotifivationDelegate mNotifivationDelegate = new NotifivationDelegate();
    private BalanceObserver mBalanceObserver = new BalanceObserver();

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
        mNotifivationDelegate.get(this).showBalance(list);
    }

    @Override
    public void clear() {
        mNotifivationDelegate.get(this).clear();
    }

    @Override
    public void getBalance() {
        Repository.getInstance().getBalance(this);
    }

    @Override
    public void onRegister() {
        SLUtil.register(mBalanceObserver);
        getBalance();
    }


    @Override
    public void response(Result result) {
        if (!result.hasError()) {
            showBalance((List<MviDao.Balance>) result.getData());
        }
    }


}
