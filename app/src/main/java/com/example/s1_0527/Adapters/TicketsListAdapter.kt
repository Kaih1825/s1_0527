package com.example.s1_0527.Adapters

import android.content.Context
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.Color
import android.os.Build
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.view.drawToBitmap
import com.example.s1_0527.QRcodeDialog
import com.example.s1_0527.databinding.TicketsListBinding
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.util.logging.SimpleFormatter
import com.google.zxing.*
import java.io.File
import java.io.FileOutputStream

class TicketsListAdapter(var context: Context, var cursor: Cursor) : BaseAdapter() {
    override fun getCount(): Int {
        return cursor.count
    }

    override fun getItem(position: Int): Any {
        return 0
    }

    override fun getItemId(position: Int): Long {
        var idList = arrayListOf<Int>()
        cursor.moveToFirst()
        for (i in 0 until cursor.count) {
            idList.add(cursor.getInt(0))
            cursor.moveToNext()
        }
        return idList[position].toLong()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        var b = TicketsListBinding.inflate(LayoutInflater.from(context))
        cursor.moveToFirst()
        for (i in 0 until cursor.count) {
            if (i == position) {
                b.aud.text = "成人票${cursor.getInt(2)}張"
                b.chi.text = "兒童票${cursor.getInt(3)}張"
                b.date.text = cursor.getString(1).replace("/", "-")
                var qrCodeContext = "${b.date.text}\n成人${cursor.getInt(2)}\n兒童${cursor.getInt(3)}"
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
                b.qrCode.setImageBitmap(bitmap)
                b.qrCode.setOnClickListener {
                    QRcodeDialog(context, bitmap).show()
                }
                b.export.setOnClickListener {
                    var bi = b.infoLayout.drawToBitmap()
                    var file = File(
                        "/storage/emulated/0/Download/${
                            b.date.text.toString().replace("-", "")
                        }_${cursor.getInt(2)}_${cursor.getInt(3)}.png"
                    )
                    var op=FileOutputStream(file)
                    bi.compress(Bitmap.CompressFormat.PNG,86,op)
                    op.flush()
                    Toast.makeText(context,"Success",Toast.LENGTH_SHORT).show()
                }
                break
            } else cursor.moveToNext()
        }
        return b.root
    }
}