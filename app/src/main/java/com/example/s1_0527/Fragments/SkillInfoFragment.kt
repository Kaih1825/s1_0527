package com.example.s1_0527.Fragments

import android.animation.Animator
import android.animation.ValueAnimator
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import androidx.fragment.app.Fragment
import com.example.s1_0527.Adapters.SkillsGridViewAdapter
import com.example.s1_0527.R
import com.example.s1_0527.databinding.FragmentSkillInfoBinding
import org.json.JSONArray
import org.json.JSONObject
import java.io.InputStream
import kotlin.math.log


class SkillInfoFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var b = FragmentSkillInfoBinding.inflate(layoutInflater)
        val type =
            arrayOf("全部", "製造工程項目", "營建技術", "資訊與通訊技術", "運輸與物流", "社會與個人服務", "藝術與時尚", "青少年組", "未來職類")
        val items = arrayListOf<HashMap<String, Any>>()
        for (i in 0 until type.count()) {
            var item = HashMap<String, Any>()
            item["name"] = type[i]
            items.add(item)
        }
        val button = arrayOf(b.btn1, b.btn2, b.btn3, b.btn4, b.btn5, b.btn6, b.btn7, b.btn8, b.btn9)
        val txt =
            arrayOf(b.text1, b.text2, b.text3, b.text4, b.text5, b.text6, b.text7, b.text8, b.text9)
        var nowType=0



        var jsonText = requireContext().assets.open("professions.json").bufferedReader().readText()
        var jsonArrayAll = JSONArray(jsonText)
        var jsonObjectArray= arrayListOf<JSONObject>()
        for (i in 0 until jsonArrayAll.length()) {
            var jsonObject = jsonArrayAll.getJSONObject(i)
            jsonObjectArray.add(jsonObject)
        }

        var nowtype=0
        var typeArray= arrayOf("全部","製造工程技術","營建技術","資訊與通訊技術","運輸與物流","社會與個人服務","藝術與時尚","青少年組","未來職類")

        var nowJsonObject=jsonObjectArray.sortedBy { it.getString("title") }
        b.gridView2.adapter=SkillsGridViewAdapter(requireContext(),nowJsonObject)
        for (i in 0 until button.count()) {
            button[i].setOnClickListener {
                for (j in 0 until txt.count()) {
                    button[j].setCardBackgroundColor(Color.WHITE)
                    txt[j].setTextColor(Color.BLACK)
                }
                button[i].setCardBackgroundColor(Color.BLACK)
                txt[i].setTextColor(Color.WHITE)
                nowType=i
                nowJsonObject=jsonObjectArray.filter { it.getString("category")==typeArray[nowType] }.sortedBy { it.getString("title") }
                if(nowType==0)nowJsonObject=jsonObjectArray
                b.gridView2.adapter=SkillsGridViewAdapter(requireContext(),nowJsonObject)
            }
        }



        var nowShow=0

        b.dialogNext.setOnClickListener {
            if(nowShow+1<nowJsonObject.count()){
                nowShow++
                b.dialogTitle.text=nowJsonObject[nowShow].getString("title")
                var inputStream=requireContext().assets.open("professions/${nowJsonObject[nowShow].getString("image")}")
                var d=Drawable.createFromStream(inputStream,null)
                b.dialogImage.setImageDrawable(d)
                b.dialogText.text=nowJsonObject[nowShow].getString("description")
                b.dialogScrollView.scrollTo(0,0)
                b.dialogPre.setTextColor(if(nowShow==0)Color.GRAY else requireContext().resources.getColor(R.color.iconColor))
                b.dialogNext.setTextColor(if(nowShow==nowJsonObject.count()-1)Color.GRAY else requireContext().resources.getColor(R.color.iconColor))

            }
        }

        b.dialogPre.setOnClickListener {
            if(nowShow-1>-1){
                nowShow--
                b.dialogTitle.text=nowJsonObject[nowShow].getString("title")
                var inputStream=requireContext().assets.open("professions/${nowJsonObject[nowShow].getString("image")}")
                var d=Drawable.createFromStream(inputStream,null)
                b.dialogImage.setImageDrawable(d)
                b.dialogText.text=nowJsonObject[nowShow].getString("description")
                b.dialogScrollView.scrollTo(0,0)
                b.dialogPre.setTextColor(if(nowShow==0)Color.GRAY else requireContext().resources.getColor(R.color.iconColor))
                b.dialogNext.setTextColor(if(nowShow==nowJsonObject.count()-1)Color.GRAY else requireContext().resources.getColor(R.color.iconColor))

            }
        }

        var animation=ValueAnimator.ofFloat(0.0f,1.0f)
        animation.duration=500
        animation.addUpdateListener {animation
            var value=animation.animatedValue as Float
            b.dialog.scaleX=value
            b.dialog.scaleY=value
            b.dialog.alpha=value
            b.dialogBac.alpha=value
            b.dialog.rotation=360*value
        }

        var animationClose=ValueAnimator.ofFloat(1f,0f)
        animationClose.duration=500
        animationClose.addUpdateListener {animation
            var value=animationClose.animatedValue as Float
            b.dialog.scaleX=value
            b.dialog.scaleY=value
            b.dialog.alpha=value
            b.dialogBac.alpha=value
            b.dialog.rotation=360*value
        }

        animationClose.addListener(object :Animator.AnimatorListener{
            override fun onAnimationStart(animation: Animator) {
//                TODO("Not yet implemented")
            }

            override fun onAnimationEnd(animation: Animator) {
//                TODO("Not yet implemented")
                b.dialogBac.visibility=View.GONE
                b.dialog.visibility=View.GONE
            }

            override fun onAnimationCancel(animation: Animator) {
//                TODO("Not yet implemented")
            }

            override fun onAnimationRepeat(animation: Animator) {
//                TODO("Not yet implemented")
            }

        })

        b.dialogBac.setOnClickListener {
            animationClose.start()
        }

        b.dialogCloseBtn2.setOnClickListener {
            animationClose.start()
        }

        b.dialogCloseBtn1.setOnClickListener {
            animationClose.start()
        }


        b.gridView2.setOnItemClickListener(object : AdapterView.OnItemClickListener{
            override fun onItemClick(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                nowShow=position
                b.dialogBac.visibility=View.VISIBLE
                b.dialog.visibility=View.VISIBLE
                animation.start()
                b.dialogTitle.text=nowJsonObject[position].getString("title")
                var inputStream=requireContext().assets.open("professions/${nowJsonObject[position].getString("image")}")
                var d=Drawable.createFromStream(inputStream,null)
                b.dialogImage.setImageDrawable(d)
                b.dialogText.text=nowJsonObject[position].getString("description")
                b.dialogScrollView.scrollTo(0,0)
                b.dialogPre.setTextColor(if(position==0)Color.GRAY else requireContext().resources.getColor(R.color.iconColor))
                b.dialogNext.setTextColor(if(position==nowJsonObject.count()-1)Color.GRAY else requireContext().resources.getColor(R.color.iconColor))
            }
        })

        return b.root
    }
}