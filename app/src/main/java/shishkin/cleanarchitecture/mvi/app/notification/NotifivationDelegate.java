package shishkin.cleanarchitecture.mvi.app.notification;

import shishkin.cleanarchitecture.mvi.sl.delegate.AbsDelegate;
import shishkin.cleanarchitecture.mvi.sl.delegate.DelegatingFactory;

public class NotifivationDelegate extends AbsDelegate<NotificationDelegating> {
    @Override
    public DelegatingFactory<NotificationDelegating> getDelegateFactory() {
        return new NotificationDelegateFactory();
    }
}
