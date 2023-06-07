package com.example.s1_0527.Fragments

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.os.Looper
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.example.s1_0527.R
import com.example.s1_0527.SqlMethod
import com.example.s1_0527.databinding.FragmentLoginBinding
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import org.json.JSONStringer
import java.io.IOException
import java.time.Duration
import java.time.LocalDateTime
import javax.security.auth.login.LoginException


class Login : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var b=FragmentLoginBinding.inflate(layoutInflater)
        var pwdTryCount=-2
        var lastFailureTime=LocalDateTime.now()
        b.login.setOnClickListener {
            var jsonObject=JSONObject()
            jsonObject.put("account",b.name.text.toString())
            jsonObject.put("password",b.pwd.text.toString())
            var infoBody=jsonObject.toString().toRequestBody("application/json; charset=utf-8".toMediaType())
            var request=Request.Builder().url("http://10.0.2.2:8485/login").post(infoBody).build()
            var client=OkHttpClient.Builder().build()
            var call=client.newCall(request)
            call.enqueue(object : Callback{
                override fun onFailure(call: Call, e: IOException) {
                    Log.e("Failure", e.toString(), )
                }

                override fun onResponse(call: Call, response: Response) {
                    var resString=response.body!!.string()
                    if(pwdTryCount==0 && Duration.between(lastFailureTime,LocalDateTime.now()).toMinutes()<5){
                        Looper.prepare()
                        Toast.makeText(requireContext(),"請等待${5-Duration.between(lastFailureTime,LocalDateTime.now()).toMinutes()}分鐘再嘗試",Toast.LENGTH_SHORT).show()
                    }
                    else if(resString.indexOf("403")>=0){
                        pwdTryCount++
                        if(pwdTryCount==3){
                            pwdTryCount=0
                            Looper.prepare()
                            Toast.makeText(requireContext(),"密碼錯誤，請等待5分鐘再嘗試",Toast.LENGTH_SHORT).show()
                            lastFailureTime=LocalDateTime.now()
                        }else{
                            pwdTryCount=if(pwdTryCount==-1) 1 else pwdTryCount
                            Looper.prepare()
                            Toast.makeText(requireContext(),"密碼錯誤，剩下${3-pwdTryCount}次機會",Toast.LENGTH_SHORT).show()
                        }
                    }
                    else if(resString=="user does not exist"){
                        Looper.prepare()
                        Toast.makeText(requireContext(),"找不到帳號，請重新輸入",Toast.LENGTH_SHORT).show()
                    }
                    else{
                        Log.e("Success", resString, )
                        activity!!.runOnUiThread{
                            addToSql(resString)
                            b.pwd.requestFocus()
                            b.pwd.clearFocus()
                        }
                        Looper.prepare()
                        Toast.makeText(requireContext(),"登入成功",Toast.LENGTH_SHORT).show()
                        var sp=requireContext().getSharedPreferences("user",Context.MODE_PRIVATE).edit()
                        sp.putBoolean("login",true)
                        sp.commit()
                        requireFragmentManager().popBackStack()
                    }
                }

            })
        }

        b.forget.setOnClickListener {
            var client=OkHttpClient.Builder()
            var jsonObject=JSONObject()
            jsonObject.put("account",b.name.text.toString())
            var body=jsonObject.toString().toRequestBody("application/json; charset=utf-8".toMediaType())
            var request=Request.Builder().url("http://10.0.2.2:8485/changePassword").post(body).build()
            var call=client.build().newCall(request)
            call.enqueue(object : Callback{
                override fun onFailure(call: Call, e: IOException) {
                    Looper.prepare()
                    Toast.makeText(requireContext(),e.toString(),Toast.LENGTH_SHORT).show()
                }

                override fun onResponse(call: Call, response: Response) {
                    Looper.prepare()
                    Toast.makeText(requireContext(),"更換完成",Toast.LENGTH_SHORT).show()
                }

            })
        }

        b.reg.setOnClickListener {
            var fm=requireFragmentManager().beginTransaction()
            fm.addToBackStack(fm.javaClass.name)
            fm.replace(R.id.layout,RegFragment()).commit()
        }
        return b.root
    }

    fun addToSql(jsonString:String){
        var jsonObject=JSONObject(jsonString)
        SqlMethod.userInfo(requireContext()).update(jsonObject.getString("account"),jsonObject.getString("name"),jsonObject.getString("password"),jsonObject.getString("email"),jsonObject.getString("userType").toInt())
    }
}