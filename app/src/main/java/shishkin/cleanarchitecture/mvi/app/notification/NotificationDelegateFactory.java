package shishkin.cleanarchitecture.mvi.app.notification;

import shishkin.cleanarchitecture.mvi.common.utils.ApplicationUtils;
import shishkin.cleanarchitecture.mvi.sl.delegate.DelegatingFactory;

public class NotificationDelegateFactory implements DelegatingFactory<NotificationDelegating> {
    @Override
    public NotificationDelegating create(Object object) {
        if (ApplicationUtils.hasO()) {
            return new NotificationO();
        } else if (ApplicationUtils.hasN()) {
            return new NotificationN();
        } else if (ApplicationUtils.hasMarshmallow()) {
            return new NotificationM();
        }
        return new Notification();
    }
}
