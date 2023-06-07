package com.example.s1_0527.Widgets

import android.content.Context
import android.content.Intent
import android.widget.RemoteViews
import android.widget.RemoteViewsService
import com.example.s1_0527.R
import com.example.s1_0527.SqlMethod

class NewsListWidgetServices():RemoteViewsService() {
    override fun onGetViewFactory(intent: Intent?): RemoteViewsFactory {
    return NewsListWidgetFactory(applicationContext)
    }

    class NewsListWidgetFactory(var context:Context):RemoteViewsFactory{
        override fun onCreate() {}

        override fun onDataSetChanged() {}

        override fun onDestroy() {}

        override fun getCount(): Int {
            return SqlMethod.news(context).getAll().count
        }

        override fun getViewAt(position: Int): RemoteViews {
            var view=RemoteViews(context.packageName, R.layout.news_list_widgets_layout)
            var cursor=SqlMethod.news(context).getWidgetInfo()
            cursor.moveToFirst()
            for(i in 0 until cursor.count){
                if(i==position){
                    view.setTextViewText(R.id.date,cursor.getString(1))
                    view.setTextViewText(R.id.title,cursor.getString(2))
                    var id=cursor.getInt(0)
                    var intent=Intent()
                    intent.putExtra("allId",id)
                    intent.action="ToAllNews"
                    view.setOnClickFillInIntent(R.id.btn_all,intent)
                    break
                }
                cursor.moveToNext()
            }
            return view
        }

        override fun getLoadingView(): RemoteViews? {return null}

        override fun getViewTypeCount(): Int { return 1}

        override fun getItemId(position: Int): Long {
            var cursor=SqlMethod.news(context).getWidgetInfo()
            cursor.moveToFirst()
            for(i in 0 until cursor.count){
                if(i==position){
                    return cursor.getInt(0).toLong()
                }
                cursor.moveToNext()
            }
            return -1
        }

        override fun hasStableIds(): Boolean {return true }

    }
}