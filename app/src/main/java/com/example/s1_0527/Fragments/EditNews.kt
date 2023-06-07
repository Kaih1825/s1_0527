package com.example.s1_0527.Fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.s1_0527.R
import com.example.s1_0527.SqlMethod
import com.example.s1_0527.databinding.FragmentEditNewsBinding
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import java.io.IOException

class EditNews(var mid: Int) : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var b = FragmentEditNewsBinding.inflate(layoutInflater)
        var cursor = SqlMethod.news(requireContext()).getNewsFromId(mid)
        cursor.moveToFirst()
        b.title.setText(cursor.getString(5))
        b.type.setText(cursor.getString(6))
        b.link.setText(cursor.getString(7))
        b.pic.setText(cursor.getString(4))
        b.context.setText(cursor.getString(1))
        b.linkText.setText(cursor.getString(3))
        b.send.setOnClickListener {
            var jsonObject = JSONObject()
            jsonObject.put("title", b.title.text.toString())
            jsonObject.put("type", b.type.text.toString())
            jsonObject.put("content", b.context.text.toString())
            jsonObject.put("linktext", b.linkText.text.toString())
            jsonObject.put("url", b.link.text.toString())
            jsonObject.put("pics", b.pic.text.toString())
            var client = OkHttpClient.Builder().build()
            var body = jsonObject.toString().toRequestBody("application/json".toMediaType())
            var request = Request.Builder().url("http://10.0.2.2:8485/news/$mid").put(body).build()
            var call = client.newCall(request)

            call.enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    Log.e("Failure_PutNews", e.toString())
                }

                override fun onResponse(call: Call, response: Response) {
                    var re = response.body!!.string()
                    if (re != "ok") {
                        Log.e("Failure_PutNews", re)
                    } else {
                        Log.e("Success_PutNews", re)
                        SqlMethod.news(requireContext()).updateNews(
                            mid,
                            b.context.text.toString(),
                            b.linkText.text.toString(),
                            b.pic.text.toString(),
                            b.title.text.toString(),
                            b.type.text.toString(),
                            b.link.text.toString()
                        )
                        requireFragmentManager().popBackStack()
                    }
                }

            })
        }
        return b.root
    }
}