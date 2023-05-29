package com.example.s1_0527.Adapters

import android.app.Activity
import android.content.Context
import android.graphics.drawable.Drawable
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import com.example.s1_0527.R
import com.example.s1_0527.databinding.SlillGridviewBinding
import org.json.JSONObject

class SkillsGridViewAdapter(var context: Context, var jsonObject:List<JSONObject>) : BaseAdapter() {
    override fun getCount(): Int {
        return jsonObject.count()
    }

    override fun getItem(position: Int): Any {
        return 0
    }

    override fun getItemId(position: Int): Long {
        return 0
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        var b = SlillGridviewBinding.inflate((context as Activity).layoutInflater)

        val layoutParams = RelativeLayout.LayoutParams(280, 280)

        b.root.layoutParams = layoutParams

        b.img.setImageDrawable(null)

        var inputStream = context.assets.open("professions/${jsonObject[position].getString("image")}")
        var d = Drawable.createFromStream(inputStream, null)
        b.img.setImageDrawable(d)
        var text=jsonObject[position].getString("title")
        b.txt.text =
            "${text.substring(0, 3)}\n${text.substring(3,text.length)}"

        return b.root
    }
}
