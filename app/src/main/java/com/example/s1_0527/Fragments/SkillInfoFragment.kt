package com.example.s1_0527.Fragments

import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.SimpleAdapter
import com.example.s1_0527.R
import com.example.s1_0527.databinding.FragmentSkillInfoBinding


class SkillInfoFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var b=FragmentSkillInfoBinding.inflate(layoutInflater)
        val type= arrayOf("全部","製造工程項目","營建技術","資訊與通訊技術","運輸與物流","社會與個人服務","藝術與時尚","青少年組","未來職類")

        val items= arrayListOf<HashMap<String,Any>>()

        for(i in 0 until type.count()){
            var item=HashMap<String,Any>()
            item["name"]=type[i]
            items.add(item)
        }

        val button= arrayOf(b.btn1,b.btn2,b.btn3,b.btn4,b.btn5,b.btn6,b.btn7,b.btn8,b.btn9)
        val txt= arrayOf(b.text1,b.text2,b.text3,b.text4,b.text5,b.text6,b.text7,b.text8,b.text9)

        for(i in 0 until button.count()){
            button[i].setOnClickListener {
                for(j in 0 until txt.count()){
                    button[j].setCardBackgroundColor(Color.WHITE)
                    txt[j].setTextColor(Color.BLACK)
                }
                button[i].setCardBackgroundColor(Color.BLACK)
                txt[i].setTextColor(Color.WHITE)
            }
        }
        return b.root
    }
}