package shishkin.cleanarchitecture.mvi.app.notification;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.app.NotificationCompat;


import java.util.List;


import shishkin.cleanarchitecture.mvi.R;
import shishkin.cleanarchitecture.mvi.app.SLUtil;
import shishkin.cleanarchitecture.mvi.app.db.MviDao;
import shishkin.cleanarchitecture.mvi.app.observe.AccountObserver;
import shishkin.cleanarchitecture.mvi.app.screen.activity.main.MainActivity;
import shishkin.cleanarchitecture.mvi.common.utils.ApplicationUtils;
import shishkin.cleanarchitecture.mvi.common.utils.StringUtils;
import shishkin.cleanarchitecture.mvi.sl.ApplicationSpecialistImpl;

@RequiresApi(Build.VERSION_CODES.O)
public class NotificationO implements NotificationSpecialist {

    private static final String GROUP_NAME = SLUtil.getContext().getString(R.string.app_name);
    private static final String CANAL_NAME = "Notification Service Canal";

    @Override
    public void showBalance(List<MviDao.Balance> list) {
        final Context context = SLUtil.getContext();
        if (context == null) {
            return;
        }

        if (list == null || list.isEmpty()) {
            clear();
            return;
        }

        clear();

        String title = ApplicationSpecialistImpl.getInstance().getString(R.string.fragment_account_balance);
        final StringBuilder sb = new StringBuilder();
        for (int i = 0; i < list.size(); i++) {
            sb.append(String.format("%,.0f", list.get(i).balance) + " " + list.get(i).currency);
            if (i < list.size() - 1) {
                sb.append("\n");
            }
        }

        showMessage(title, sb.toString());
    }

    @Override
    public void showMessage(String title, String message) {
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

        NotificationChannel mChannel = nm.getNotificationChannel(GROUP_NAME);
        if (mChannel == null) {
            mChannel = new NotificationChannel(GROUP_NAME, CANAL_NAME, NotificationManager.IMPORTANCE_LOW);
            mChannel.enableLights(true);
            mChannel.setLightColor(R.color.red);
            nm.createNotificationChannel(mChannel);
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
                .setStyle(new NotificationCompat.BigTextStyle().bigText(message))
                .setGroup(GROUP_NAME)
                .setContentText(message);
        nm.notify(Long.valueOf(System.currentTimeMillis()).intValue(),
                notificationBuilder.build());
    }


    @Override
    public void clear() {
        final NotificationManager nm = ApplicationUtils.getSystemService(SLUtil.getContext(), Context.NOTIFICATION_SERVICE);
        if (nm != null) {
            nm.cancelAll();
        }
    }
}
