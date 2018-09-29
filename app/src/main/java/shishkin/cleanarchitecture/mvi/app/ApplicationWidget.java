package shishkin.cleanarchitecture.mvi.app;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;


import shishkin.cleanarchitecture.mvi.app.request.WidgetUpdateRequest;

/**
 * Created by Shishkin on 16.03.2018.
 */

public class ApplicationWidget extends AppWidgetProvider {

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        SLUtil.getRequestSpecialist().request(this, new WidgetUpdateRequest(appWidgetManager, appWidgetIds));
    }
}
