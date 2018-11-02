package shishkin.cleanarchitecture.mvi.app.specialist.notification;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import androidx.core.app.NotificationCompat;


import shishkin.cleanarchitecture.mvi.R;
import shishkin.cleanarchitecture.mvi.app.SLUtil;
import shishkin.cleanarchitecture.mvi.app.observe.AccountObserver;
import shishkin.cleanarchitecture.mvi.app.screen.activity.main.MainActivity;
import shishkin.cleanarchitecture.mvi.common.utils.ApplicationUtils;
import shishkin.cleanarchitecture.mvi.common.utils.StringUtils;

public class Notification implements NotificationSpecialist {

    private static final String GROUP_NAME = SLUtil.getContext().getString(R.string.app_name);

    private int id = -1;

    @Override
    public void showMessage(String title, String message) {
        id = Long.valueOf(System.currentTimeMillis()).intValue();
        show(title, message);
    }

    @Override
    public void replaceMessage(String title, String message) {
        if (id == -1) {
            id = Long.valueOf(System.currentTimeMillis()).intValue();
        }
        show(title, message);
    }

    private void show(String title, String message) {
        if (StringUtils.isNullOrEmpty(message)) {
            clear();
            return;
        }

        final Context context = SLUtil.getContext();
        if (context == null) {
            return;
        }

        final NotificationManager nm = ApplicationUtils.getSystemService(context, Context.NOTIFICATION_SERVICE);
        if (nm == null) {
            return;
        }

        final Intent intent = new Intent(context, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        intent.setAction(AccountObserver.ACTION_CLICK);
        final PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_ONE_SHOT);

        final NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context, GROUP_NAME)
                .setContentIntent(pendingIntent)
                .setSmallIcon(R.drawable.ic_cat)
                .setAutoCancel(true)
                .setWhen(System.currentTimeMillis())
                .setDefaults(0)
                .setContentTitle(title)
                .setContentText(message);
        nm.notify(id, notificationBuilder.build());
    }

    @Override
    public void clear() {
        final NotificationManager nm = ApplicationUtils.getSystemService(SLUtil.getContext(), Context.NOTIFICATION_SERVICE);
        if (nm != null) {
            nm.cancelAll();
        }
    }
}
