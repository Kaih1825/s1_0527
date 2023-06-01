package com.example.s1_0527.Fragments

import android.app.Notification.Action
import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.Paint
import android.net.Uri
import android.os.Bundle
import android.text.TextPaint
import android.util.Base64
import android.util.JsonReader
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.marginTop
import androidx.fragment.app.Fragment
import com.example.s1_0527.databinding.FragmentNewsInfoBinding
import org.json.JSONObject
import org.w3c.dom.Text

class NewsInfoFragment(var jsonObject: JSONObject) : Fragment() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var b=FragmentNewsInfoBinding.inflate(layoutInflater)
        b.title.text=jsonObject.getString("title")
        b.type.text=jsonObject.getString("type")
        b.date.text=jsonObject.getString("date")
        b.authorType.text=jsonObject.getString("authorType")
        b.content.text=jsonObject.getString("content")
        try {
            var linkText=jsonObject.getString("linktext")
            var url=jsonObject.getString("url")
            var linkTextArray=linkText.split("&,&")
            var urlArray=url.split("&,&")
            for(i in 0 until linkTextArray.count()){
                var tisTextView=TextView(requireContext())
                tisTextView.paintFlags=TextPaint.UNDERLINE_TEXT_FLAG
                tisTextView.setTextColor(Color.BLUE)
                tisTextView.setOnClickListener {
                    var intent=Intent(Intent.ACTION_VIEW)
                    intent.data=Uri.parse(urlArray[i])
                    startActivity(intent)
                }
                tisTextView.text=linkTextArray[i]+"\n"
                b.rootView.addView(tisTextView)
            }
        }catch (ex:java.lang.Exception){}
        var imageBytes= Base64.decode(jsonObject.getString("pics"), Base64.DEFAULT)
        val decorImage=BitmapFactory.decodeByteArray(imageBytes,0,imageBytes.size)
        var image=ImageView(requireContext())
        image.setImageBitmap(decorImage)
        b.rootView.addView(image)
        return b.root
    }
}