package com.example.s1_0527.Widgets

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.security.identity.PersonalizationData
import android.widget.RemoteViews
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.example.s1_0527.HomeContainer
import com.example.s1_0527.R

/**
 * Implementation of App Widget functionality.
 */
class MyTicketsWidget : AppWidgetProvider() {
    override fun onUpdate(context: Context, appWidgetManager: AppWidgetManager, appWidgetIds: IntArray) {
        // There may be multiple widgets active, so update all of them
        for (appWidgetId in appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId)
        }

    }

    override fun onEnabled(context: Context) {
        // Enter relevant functionality for when the first widget is created
    }

    override fun onDisabled(context: Context) {
        // Enter relevant functionality for when the last widget is disabled
    }

    override fun onReceive(context: Context?, intent: Intent?) {
        super.onReceive(context, intent)
        if(intent!=null && intent.action.equals("com.example.widget.mytickets.click")){
            var intent=Intent(context, HomeContainer::class.java)
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            intent.putExtra("action","OpenMyTicket")
            ContextCompat.startActivity(context!!,intent,null)
        }
    }
}

internal fun updateAppWidget(context: Context, appWidgetManager: AppWidgetManager, appWidgetId: Int) {
    val widgetText = context.getString(R.string.appwidget_text)
    // Construct the RemoteViews object
    val views = RemoteViews(context.packageName, R.layout.my_tickets_widget)
    var intent=Intent(context,MyTicketsWidget::class.java)
    intent.action="com.example.widget.mytickets.click"
    var pendingIntent=PendingIntent.getBroadcast(context,0,intent,0)
    views.setOnClickPendingIntent(R.id.root,pendingIntent)

    // Instruct the widget manager to update the widget
    appWidgetManager.updateAppWidget(appWidgetId, views)
}