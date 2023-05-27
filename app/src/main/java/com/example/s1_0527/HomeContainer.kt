package com.example.s1_0527

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.s1_0527.Fragments.HomeFragment
import com.example.s1_0527.Fragments.SkillInfoFragment
import com.example.s1_0527.databinding.ActivityHomeContainerBinding

class HomeContainer : AppCompatActivity() {
    lateinit var b:ActivityHomeContainerBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        b = ActivityHomeContainerBinding.inflate(layoutInflater)
        setContentView(b.root)

        b.drawerIcon.setOnClickListener {
            b.drawer.open()
        }

        supportFragmentManager.beginTransaction().replace(R.id.layout,HomeFragment()).commit()

        b.skillInfo.setOnClickListener {
            var fm=supportFragmentManager.beginTransaction()
            fm.addToBackStack(fm.javaClass.name)
            fm.replace(R.id.layout,SkillInfoFragment()).commit()
            b.drawer.close()
        }
    }
}