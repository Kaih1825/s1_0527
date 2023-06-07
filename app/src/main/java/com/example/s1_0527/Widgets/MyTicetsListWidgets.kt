package com.example.s1_0527.Widgets

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.opengl.Visibility
import android.view.View
import android.widget.RemoteViews
import android.widget.Switch
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.example.s1_0527.HomeContainer
import com.example.s1_0527.R
import com.example.s1_0527.SqlMethod

/**
 * Implementation of App Widget functionality.
 */
class MyTicetsListWidgets : AppWidgetProvider() {
    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        // There may be multiple widgets active, so update all of them
        for (appWidgetId in appWidgetIds) {
            updateMyTicketsListWidget(context, appWidgetManager, appWidgetId)
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
        if(intent!=null){
            if(intent.action.equals("com.example.widget.butTicket.click")){
                var intent=Intent(context, HomeContainer::class.java)
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                intent.putExtra("action","BuyTickets")
                ContextCompat.startActivity(context!!,intent,null)
            }else if(intent.action.equals("com.example.widget.holeTicket.click")){
                var intent=Intent(context, HomeContainer::class.java)
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                intent.putExtra("action","OpenMyTicket")
                ContextCompat.startActivity(context!!,intent,null)
            }
        }
    }
}

internal fun updateMyTicketsListWidget(
    context: Context,
    appWidgetManager: AppWidgetManager,
    appWidgetId: Int
) {
    // Construct the RemoteViews object
    val views = RemoteViews(context.packageName, R.layout.my_ticets_list_widgets)
    var listIntent= Intent(context,MyTicketsListService::class.java)
    views.setRemoteAdapter(R.id.listView,listIntent)

    if(SqlMethod.tickets(context).getAll().count==0){
        views.setViewVisibility(R.id.hasRecord, View.GONE)
        views.setViewVisibility(R.id.noRecord,View.VISIBLE)
    }else{
        views.setViewVisibility(R.id.hasRecord, View.VISIBLE)
        views.setViewVisibility(R.id.noRecord,View.GONE)
    }

    var intent=Intent(context,MyTicetsListWidgets::class.java)
    intent.action="com.example.widget.butTicket.click"
    var pendingIntent=PendingIntent.getBroadcast(context,0,intent,0)
    views.setOnClickPendingIntent(R.id.buyTickets,pendingIntent)

    var intent2=Intent(context,MyTicetsListWidgets::class.java)
    intent2.action="com.example.widget.holeTicket.click"
    var pendingIntent2=PendingIntent.getBroadcast(context,0,intent2,0)
    views.setOnClickPendingIntent(R.id.holeTicket,pendingIntent2)


    // Instruct the widget manager to update the widget
    appWidgetManager.updateAppWidget(appWidgetId, views)
    appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetId,R.id.listView)
}