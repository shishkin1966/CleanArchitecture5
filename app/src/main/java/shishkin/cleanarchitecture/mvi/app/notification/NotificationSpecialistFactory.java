package shishkin.cleanarchitecture.mvi.app.notification;

import shishkin.cleanarchitecture.mvi.common.utils.ApplicationUtils;

public class NotificationSpecialistFactory {

    public static NotificationSpecialist get() {
        if (ApplicationUtils.hasO()) {
            return new NotificationO();
        } else if (ApplicationUtils.hasMarshmallow()) {
            return new NotificationM();
        }
        return new Notification();
    }
}
