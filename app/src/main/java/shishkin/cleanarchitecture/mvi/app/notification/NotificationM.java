package shishkin.cleanarchitecture.mvi.app.notification;

import android.app.NotificationManager;
import android.content.Context;
import android.support.v4.app.NotificationCompat;


import java.util.List;


import shishkin.cleanarchitecture.mvi.R;
import shishkin.cleanarchitecture.mvi.app.SLUtil;
import shishkin.cleanarchitecture.mvi.app.db.MviDao;
import shishkin.cleanarchitecture.mvi.common.utils.ApplicationUtils;

public class NotificationM implements NotificationSpecialist {

    private static final String GROUP_NAME = SLUtil.getContext().getString(R.string.app_name);

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

        final NotificationManager nm = ApplicationUtils.getSystemService(context, Context.NOTIFICATION_SERVICE);
        if (nm == null) {
            return;
        }


        final StringBuilder sb = new StringBuilder();
        for (MviDao.Balance balance : list) {
            sb.append(String.format("%,.0f", balance.balance) + " " + balance.currency);
            sb.append("\n");
        }
        final NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context, GROUP_NAME)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setAutoCancel(true)
                .setWhen(System.currentTimeMillis())
                .setDefaults(0)
                .setContentTitle(context.getString(R.string.fragment_account_balance))
                .setStyle(new NotificationCompat.BigTextStyle().bigText(sb.toString()))
                .setGroup(GROUP_NAME)
                .setContentText(sb.toString());
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
