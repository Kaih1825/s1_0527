package com.example.s1_0527.Adapters

import android.content.Context
import android.provider.ContactsContract.CommonDataKinds.Im
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.example.s1_0527.R

class HomeViewPagerAdapter(var context: Context):RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    class viewHolder(view:View):RecyclerView.ViewHolder(view){
        var img:ImageView
        init {
            img=view.findViewById(R.id.img)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return viewHolder(LayoutInflater.from(context).inflate(R.layout.home_viewpager,parent,false))
    }

    override fun getItemCount(): Int {
        return 2
    }

    var images= arrayListOf(R.drawable.banner1,R.drawable.banner2)
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        var v=holder as viewHolder
        v.img.setImageDrawable(context.getDrawable(images[position]))
    }
}