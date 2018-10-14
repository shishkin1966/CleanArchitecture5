package shishkin.cleanarchitecture.mvi.app.specialist.notification;

public interface NotificationSpecialist {

    void showMessage(String title, String message);

    void replaceMessage(String title, String message);

    void clear();
}
