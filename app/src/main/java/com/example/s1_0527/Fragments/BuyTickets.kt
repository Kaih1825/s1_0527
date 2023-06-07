package com.example.s1_0527.Fragments

import android.content.Context
import android.os.Bundle
import android.os.Parcel
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.s1_0527.R
import com.example.s1_0527.databinding.FragmentBuyTicketsBinding
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.CalendarConstraints.DateValidator
import com.google.android.material.datepicker.CompositeDateValidator
import com.google.android.material.datepicker.DateValidatorPointBackward
import com.google.android.material.datepicker.DateValidatorPointForward
import com.google.android.material.datepicker.MaterialDatePicker
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.util.*


class BuyTickets : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var b = FragmentBuyTicketsBinding.inflate(layoutInflater)
        b.chPlus.setOnClickListener {
            if (b.chiCount.text != "10") b.chiCount.text =
                (b.chiCount.text.toString().toInt() + 1).toString()
            b.all.text = "總共${
                b.chiCount.text.toString().toInt() * 50 + b.adCount.text.toString().toInt() * 100
            }元"
        }
        b.chiMin.setOnClickListener {
            if (b.chiCount.text != "0") b.chiCount.text =
                (b.chiCount.text.toString().toInt() - 1).toString()
            b.all.text = "總共${
                b.chiCount.text.toString().toInt() * 50 + b.adCount.text.toString().toInt() * 100
            }元"
        }

        b.adPlus.setOnClickListener {
            if (b.adCount.text != "10") b.adCount.text =
                (b.adCount.text.toString().toInt() + 1).toString()
            b.all.text = "總共${
                b.chiCount.text.toString().toInt() * 50 + b.adCount.text.toString().toInt() * 100
            }元"
        }
        b.adMin.setOnClickListener {
            if (b.adCount.text != "0") b.adCount.text =
                (b.adCount.text.toString().toInt() - 1).toString()
            b.all.text = "總共${
                b.chiCount.text.toString().toInt() * 50 + b.adCount.text.toString().toInt() * 100
            }元"
        }
        val startDateString = "2023/07/13"
        val endDateString = "2023/07/18"
        val dateFormat = SimpleDateFormat("yyyy/MM/dd", Locale.getDefault())
        val startDate = dateFormat.parse(startDateString)
        val endDate = dateFormat.parse(endDateString)
        val dateValidator = object : DateValidator {
            override fun isValid(date: Long): Boolean {
                val selectedDate = Date(date)
                return selectedDate in startDate..endDate
            }

            override fun describeContents(): Int {
                return 0
            }

            override fun writeToParcel(dest: Parcel, flags: Int) {
//                TODO("Not yet implemented")
            }
        }

        var datePicker =
            MaterialDatePicker.Builder.datePicker()
                .setCalendarConstraints(CalendarConstraints.Builder().setValidator(dateValidator).build())
                .build()

        b.date.setOnClickListener {
            datePicker.show(requireFragmentManager(), "")
        }
        datePicker.addOnPositiveButtonClickListener { calender->
            b.date.text=dateFormat.format(calender)
        }
        b.buy.setOnClickListener {
            if(b.chiCount.text.toString().toInt()+b.adCount.text.toString().toInt()>10){
                b.buy.text="總張數不能超過10張"
            }else if(b.date.text=="請選擇日期"){
                b.buy.text="日期未選擇"
            }else{
                var sp=requireContext().getSharedPreferences("user",Context.MODE_PRIVATE)
                if(sp.getBoolean("login",false)){
                    var fm=requireFragmentManager().beginTransaction()
                    fm.addToBackStack(fm.javaClass.name)
                    fm.replace(R.id.layout,CheckTickets(b.date.text.toString(),b.adCount.text.toString(),b.chiCount.text.toString(),b.all.text.toString())).commit()
                }else{
                    var fm=requireFragmentManager().beginTransaction()
                    fm.addToBackStack(fm.javaClass.name)
                    fm.replace(R.id.layout,Login()).commit()
                }
            }
        }
        return b.root
    }
}