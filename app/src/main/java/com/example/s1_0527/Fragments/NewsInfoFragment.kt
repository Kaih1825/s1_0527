package com.example.s1_0527.Fragments

import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.Paint
import android.net.Uri
import android.os.Bundle
import android.util.Base64
import android.util.JsonReader
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.s1_0527.databinding.FragmentNewsInfoBinding
import org.json.JSONObject

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
        b.type.text=jsonObject.getString("text")
        b.date.text=jsonObject.getString("date")
        b.authorType.text=jsonObject.getString("authorType")
        b.content.text=jsonObject.getString("content")
//        for(i in 0 until linkArray.count()){
//            var textView=TextView(requireContext())
//            textView.text=linkArray[i]["linktext"]
//            textView.setOnClickListener {
//                var intent= Intent()
//                intent.action=Intent.ACTION_VIEW
//                intent.data= Uri.parse(linkArray[i]["url"])
//                startActivity(intent)
//            }
//            textView.setTextColor(Color.BLUE)
//            textView.paintFlags=Paint.UNDERLINE_TEXT_FLAG
//            b.hyperLink.addView(textView)
//        }
        var imageBytes= Base64.decode(jsonObject.getString("pics"), Base64.DEFAULT)
        val decorImage=BitmapFactory.decodeByteArray(imageBytes,0,imageBytes.size)
        b.img.setImageBitmap(decorImage)
        return b.root
    }
}