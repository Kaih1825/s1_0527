package com.example.s1_0527.Adapters

import android.app.Activity
import android.content.Context
import android.graphics.drawable.Drawable
import android.support.v4.app.INotificationSideChannel
import android.util.Base64
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.example.s1_0527.Fragments.EditNews
import com.example.s1_0527.R
import com.example.s1_0527.SqlMethod
import com.example.s1_0527.databinding.MoreNewsLayoutBinding
import com.example.s1_0527.databinding.NewsListBinding
import com.example.s1_0527.databinding.StarNewsLayoutBinding
import okhttp3.*
import okhttp3.internal.http.RequestLine
import org.json.JSONObject
import java.io.IOException

class NewsListAdapter(var context: Context,var jsonObjectArray:ArrayList<JSONObject>,var itemCount:Int):BaseAdapter() {
    override fun getCount(): Int {
        if(itemCount==-1){
            return jsonObjectArray.count()
        }
        if(itemCount==-2){
            return SqlMethod.news(context).getStarID().count
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

        var sp = context.getSharedPreferences("user", Context.MODE_PRIVATE)
        if (sp.getBoolean("login", false) && itemCount==-1) {
            if(SqlMethod.userInfo(context).getType()==2){
                var b = StarNewsLayoutBinding.inflate(LayoutInflater.from(context))
                b.date.text = jsonObjectArray[position].getString("date")
                b.title.text = jsonObjectArray[position].getString("title")
                b.type.text = jsonObjectArray[position].getString("type")
                if(SqlMethod.news(context).getStar(jsonObjectArray[position].getInt("id"))){
                    b.star.background=context.getDrawable(R.drawable.baseline_star_24)
                }else{
                    b.star.background=context.getDrawable(R.drawable.baseline_star_border_24)
                }

                b.star.setOnClickListener {
                    var isStar=SqlMethod.news(context).getStar(jsonObjectArray[position].getInt("id"))
                    SqlMethod.news(context).setStar(if(isStar)0 else 1,jsonObjectArray[position].getInt("id"))
                    if(SqlMethod.news(context).getStar(jsonObjectArray[position].getInt("id"))){
                        b.star.background=context.getDrawable(R.drawable.baseline_star_24)
                    }else{
                        b.star.background=context.getDrawable(R.drawable.baseline_star_border_24)
                    }
                }
                return b.root
            }
            if (SqlMethod.userInfo(context).getType()+1 == jsonObjectArray[position].getInt("authorType")) {
                var b = MoreNewsLayoutBinding.inflate(LayoutInflater.from(context))
                b.date.text = jsonObjectArray[position].getString("date")
                b.title.text = jsonObjectArray[position].getString("title")
                b.type.text = jsonObjectArray[position].getString("type")
                b.btnEdit.setOnClickListener {
                    var fm=(context as AppCompatActivity).supportFragmentManager.beginTransaction()
                    fm.addToBackStack(fm.javaClass.name)
                    fm.replace(R.id.layout,EditNews(jsonObjectArray[position].getInt("id"))).commit()
                }
                return b.root
            }

        }
        else if(sp.getBoolean("login", false) && itemCount==-2){
            var b = StarNewsLayoutBinding.inflate(LayoutInflater.from(context))
            var cursor=SqlMethod.news(context).getStarID()
            var id=0
            cursor.moveToFirst()
            for (i in 0 until cursor.count){
                if(i==position){
                    id=cursor.getInt(0)
                    b.date.text=cursor.getString(2)
                    b.title.text=cursor.getString(5)
                    b.type.text=cursor.getString(6)
                    break
                }
                cursor.moveToNext()
            }

            if(SqlMethod.news(context).getStar(id)){
                b.star.background=context.getDrawable(R.drawable.baseline_star_24)
            }else{
                b.star.background=context.getDrawable(R.drawable.baseline_star_border_24)
            }

            b.star.setOnClickListener {
                var isStar=SqlMethod.news(context).getStar(id)
                SqlMethod.news(context).setStar(if(isStar)0 else 1,id)
                if(SqlMethod.news(context).getStar(id)){
                    b.star.background=context.getDrawable(R.drawable.baseline_star_24)
                }else{
                    b.star.background=context.getDrawable(R.drawable.baseline_star_border_24)
                }
            }
            return b.root
        }
        else {
            var b=NewsListBinding.inflate(LayoutInflater.from(context))
            b.date.text=jsonObjectArray[position].getString("date")
            b.title.text=jsonObjectArray[position].getString("title")
            b.type.text=jsonObjectArray[position].getString("type")
            return b.root
        }
        var b=NewsListBinding.inflate(LayoutInflater.from(context))
        b.date.text=jsonObjectArray[position].getString("date")
        b.title.text=jsonObjectArray[position].getString("title")
        b.type.text=jsonObjectArray[position].getString("type")
        return b.root
    }
}