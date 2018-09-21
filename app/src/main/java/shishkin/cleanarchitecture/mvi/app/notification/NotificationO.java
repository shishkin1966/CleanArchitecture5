package shishkin.cleanarchitecture.mvi.app.notification;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.support.v4.app.NotificationCompat;


import java.util.List;


import shishkin.cleanarchitecture.mvi.R;
import shishkin.cleanarchitecture.mvi.app.db.MviDao;
import shishkin.cleanarchitecture.mvi.common.utils.ApplicationUtils;
import shishkin.cleanarchitecture.mvi.sl.SLUtil;

public class NotificationO implements NotificationDelegating {

    private static final int NOTIFICATION_ID = 1;
    private String GROUP_NAME;
    private static final String CANAL_NAME = "Notification Service Canal";

    @Override
    public void showBalance(List<MviDao.Balance> list) {
        if (list == null || list.isEmpty()) {
            clear();
            return;
        }

        clear();

        final Context context = SLUtil.getContext();
        if (context == null) {
            return;
        }

        GROUP_NAME = SLUtil.getContext().getString(R.string.app_name);

        final NotificationManager nm = ApplicationUtils.getSystemService(context, Context.NOTIFICATION_SERVICE);
        if (nm == null) {
            return;
        }

        if (ApplicationUtils.hasO()) {
            NotificationChannel mChannel = nm.getNotificationChannel(GROUP_NAME);
            if (mChannel == null) {
                final int importance = NotificationManager.IMPORTANCE_LOW;
                mChannel = new NotificationChannel(GROUP_NAME, CANAL_NAME, importance);
                mChannel.enableLights(true);
                mChannel.setLightColor(R.color.red);
                nm.createNotificationChannel(mChannel);
            }
        }

        /*
        final Notification notificationMain = new NotificationCompat.Builder(context, GROUP_NAME)
                .setAutoCancel(true)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setGroup(GROUP_NAME)
                .setStyle(new NotificationCompat.MessagingStyle(GROUP_NAME))
                .setDefaults(0)
                .build();
        nm.notify(NOTIFICATION_ID, notificationMain);
        */

        for (MviDao.Balance balance : list) {
            final String message = String.format("%,.0f", balance.balance) + " " + balance.currency;
            final NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context, GROUP_NAME)
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setAutoCancel(true)
                    .setWhen(System.currentTimeMillis())
                    .setTicker(GROUP_NAME)
                    .setStyle(new NotificationCompat.BigTextStyle().bigText(message))
                    .setContentText(message)
                    .setGroupSummary(false)
                    .setDefaults(0)
                    .setGroup(GROUP_NAME);
            nm.notify(Long.valueOf(System.currentTimeMillis()).intValue(),
                    notificationBuilder.build());
        }
    }

    @Override
    public void clear() {
        final NotificationManager nm = ApplicationUtils.getSystemService(SLUtil.getContext(), Context.NOTIFICATION_SERVICE);
        if (nm != null) {
            nm.cancelAll();
        }
    }

    @Override
    public void processing(Object object) {

    }
}
