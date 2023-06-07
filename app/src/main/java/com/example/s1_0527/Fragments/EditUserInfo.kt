package com.example.s1_0527.Fragments

import android.os.Bundle
import android.os.Looper
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.s1_0527.R
import com.example.s1_0527.SqlMethod
import com.example.s1_0527.databinding.FragmentEditUserInfoBinding
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import java.io.IOException


class EditUserInfo : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var b=FragmentEditUserInfoBinding.inflate(layoutInflater)
        b.send.setOnClickListener {
            var pwdRegex=Regex("^(?=.*[A-Z])(?=.*[a-z])(?=.*[0-9])(?=.*[^A-Za-z0-9]).{6,}.$")
            var emailRegex=Regex("^[A-Z,a-z,0-9]+@+[A-Z,a-z,0-9]+.+[A-Z,a-z,0-9]{2,4}$")
            if(!pwdRegex.matches(b.pwd.text.toString())){
                b.pwd.error="格式錯誤"
                return@setOnClickListener
            }
            if(!emailRegex.matches(b.email.text.toString()) || b.email.text.toString().contains(" ")){
                b.email.error="格式錯誤"
                return@setOnClickListener
            }
            var jsonObject=JSONObject()
            jsonObject.put("account",SqlMethod.userInfo(requireContext()).getAccount())
            jsonObject.put("email",b.email.text.toString())
            jsonObject.put("name",b.name.text.toString())
            jsonObject.put("password",b.pwd.text.toString())
            var cilent=OkHttpClient.Builder().build()
            var body=jsonObject.toString().toRequestBody("application/json".toMediaType())
            var request=Request.Builder().url("http://10.0.2.2:8485/editUser").post(body).build()
            var call=cilent.newCall(request)
            call.enqueue(object : Callback{
                override fun onFailure(call: Call, e: IOException) {
//                    TODO("Not yet implemented")
                    Log.e("EditError", e.toString(), )
                }

                override fun onResponse(call: Call, response: Response) {
                    Log.e("EditSuccess", response.body!!.string(), )
                    Looper.prepare()
                    Toast.makeText(requireContext(),"Success",Toast.LENGTH_SHORT).show()
                }

            })
        }
        return b.root
    }
}