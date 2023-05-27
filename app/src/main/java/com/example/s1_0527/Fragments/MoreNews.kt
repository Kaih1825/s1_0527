package com.example.s1_0527.Fragments

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.s1_0527.Adapters.NewsListAdapter
import com.example.s1_0527.R
import com.example.s1_0527.databinding.FragmentMoreNewsBinding
import okhttp3.*
import org.json.JSONArray
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

        val celent = OkHttpClient().newBuilder().build()
        var request = Request.Builder().url("http://192.168.50.184/getallnews/").build()
        var call = celent.newCall(request)
        var title = arrayListOf<String>()
        var type = arrayListOf<String>()
        var date = arrayListOf<String>()
        var id= arrayListOf<Int>()

        call.enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.e("TAG", "Failure")
            }

            override fun onResponse(call: Call, response: Response) {
                val jsonText = response.body!!.string()
                var jsonArray = JSONArray(jsonText)
                for (i in 0 until jsonArray.length()) {
                    var jsonObject = jsonArray.getJSONObject(i)
                    title.add(jsonObject.getString("title"))
                    type.add(jsonObject.getString("type"))
                    var tisDate=jsonObject.getString("date")
                    var formate= SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss")
                    var dateFormate= SimpleDateFormat("yyyy-MM-dd")
                    date.add(dateFormate.format(formate.parse(tisDate)))
                    id.add(jsonObject.getInt("id"))
                }

                activity!!.runOnUiThread {
                    b.list.adapter = NewsListAdapter(requireContext(),date.count(), date, type, title,id)
                }
            }
        })
        for (i in 0 until newsBtn.count()) {
            newsBtn[i].setOnClickListener {
                for (j in 0 until newsBtn.count()) {
                    newsBtn[j].setCardBackgroundColor(Color.WHITE)
                    text[j].setTextColor(Color.BLACK)
                }
                newsBtn[i].setCardBackgroundColor(Color.BLACK)
                text[i].setTextColor(Color.WHITE)
                var tisTitle= arrayListOf<String>()
                var tisType= arrayListOf<String>()
                var tisDate= arrayListOf<String>()
                var tisId= arrayListOf<Int>()
                for(j in 0 until title.count()){
                    if(type[j]==typeArray[i]){
                        tisTitle.add(title[j])
                        tisType.add(type[j])
                        tisDate.add(date[j])
                        tisId.add(id[j])
                    }
                }

                if(i==0){
                    tisTitle= title.clone() as ArrayList<String>
                    tisType=type.clone() as ArrayList<String>
                    tisDate=date.clone() as ArrayList<String>
                    tisId=id.clone() as ArrayList<Int>
                }

                requireActivity().runOnUiThread {
                    b.list.adapter = NewsListAdapter(requireContext(),tisDate.count(), tisDate, tisType, tisTitle,tisId)
                }
            }
        }
        var view=layoutInflater.inflate(R.layout.pre_page,b.root,false)
        b.page.addView(view)

        return b.root
    }
}