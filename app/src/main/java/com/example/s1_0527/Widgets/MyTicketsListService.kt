package com.example.s1_0527.Widgets

import android.annotation.SuppressLint
import android.appwidget.AppWidgetManager
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.widget.RemoteViews
import android.widget.RemoteViewsService
import android.widget.Toast
import androidx.core.view.drawToBitmap
import com.example.s1_0527.QRcodeDialog
import com.example.s1_0527.R
import com.example.s1_0527.SqlMethod
import com.google.zxing.BarcodeFormat
import com.google.zxing.EncodeHintType
import com.google.zxing.MultiFormatWriter
import java.io.File
import java.io.FileOutputStream

class MyTicketsListService:RemoteViewsService() {
    override fun onGetViewFactory(intent: Intent?): RemoteViewsFactory {
        return MyTicketListViewFactory(applicationContext)
    }

    class MyTicketListViewFactory(var context: Context): RemoteViewsFactory {
        override fun onCreate() {}

        override fun onDataSetChanged() {}

        override fun onDestroy() {}

        override fun getCount(): Int {
            return SqlMethod.tickets(context).getAll().count
//            return 10
        }

        @SuppressLint("RemoteViewLayout")
        override fun getViewAt(position: Int): RemoteViews {
            var cursor=SqlMethod.tickets(context).getAll()
            cursor.moveToFirst()
            for(i in 0 until cursor.count){
                if(i==position) break
                else cursor.moveToNext()
            }
            var view=RemoteViews(context.packageName, R.layout.tickets_list_widget)
            view.setTextViewText(R.id.date,"日期：${cursor.getString(1).replace("/","-")}")
            view.setTextViewText(R.id.aud,"成人票：${cursor.getInt(2)}張")
            view.setTextViewText(R.id.chi,"兒童票：${cursor.getInt(3)}張")
            var qrCodeContext = "${cursor.getString(1).replace("/","-")}\n成人${cursor.getInt(2)}\n兒童${cursor.getInt(3)}"
            var hints = mapOf(EncodeHintType.CHARACTER_SET to "UTF-8")
            var martix = MultiFormatWriter().encode(
                qrCodeContext,
                BarcodeFormat.QR_CODE,
                600,
                600,
                hints
            )
            val rawData = IntArray(360000)
            for (w in 0 until 600) {
                for (h in 0 until 600) {
                    var color = Color.WHITE
                    if (martix[w, h]) {
                        color = Color.BLACK
                    }
                    rawData[600 * h + w] = color
                }
            }
            var bitmap = Bitmap.createBitmap(rawData, 600, 600, Bitmap.Config.ARGB_8888)
            view.setImageViewBitmap(R.id.qrCode,bitmap)
            return view
        }

        override fun getLoadingView(): RemoteViews? {return null}

        override fun getViewTypeCount(): Int {return 1}

        override fun getItemId(position: Int): Long {return position.toLong()}

        override fun hasStableIds(): Boolean {return true}
    }

}