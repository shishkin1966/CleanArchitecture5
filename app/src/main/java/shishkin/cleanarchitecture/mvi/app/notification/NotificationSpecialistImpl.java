package shishkin.cleanarchitecture.mvi.app.notification;

import android.support.annotation.NonNull;


import java.util.List;


import shishkin.cleanarchitecture.mvi.app.db.MviDao;
import shishkin.cleanarchitecture.mvi.sl.AbsSpecialist;

public class NotificationSpecialistImpl extends AbsSpecialist implements NotificationSpecialist {

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
    public void showMessage(String title, String message) {
        mSpecialist.showMessage(title, message);
    }

    @Override
    public void clear() {
        mSpecialist.clear();
    }

}
