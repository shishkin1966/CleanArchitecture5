package shishkin.cleanarchitecture.mvi.app.request;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;


import java.util.List;


import shishkin.cleanarchitecture.mvi.R;
import shishkin.cleanarchitecture.mvi.app.ApplicationController;
import shishkin.cleanarchitecture.mvi.app.SLUtil;
import shishkin.cleanarchitecture.mvi.app.db.MviDao;
import shishkin.cleanarchitecture.mvi.app.db.MviDb;
import shishkin.cleanarchitecture.mvi.app.observe.AccountObserver;
import shishkin.cleanarchitecture.mvi.app.screen.activity.main.MainActivity;
import shishkin.cleanarchitecture.mvi.common.utils.ApplicationUtils;
import shishkin.cleanarchitecture.mvi.sl.request.AbsRequest;

/**
 * Created by Shishkin on 16.03.2018.
 */

public class WidgetUpdateRequest extends AbsRequest {

    private AppWidgetManager appWidgetManager;
    private int[] appWidgetIds;

    public WidgetUpdateRequest(AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        this.appWidgetManager = appWidgetManager;
        this.appWidgetIds = appWidgetIds;
    }

    @Override
    public String getName() {
        return getClass().getName();
    }

    @Override
    public boolean isDistinct() {
        return false;
    }

    @Override
    public void run() {
        if (appWidgetManager == null) return;

        final Context context = ApplicationController.getInstance();
        if (context == null) return;

        final MviDb db = SLUtil.getDb();
        if (db == null) return;

        List<MviDao.Balance> list = db.MviDao().getBalance();
        if (list == null) {
            return;
        }

        final StringBuilder sb = new StringBuilder();
        for (int i = 0; i < list.size(); i++) {
            sb.append(String.format("%,.0f", list.get(i).balance) + " " + list.get(i).currency);
            if (i < list.size() - 1) {
                sb.append("\n");
            }
        }

        ApplicationUtils.runOnUiThread(() -> {
            for (int i = 0; i < appWidgetIds.length; i++) {
                int appWidgetId = appWidgetIds[i];

                final Intent intent = new Intent(context, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                intent.setAction(AccountObserver.ACTION_CLICK);
                final PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);

                final RemoteViews remoteView = new RemoteViews(context.getPackageName(), R.layout.widget_layout);
                remoteView.setOnClickPendingIntent(R.id.ll, pendingIntent);

                remoteView.setTextViewText(R.id.widget, sb.toString());
                appWidgetManager.updateAppWidget(appWidgetId, remoteView);
            }
        });
    }
}
