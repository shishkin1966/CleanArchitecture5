package shishkin.cleanarchitecture.mvi.app.notification;

import java.util.List;


import shishkin.cleanarchitecture.mvi.app.db.MviDao;
import shishkin.cleanarchitecture.mvi.sl.delegate.Delegating;

public interface NotificationDelegating extends Delegating {
    void showBalance(List<MviDao.Balance> list);

    void clear();


}
