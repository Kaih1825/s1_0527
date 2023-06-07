package com.example.s1_0527.Fragments

import android.graphics.Color
import android.os.Bundle
import android.os.Parcel
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.s1_0527.R
import com.example.s1_0527.SqlMethod
import com.example.s1_0527.databinding.FragmentNewNewsBinding
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.CalendarConstraints.DateValidator
import com.google.android.material.datepicker.MaterialDatePicker
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import java.io.IOException
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.util.*


class NewNews : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var b=FragmentNewNewsBinding.inflate(layoutInflater)
        var dateValidator=object : DateValidator{
            override fun describeContents(): Int {
                return 0
            }

            override fun writeToParcel(dest: Parcel, flags: Int) {

            }

            override fun isValid(date: Long): Boolean {
                return Date(date) <= Calendar.getInstance().time
            }

        }

        var datePicker=MaterialDatePicker.Builder.datePicker().setCalendarConstraints(CalendarConstraints.Builder().setValidator(dateValidator).build()).build()
        b.date.setOnClickListener {
            datePicker.show(requireFragmentManager(),"")
        }

        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        datePicker.addOnPositiveButtonClickListener { calender->
            b.date.text=dateFormat.format(calender)
        }
        b.send.setOnClickListener {
            if(b.date.text=="請選擇日期"){
                b.date.setTextColor(Color.RED)
                return@setOnClickListener
            }
            var jsonObject = JSONObject()
            jsonObject.put("title", b.title.text.toString())
            jsonObject.put("type", b.type.text.toString())
            jsonObject.put("content", b.context.text.toString())
            jsonObject.put("linktext", b.linkText.text.toString())
            jsonObject.put("url", b.link.text.toString())
            jsonObject.put("pics", b.pic.text.toString())
            jsonObject.put("authorType", SqlMethod.userInfo(requireContext()).getType()+1)
            jsonObject.put("date", b.date.text)
            var client = OkHttpClient.Builder().build()
            var body = jsonObject.toString().toRequestBody("application/json".toMediaType())
            var request = Request.Builder().url("http://10.0.2.2:8485/news").post(body).build()
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
                        SqlMethod.news(requireContext()).insertNoId(
                            b.context.text.toString(),
                            b.date.text.toString(),
                            b.linkText.text.toString(),
                            b.pic.text.toString(),
                            b.title.text.toString(),
                            b.type.text.toString(),
                            b.link.text.toString(),
                            SqlMethod.userInfo(requireContext()).getType()+1
                        )
                        requireFragmentManager().popBackStack()
                    }
                }
            })
        }
        return b.root
    }
}