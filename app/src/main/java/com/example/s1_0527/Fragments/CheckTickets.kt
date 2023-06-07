package com.example.s1_0527.Fragments

import android.os.Bundle
import android.os.Handler
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.s1_0527.R
import com.example.s1_0527.SqlMethod
import com.example.s1_0527.databinding.FragmentCheckTicketsBinding

class CheckTickets(var date:String,var adCount:String,var chiCount:String,var all:String) : Fragment() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var b=FragmentCheckTicketsBinding.inflate(layoutInflater)
        b.all.text=all
        b.date.text=date
        b.adCount.text=adCount
        b.chiCount.text=chiCount
        b.buy.setOnClickListener {
            SqlMethod.tickets(requireContext()).insert(b.date.text.toString(),b.adCount.text.toString().toInt(),b.chiCount.text.toString().toInt())
            b.success.visibility=View.VISIBLE
            Handler().postDelayed({
                var fm=requireFragmentManager().beginTransaction()
                fm.replace(R.id.layout,MyTickets()).commit()
            },2000)
        }
        return b.root
    }
}