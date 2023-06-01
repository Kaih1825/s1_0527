package com.example.s1_0527.Fragments

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager2.widget.ViewPager2
import com.example.s1_0527.Adapters.HomeViewPagerAdapter
import com.example.s1_0527.Adapters.NewsListAdapter
import com.example.s1_0527.R
import com.example.s1_0527.databinding.FragmentHomeBinding
import kotlinx.coroutines.delay
import okhttp3.*
import org.json.JSONArray
import org.json.JSONObject
import java.io.IOException
import java.text.SimpleDateFormat
import javax.security.auth.login.LoginException

class HomeFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var b = FragmentHomeBinding.inflate(layoutInflater)
        b.viewPager.adapter = HomeViewPagerAdapter(requireContext())
        b.viewPager.offscreenPageLimit = 3
        b.viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                var viewPagerI = arrayOf(b.viewPagerI1, b.viewPagerI2)
                for (i in 0 until viewPagerI.count()) {
                    viewPagerI[i].setCardBackgroundColor(Color.WHITE)
                }
                viewPagerI[position].setCardBackgroundColor(resources.getColor(R.color.deepBlue))
            }
        })
        var newsBtn =
            arrayOf(b.newsBtn1, b.newsBtn2, b.newsBtn3, b.newsBtn4, b.newsBtn5, b.newsBtn6)
        var text = arrayOf(b.text1, b.text2, b.text3, b.text4, b.text5, b.text6)
        var typeArray= arrayOf("全部","全國賽","身障賽","國際賽","展能節","達人盃")

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
                    b.list.adapter=NewsListAdapter(requireContext(),jsonObjectArray,3)
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

                b.list.adapter = NewsListAdapter(requireContext(), tisJsonObjectArray, 3)
            }
        }

        b.moreNews.setOnClickListener {
            var fm=requireFragmentManager().beginTransaction()
            fm.addToBackStack(fm.javaClass.name)
            fm.replace(R.id.layout,MoreNews()).commit()
        }

        b.list.setOnItemClickListener { parent, view, position, id -> run{
            var fm=requireFragmentManager().beginTransaction()
            fm.addToBackStack(fm.javaClass.name)
            fm.replace(R.id.layout,NewsInfoFragment(tisJsonObjectArray[position])).commit()
        } }

        return b.root
    }
}