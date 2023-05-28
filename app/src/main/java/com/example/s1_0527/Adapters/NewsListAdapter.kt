package com.example.s1_0527.Adapters

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import com.example.s1_0527.databinding.NewsListBinding
import okhttp3.*
import okhttp3.internal.http.RequestLine
import java.io.IOException

class NewsListAdapter(var context: Context,var itemCount:Int,var dateArray: ArrayList<String>,var typpeArray:ArrayList<String>,var titleArray:ArrayList<String>,var idArray:java.util.ArrayList<Int>):BaseAdapter() {
    override fun getCount(): Int {
        if(itemCount==-1){
            return dateArray.count()
        }
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
        b.date.text=dateArray[position]
        b.title.text=titleArray[position]
        b.type.text=typpeArray[position]

        return b.root
    }
}