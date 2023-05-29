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
        var title = arrayListOf<String>()
        var type = arrayListOf<String>()
        var date = arrayListOf<String>()
        var id= arrayListOf<Int>()
        var image= arrayListOf<String>()
        var authorType= arrayListOf<String>()
        var context= arrayListOf<String>()
        var hyperLink= arrayListOf<ArrayList<HashMap<String,String>>>()

        call.enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.e("Failure", e.toString())
            }

            override fun onResponse(call: Call, response: Response) {
                val jsonText = response.body!!.string()
                var jsonArray = JSONArray(jsonText)
                for (i in 0 until jsonArray.length()) {
                    var jsonObject = jsonArray.getJSONObject(i)
                    title.add(jsonObject.getString("title"))
                    type.add(jsonObject.getString("type"))
                    date.add(jsonObject.getString("date"))
                    id.add(jsonObject.getInt("id"))
                    image.add(jsonObject.getString("pics"))
                    authorType.add(jsonObject.getInt("authorType").toString())
                    context.add(jsonObject.getString("content"))
                    hyperLink.add(arrayListOf(hashMapOf("linktext" to jsonObject.getString("linktext"),"url" to jsonObject.getString("url"))))
                }

                activity!!.runOnUiThread {
                    var c=3
                    if(date.count()<3) c=date.count()
                    b.list.adapter = NewsListAdapter(requireContext(),c, date, type, title,id)
                }
            }

        })
        Thread.sleep(1000)
        Log.e("TAG", type.count().toString(), )
        var tisTitle= title
        var tisType= type
        var tisDate= date
        var tisImage= image
        var tisId= id
        var tisAuthorType= authorType
        var tisContext= context
        var tisHyperLink= hyperLink
        for (i in 0 until newsBtn.count()) {
            newsBtn[i].setOnClickListener {
                Log.e("TAG2", type.count().toString(), )
                for (j in 0 until newsBtn.count()) {
                    newsBtn[j].setCardBackgroundColor(Color.WHITE)
                    text[j].setTextColor(Color.BLACK)
                }
                newsBtn[i].setCardBackgroundColor(Color.BLACK)
                text[i].setTextColor(Color.WHITE)
                tisTitle.clear()
                tisType.clear()
                tisDate.clear()
                tisImage.clear()
                tisId.clear()
                tisAuthorType.clear()
                tisContext.clear()
                tisHyperLink.clear()
                for(j in 0 until type.count()){
                    if(type[j]==typeArray[i]){
                        tisTitle.add(title[j])
                        tisType.add(type[j])
                        tisDate.add(date[j])
                        tisId.add(id[j])
                        tisImage.add(image[j])
                        tisAuthorType.add(authorType[j])
                        tisContext.add(context[j])
                        tisHyperLink.add(hyperLink[j])
                    }
                }

                if(i==0){
                    tisTitle= title.clone() as ArrayList<String>
                    tisType=type.clone() as ArrayList<String>
                    tisDate=date.clone() as ArrayList<String>
                    tisId=id.clone() as ArrayList<Int>
                }

                var c=3
                if(tisDate.count()<3) c=tisDate.count()
                b.list.adapter = NewsListAdapter(requireContext(),c, tisDate, tisType, tisTitle,tisId)

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
            fm.replace(R.id.layout,NewsInfoFragment(tisTitle[position],tisType[position],tisDate[position],tisAuthorType[position],tisContext[position],tisImage[position],tisHyperLink[position])).commit()
        } }

        return b.root
    }
}