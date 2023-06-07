package com.example.s1_0527.Fragments

import android.content.Context
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
import com.example.s1_0527.databinding.FragmentRegBinding
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import java.io.IOException

class RegFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var b=FragmentRegBinding.inflate(layoutInflater)
        b.reg.setOnClickListener {
            var pwdRegex=Regex("^(?=.*[A-Z])(?=.*[a-z])(?=.*[0-9])(?=.*[^A-Za-z0-9]).{6,}.$")
            var emailRegex=Regex("^[A-Z,a-z,0-9]+@+[A-Z,a-z,0-9]+.+[A-Z,a-z,0-9]{2,4}$")
            var error=false
            if(!pwdRegex.matches(b.pwd.text.toString())){
                b.pwd.error="格式錯誤"
                error=true
            }
            if(!emailRegex.matches(b.email.text.toString()) || b.email.text.toString().contains(" ")){
                b.email.error="格式錯誤"
                error=true
            }
            if(!error){
                var client = OkHttpClient.Builder()
                var jsonObject = JSONObject()
                jsonObject.put("name", b.name.text.toString())
                jsonObject.put("account", b.account.text.toString())
                jsonObject.put("password", b.pwd.text.toString())
                jsonObject.put("email", b.email.text.toString())
                jsonObject.put(
                    "userType",
                    b.userType.selectedItemPosition
                )
                var request = Request.Builder().url("http://10.0.2.2:8485/register")
                    .post(jsonObject.toString().toRequestBody("application/json; charset=utf-8".toMediaType()))
                    .build()
                var call = client.build().newCall(request)
                call.enqueue(object : Callback {
                    override fun onFailure(call: Call, e: IOException) {

                    }

                    override fun onResponse(call: Call, response: Response) {
                        var reslut = response.body!!.string()
                        if (reslut != "ok") {
                            Looper.prepare()
                            Toast.makeText(
                                requireContext(),
                                "註冊失敗，請檢查帳號是否已被使用過",
                                Toast.LENGTH_SHORT
                            ).show()
                        } else {
                            activity!!.runOnUiThread{
                                b.email.requestFocus()
                                b.email.clearFocus()
                                SqlMethod.userInfo(requireContext()).update(b.account.text.toString(),b.name.text.toString(),b.pwd.text.toString(),b.email.text.toString(),b.userType.selectedItemPosition)
                            }
                            Log.e("Result", reslut, )
                            Looper.prepare()
                            Toast.makeText(requireContext(), "註冊成功", Toast.LENGTH_SHORT).show()
                            var sp=requireContext().getSharedPreferences("user", Context.MODE_PRIVATE).edit()
                            sp.putBoolean("login",true)
                            sp.commit()
                            requireFragmentManager().popBackStack()
                            requireFragmentManager().popBackStack()
                        }
                    }

                })
            }
        }
        return b.root
    }
}