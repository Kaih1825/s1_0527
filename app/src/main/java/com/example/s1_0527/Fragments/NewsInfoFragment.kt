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
import com.example.s1_0527.R
import com.example.s1_0527.SqlMethod
import com.example.s1_0527.databinding.FragmentNewsInfoBinding
import org.json.JSONObject
import org.w3c.dom.Text

class NewsInfoFragment(var jsonObject: JSONObject?,var id:Int?) : Fragment() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var b=FragmentNewsInfoBinding.inflate(layoutInflater)
        if(jsonObject!=null){
            b.title.text=jsonObject!!.getString("title")
            b.type.text=jsonObject!!.getString("type")
            b.date.text=jsonObject!!.getString("date")
            b.authorType.text=resources.getStringArray(R.array.userType)[jsonObject!!.getString("authorType").toInt()-1]
            b.content.text=jsonObject!!.getString("content")
            try {
                var linkText=jsonObject!!.getString("linktext")
                var url=jsonObject!!.getString("url")
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
            try{
                var imageBytes= Base64.decode(jsonObject!!.getString("pics"), Base64.DEFAULT)
                val decorImage=BitmapFactory.decodeByteArray(imageBytes,0,imageBytes.size)
                var image=ImageView(requireContext())
                image.setImageBitmap(decorImage)
                b.rootView.addView(image)
            }catch (ex:java.lang.Exception){}
        }
        else{
            var cursor=SqlMethod.news(requireContext()).getNewsFromId(id!!)
            cursor.moveToFirst()
            b.title.text=cursor.getString(5)
            b.type.text=cursor.getString(6)
            b.date.text=cursor.getString(2)
            b.authorType.text=resources.getStringArray(R.array.userType)[cursor.getInt(8)-1]
            b.content.text=cursor.getString(1)
            try {
                var linkText=cursor.getString(3)
                var url=cursor.getString(7)
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
            try{
                var imageBytes= Base64.decode(cursor.getString(4), Base64.DEFAULT)
                val decorImage=BitmapFactory.decodeByteArray(imageBytes,0,imageBytes.size)
                var image=ImageView(requireContext())
                image.setImageBitmap(decorImage)
                b.rootView.addView(image)
            }catch (ex:java.lang.Exception){}
        }
        return b.root
    }
}