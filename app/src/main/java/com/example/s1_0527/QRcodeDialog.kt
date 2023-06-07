package com.example.s1_0527

import android.app.Dialog
import android.content.Context
import android.graphics.Bitmap
import android.os.Bundle
import com.example.s1_0527.databinding.QrcodeDialogBinding

class QRcodeDialog(context: Context,var bitmap: Bitmap):Dialog(context) {
    lateinit var b:QrcodeDialogBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        b= QrcodeDialogBinding.inflate(layoutInflater)
        setContentView(b.root)
        window!!.setBackgroundDrawable(null)
        b.qrcode.setImageBitmap(bitmap)
    }

    override fun show() {
        super.show()
    }

    override fun cancel() {
        super.cancel()
    }
}