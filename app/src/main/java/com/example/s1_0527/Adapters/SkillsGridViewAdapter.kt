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

class SkillsGridViewAdapter(var context: Context, var imagesArray: List<String>, var textArray: List<String>) : BaseAdapter() {
    override fun getCount(): Int {
        return imagesArray.count()
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

        var inputStream = context.assets.open("professions/${imagesArray[position]}")
        var d = Drawable.createFromStream(inputStream, null)
        b.img.setImageDrawable(d)
        b.txt.text =
            "${textArray[position].substring(0, 3)}\n${textArray[position].substring(3, textArray[position].length)}"

        return b.root
    }
}
