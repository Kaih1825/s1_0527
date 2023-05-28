package com.example.s1_0527.Fragments

import android.animation.Animator
import android.animation.ValueAnimator
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import androidx.fragment.app.Fragment
import com.example.s1_0527.Adapters.SkillsGridViewAdapter
import com.example.s1_0527.databinding.FragmentSkillInfoBinding
import org.json.JSONArray
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

        for (i in 0 until button.count()) {
            button[i].setOnClickListener {
                for (j in 0 until txt.count()) {
                    button[j].setCardBackgroundColor(Color.WHITE)
                    txt[j].setTextColor(Color.BLACK)
                }
                button[i].setCardBackgroundColor(Color.BLACK)
                txt[i].setTextColor(Color.WHITE)
            }
        }

        var jsonText = requireContext().assets.open("professions.json").bufferedReader().readText()
        var jsonArray = JSONArray(jsonText)
        var category = arrayListOf<String>()
        var title = arrayListOf<String>()
        var image = arrayListOf<String>()
        var description = arrayListOf<String>()
        var allArray = arrayListOf<ArrayList<String>>()
        for (i in 0 until jsonArray.length()) {
            var jsonObject = jsonArray.getJSONObject(i)
            category.add(jsonObject.getString("category"))
            title.add(jsonObject.getString("title"))
            image.add(jsonObject.getString("image"))
            description.add(jsonObject.getString("description"))
        }
        allArray.add(category)
        allArray.add(title)
        allArray.add(image)
        allArray.add(description)

        val sortedIndices = title.indices.sortedBy { title[it] }

        val nt = sortedIndices.map { title[it] }
        val ni = sortedIndices.map { image[it] }
        val nc = sortedIndices.map { category[it] }
        val nd = sortedIndices.map { description[it] }
        b.gridView2.adapter=SkillsGridViewAdapter(requireContext(),ni,nt)

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

        var animationColse=ValueAnimator.ofFloat(1f,0f)
        animationColse.duration=500
        animationColse.addUpdateListener {animation
            var value=animationColse.animatedValue as Float
            b.dialog.scaleX=value
            b.dialog.scaleY=value
            b.dialog.alpha=value
            b.dialogBac.alpha=value
            b.dialog.rotation=360*value
        }

        animationColse.addListener(object :Animator.AnimatorListener{
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
            animationColse.start()
        }


        b.gridView2.setOnItemClickListener(object : AdapterView.OnItemClickListener{
            override fun onItemClick(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                b.dialogBac.visibility=View.VISIBLE
                b.dialog.visibility=View.VISIBLE
                animation.start()

            }
        })

        b.dialog.setOnClickListener{}
        return b.root
    }
}