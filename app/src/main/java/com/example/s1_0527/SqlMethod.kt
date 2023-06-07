package com.example.s1_0527

import android.appwidget.AppWidgetManager
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.util.Log
import android.view.View
import android.widget.RemoteViews
import com.example.s1_0527.Adapters.TicketsListAdapter
import com.example.s1_0527.Widgets.*
import okhttp3.internal.notifyAll
import java.util.jar.Attributes.Name

class SqlMethod {
    class userInfo(context: Context) {
        var db: SQLiteDatabase

        init {
            db = context.openOrCreateDatabase("db.db", Context.MODE_PRIVATE, null)
            try {
                db.execSQL("CREATE TABLE userInfo(id TEXT PRIMARY KEY,Username TEXT,Userpwd text,email TEXT,type INTEGER)")
                db.execSQL("INSERT INTO userInfo(id,Username,Userpwd,email,type) VALUES('account','name','Aa@1234','abc@mail.com',0)")
            } catch (ex: java.lang.Exception) {
                Log.e("Create Table Error", ex.toString())
            }
        }

        fun update(account: String, name: String, pwd: String, email: String, type: Int) {
            db.execSQL("UPDATE userInfo SET id='${account}',Username='${name}',Userpwd='${pwd}',email='${email}',type=${type}")
        }

        fun getType(): Int {
            var cursor = db.rawQuery("SELECT type FROM userInfo", null)
            cursor.moveToFirst()
            return cursor.getInt(0)
        }

        fun getAccount():String{
            var cursor = db.rawQuery("SELECT id FROM userInfo", null)
            cursor.moveToFirst()
            return cursor.getString(0)
        }

        fun logout(){
            db.execSQL("UPDATE userInfo SET id='account',Username='name',Userpwd='pwd',email='email',type=-1")
        }
    }

    class tickets(var context: Context) {
        var db: SQLiteDatabase = context.openOrCreateDatabase("db.db", Context.MODE_PRIVATE, null)

        init {
            try {
                db.execSQL("CREATE TABLE tickets(id INTEGER PRIMARY KEY,date TEXT,ad INTEGER,chi INTEGER)")
            } catch (ex: java.lang.Exception) {
                Log.e("Create Table Error", ex.toString())
            }
        }

        fun insert(date: String, ad: Int, chi: Int) {
            db.execSQL("INSERT INTO tickets(date,ad,chi) VALUES('${date}',${ad},${chi})")
            updateWidget(context)
        }

        fun updateWidget(context: Context) {
            val appWidgetManager = AppWidgetManager.getInstance(context)
            val appWidgetIds = appWidgetManager.getAppWidgetIds(
                ComponentName(
                    context,
                    MyTicetsListWidgets::class.java
                )
            )

            MyTicetsListWidgets().onUpdate(context, appWidgetManager, appWidgetIds)
        }

        fun getAll(): Cursor {
            return db.rawQuery("SELECT * FROM tickets", null)
        }
    }

    class news(var context: Context) {
        var db = context.openOrCreateDatabase("db.db", Context.MODE_PRIVATE, null)

        init {
            try {
                db.execSQL("CREATE TABLE news(id INTEGER PRIMARY KEY,content TEXT,date TEXT,linktext TEXT,pics TEXT,title TEXT,type TEXT,url TEXT,authorType INTEGER,isStar INTEGER)")
            } catch (ex: Exception) {

            }
        }

        fun insert(
            id: Int,
            content: String,
            date: String,
            linktext: String,
            pics: String,
            title: String,
            type: String,
            url: String,
            authorType: Int
        ) {
            try {
                db.execSQL("INSERT INTO news(id,content,date,linktext,pics,title,type,url,authorType,isStar) VALUES(${id},'${content}','${date}','${linktext}','${pics}','${title}','${type}','${url}',${authorType},0)")
            } catch (ex: java.lang.Exception) {
            }
            upDateWidget()
        }

        fun insertNoId(
            content: String,
            date: String,
            linktext: String,
            pics: String,
            title: String,
            type: String,
            url: String,
            authorType: Int
        ) {
            try {
                db.execSQL("INSERT INTO news(content,date,linktext,pics,title,type,url,authorType,isStar) VALUES('${content}','${date}','${linktext}','${pics}','${title}','${type}','${url}',${authorType},0)")
            } catch (ex: java.lang.Exception) {
            }
            upDateWidget()
        }

        fun getAll(): Cursor {
            return db.rawQuery("SELECT * FROM news", null)
        }

        fun getWidgetInfo(): Cursor {
            return db.rawQuery("SELECT id,date,title FROM news", null)
        }

        fun getNewsFromId(id: Int): Cursor {
            return db.rawQuery(
                "SELECT id,content,date,linktext,pics,title,type,url,authorType FROM news WHERE id = $id",
                null
            )
        }

        fun setStar(isStar:Int,id: Int){
            db.execSQL("UPDATE news SET isStar=$isStar WHERE id=$id")
        }

        fun getStar(id:Int):Boolean{
            var cursor=db.rawQuery("SELECT isStar FROM news WHERE id=$id",null)
            cursor.moveToFirst()
            return cursor.getInt(0)==1
        }

        fun getStarID():Cursor{
            return db.rawQuery("SELECT * FROM news WHERE isStar=1",null)
        }

        fun updateNews(
            id: Int,
            content: String,
            linktext: String,
            pics: String,
            title: String,
            type: String,
            url: String
        ) {
            Log.e("TAG", "updateNews: ", )
            db.execSQL("UPDATE news SET content='$content',linktext='$linktext',pics='$pics',title='$title',type='$type',url='$url' WHERE id=$id")
            upDateWidget()
        }


        fun upDateWidget() {
            var widgetManager = AppWidgetManager.getInstance(context)
            var appWidgetId =
                widgetManager.getAppWidgetIds(ComponentName(context, NewsListWidget::class.java))
            NewsListWidget().onUpdate(context,widgetManager,appWidgetId)
        }
    }
}