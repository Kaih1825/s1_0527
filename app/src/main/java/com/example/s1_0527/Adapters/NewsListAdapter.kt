package com.example.s1_0527.Adapters

import android.content.Context
import android.support.v4.app.INotificationSideChannel
import android.util.Base64
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import com.example.s1_0527.databinding.NewsListBinding
import okhttp3.*
import okhttp3.internal.http.RequestLine
import org.json.JSONObject
import java.io.IOException

class NewsListAdapter(var context: Context,var jsonObjectArray:ArrayList<JSONObject>,var itemCount:Int):BaseAdapter() {
    override fun getCount(): Int {
        if(itemCount==-1){
            return jsonObjectArray.count()
        }
        if(itemCount>jsonObjectArray.count()) return jsonObjectArray.count()
        return itemCount
    }
    override fun getItem(position: Int): Any {
        return Any()
    }

    override fun getItemId(position: Int): Long {
        return 0
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        var b=NewsListBinding.inflate(LayoutInflater.from(context))
        b.date.text=jsonObjectArray[position].getString("date")
        b.title.text=jsonObjectArray[position].getString("title")
        b.type.text=jsonObjectArray[position].getString("type")
        return b.root
    }
}