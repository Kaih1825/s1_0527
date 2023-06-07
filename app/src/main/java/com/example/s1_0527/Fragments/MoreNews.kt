package com.example.s1_0527.Fragments

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.ViewUtils
import com.example.s1_0527.Adapters.NewsListAdapter
import com.example.s1_0527.R
import com.example.s1_0527.SqlMethod
import com.example.s1_0527.databinding.FragmentMoreNewsBinding
import okhttp3.*
import org.json.JSONArray
import org.json.JSONObject
import java.io.IOException
import java.text.SimpleDateFormat

class MoreNews : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var b=FragmentMoreNewsBinding.inflate(layoutInflater)
        var newsBtn =
            arrayOf(b.newsBtn1, b.newsBtn2, b.newsBtn3, b.newsBtn4, b.newsBtn5, b.newsBtn6)
        var text = arrayOf(b.text1, b.text2, b.text3, b.text4, b.text5, b.text6)
        var typeArray= arrayOf("全部","全國賽","身障賽","國際賽","展能節","達人盃")

        var sp=requireContext().getSharedPreferences("user",Context.MODE_PRIVATE)
        if(sp.getBoolean("login",false) && SqlMethod.userInfo(requireContext()).getType()!=2){
            b.newNews.visibility=View.VISIBLE
            b.star.visibility=View.INVISIBLE
        }
        else if(sp.getBoolean("login",false)){
            b.newNews.visibility=View.INVISIBLE
            b.star.visibility=View.VISIBLE
        }
        else{
            b.newNews.visibility=View.INVISIBLE
            b.star.visibility=View.INVISIBLE
        }

        b.star.setOnClickListener {
            var fm=requireFragmentManager().beginTransaction()
            fm.addToBackStack(fm.javaClass.name)
            fm.replace(R.id.layout,AllStar()).commit()
        }

        b.newNews.setOnClickListener {
            var fm=requireFragmentManager().beginTransaction()
            fm.addToBackStack(fm.javaClass.name)
            fm.replace(R.id.layout,NewNews()).commit()
        }

        val celent = OkHttpClient().newBuilder().build()
        var request = Request.Builder().url("http://10.0.2.2:8485/news").build()
        var call = celent.newCall(request)

        var jsonObjectArray= arrayListOf<JSONObject>()
        var tisJsonObjectArray= arrayListOf<JSONObject>()
        call.enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.e("Failure", e.toString())
            }

            override fun onResponse(call: Call, response: Response) {
                val jsonText = response.body!!.string()
                var tisJsonArray = JSONArray(jsonText)
                for(i in 0 until  tisJsonArray.length()){
                    jsonObjectArray.add(tisJsonArray.getJSONObject(i))
                }
                activity!!.runOnUiThread {
                    b.list.adapter=NewsListAdapter(requireContext(),jsonObjectArray,-1)
                    tisJsonObjectArray= jsonObjectArray.clone() as ArrayList<JSONObject>
                }
            }

        })
        for (i in 0 until newsBtn.count()) {
            newsBtn[i].setOnClickListener {
                tisJsonObjectArray.clear()
                for (j in 0 until newsBtn.count()) {
                    newsBtn[j].setCardBackgroundColor(Color.WHITE)
                    text[j].setTextColor(Color.BLACK)
                }
                newsBtn[i].setCardBackgroundColor(Color.BLACK)
                text[i].setTextColor(Color.WHITE)
                tisJsonObjectArray = if(i==0) {
                    jsonObjectArray.clone() as ArrayList<JSONObject>
                } else {
                    jsonObjectArray.filter { it.getString("type") == typeArray[i] } as ArrayList<JSONObject>
                }

                b.list.adapter = NewsListAdapter(requireContext(), tisJsonObjectArray, -1)
            }
        }

        b.list.setOnItemClickListener { parent, view, position, id -> run{
            var fm=requireFragmentManager().beginTransaction()
            fm.addToBackStack(fm.javaClass.name)
            fm.replace(R.id.layout,NewsInfoFragment(tisJsonObjectArray[position],null)).commit()
        } }
        var view=layoutInflater.inflate(R.layout.pre_page,b.root,false)
        b.page.addView(view)

        return b.root
    }
}