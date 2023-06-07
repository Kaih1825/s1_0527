package com.example.s1_0527

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.CompoundButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import com.example.s1_0527.Fragments.*
import com.example.s1_0527.databinding.ActivityHomeContainerBinding

class HomeContainer : AppCompatActivity() {
    lateinit var b:ActivityHomeContainerBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        b = ActivityHomeContainerBinding.inflate(layoutInflater)
        setContentView(b.root)

        var action=intent.getStringExtra("action")
        if(action=="OpenMyTicket"){
            supportFragmentManager.beginTransaction().replace(R.id.layout,MyTickets()).commit()
        }else if(action=="ToAllNews"){
            supportFragmentManager.beginTransaction().replace(R.id.layout,NewsInfoFragment(null,intent.getIntExtra("allId",-1))).commit()
        }
        else if(action=="BuyTickets"){
            supportFragmentManager.beginTransaction().replace(R.id.layout,BuyTickets()).commit()
        }
        else{
            supportFragmentManager.beginTransaction().replace(R.id.layout,HomeFragment()).commit()
        }
        b.drawerIcon.setOnClickListener {
            b.drawer.open()
            var sp=getSharedPreferences("user", MODE_PRIVATE)
            if(sp.getBoolean("login",false)){
                b.login.visibility= View.GONE
                b.logout.visibility=View.VISIBLE
                b.myTickets.visibility=View.VISIBLE
                b.editInfo.visibility=View.VISIBLE
            }else{
                b.logout.visibility=View.GONE
                b.login.visibility=View.VISIBLE
                b.myTickets.visibility=View.GONE
                b.editInfo.visibility=View.GONE
            }
        }

        b.darkModeSwitch.setOnCheckedChangeListener(object : CompoundButton.OnCheckedChangeListener{
            override fun onCheckedChanged(buttonView: CompoundButton?, isChecked: Boolean) {
                if(isChecked){
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                }else{
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                }
                var sp=getSharedPreferences("user", MODE_PRIVATE)
                if(sp.getBoolean("login",false)){
                    b.login.visibility= View.GONE
                    b.logout.visibility=View.VISIBLE
                    b.myTickets.visibility=View.VISIBLE
                    b.editInfo.visibility=View.VISIBLE
                }else{
                    b.logout.visibility=View.GONE
                    b.login.visibility=View.VISIBLE
                    b.myTickets.visibility=View.GONE
                    b.editInfo.visibility=View.GONE
                }
            }

        })


        b.myTickets.setOnClickListener {
            var fm=supportFragmentManager.beginTransaction()
            fm.addToBackStack(fm.javaClass.name)
            fm.replace(R.id.layout,MyTickets()).commit()
            b.drawer.close()
        }

        b.editInfo.setOnClickListener {
            var fm=supportFragmentManager.beginTransaction()
            fm.addToBackStack(fm.javaClass.name)
            fm.replace(R.id.layout,EditUserInfo()).commit()
            b.drawer.close()
        }

        b.logout.setOnClickListener {
            var sp=getSharedPreferences("user", MODE_PRIVATE).edit()
            sp.putBoolean("login",false)
            sp.commit()
            b.drawer.close()
            var fm=supportFragmentManager
            fm.beginTransaction().replace(R.id.layout,HomeFragment()).commit()
            SqlMethod.userInfo(this).logout()
        }

        b.drawerNews.setOnClickListener {
            var fm=supportFragmentManager.beginTransaction()
            fm.addToBackStack(fm.javaClass.name)
            fm.replace(R.id.layout,MoreNews()).commit()
            b.drawer.close()
        }

        b.history.setOnClickListener {
            var fm=supportFragmentManager.beginTransaction()
            fm.addToBackStack(fm.javaClass.name)
            fm.replace(R.id.layout,WorldSkillHistoryFragment()).commit()
            b.drawer.close()
        }

        b.buyTickets.setOnClickListener {
            var fm=supportFragmentManager.beginTransaction()
            fm.addToBackStack(fm.javaClass.name)
            fm.replace(R.id.layout,BuyTickets()).commit()
            b.drawer.close()
        }



        b.skillInfo.setOnClickListener {
            var fm=supportFragmentManager.beginTransaction()
            fm.addToBackStack(fm.javaClass.name)
            fm.replace(R.id.layout,SkillInfoFragment()).commit()
            b.drawer.close()
        }

        b.login.setOnClickListener {
            var fm=supportFragmentManager.beginTransaction()
            fm.addToBackStack(fm.javaClass.name)
            fm.replace(R.id.layout,Login()).commit()
            b.drawer.close()
        }
    }
}