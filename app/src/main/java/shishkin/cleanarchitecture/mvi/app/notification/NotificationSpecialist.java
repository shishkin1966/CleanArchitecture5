package shishkin.cleanarchitecture.mvi.app.notification;

import java.util.List;


import shishkin.cleanarchitecture.mvi.app.db.MviDao;

public interface NotificationSpecialist {

    void showBalance(List<MviDao.Balance> list);

    void clear();

    void getBalance();
}
