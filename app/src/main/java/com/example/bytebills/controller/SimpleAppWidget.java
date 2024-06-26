package com.example.bytebills.controller;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.RemoteViews;

import com.example.bytebills.R;

public class SimpleAppWidget extends AppWidgetProvider {

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    private void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                 int appWidgetId) {


        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.simple_app_widget);

        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://albert.com"));

        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);

        views.setOnClickPendingIntent(R.id.tvWidget, pendingIntent);

        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

}