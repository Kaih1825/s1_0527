package com.example.s1_0527.Widgets

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.widget.RemoteViews
import android.widget.Toast
import com.example.s1_0527.Fragments.NewsInfoFragment
import com.example.s1_0527.HomeContainer
import com.example.s1_0527.R

/**
 * Implementation of App Widget functionality.
 */
class NewsListWidget : AppWidgetProvider() {
    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        // There may be multiple widgets active, so update all of them
        for (appWidgetId in appWidgetIds) {
            updateNewsAppWidget(context, appWidgetManager, appWidgetId)
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
        if(intent!=null && intent.action=="com.example.widget.allId.click"){
            var id=intent.getIntExtra("allId",-1)
            if(id!=-1){
                var nIntent=Intent(context,HomeContainer::class.java)
                nIntent.putExtra("action","ToAllNews")
                nIntent.putExtra("allId",id)
                nIntent.flags=Intent.FLAG_ACTIVITY_NEW_TASK
                context!!.startActivity(nIntent)
            }
        }
    }
}

internal fun updateNewsAppWidget(
    context: Context,
    appWidgetManager: AppWidgetManager,
    appWidgetId: Int
) {
    val views = RemoteViews(context.packageName, R.layout.news_list_widget)
    var intent=Intent(context,NewsListWidgetServices::class.java)
    views.setRemoteAdapter(R.id.list,intent)

    var intent2=Intent(context,NewsListWidget::class.java)
    intent2.action="com.example.widget.allId.click"
    var pendingIntent2=PendingIntent.getBroadcast(context,0,intent2,0)
    views.setPendingIntentTemplate(R.id.list,pendingIntent2)

    // Instruct the widget manager to update the widget
    appWidgetManager.updateAppWidget(appWidgetId, views)
    appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetId,R.id.list)
}