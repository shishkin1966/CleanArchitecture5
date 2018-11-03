package shishkin.cleanarchitecture.mvi.app.specialist.notification;

import androidx.annotation.NonNull;
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
    public void showMessage(String title, String message) {
        mSpecialist.showMessage(title, message);
    }

    @Override
    public void replaceMessage(String title, String message) {
        mSpecialist.replaceMessage(title, message);
    }

    @Override
    public void clear() {
        mSpecialist.clear();
    }

}
