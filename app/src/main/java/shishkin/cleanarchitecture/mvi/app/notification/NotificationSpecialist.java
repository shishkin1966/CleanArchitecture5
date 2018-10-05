package shishkin.cleanarchitecture.mvi.app.notification;

public interface NotificationSpecialist {

    void showMessage(String title, String message);

    void replaceMessage(String title, String message);

    void clear();
}
