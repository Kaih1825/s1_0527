package com.example.s1_0527.Fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.s1_0527.Adapters.NewsListAdapter
import com.example.s1_0527.R
import com.example.s1_0527.SqlMethod
import com.example.s1_0527.databinding.FragmentAllStarBinding

class AllStar : Fragment() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var b=FragmentAllStarBinding.inflate(layoutInflater)
        b.list.adapter=NewsListAdapter(requireContext(), arrayListOf(),-2)

        b.list.setOnItemClickListener { _, _, position, _ ->  run{
            var cursor= SqlMethod.news(requireContext()).getStarID()
            var id=0
            cursor.moveToFirst()
            for (i in 0 until cursor.count){
                if(i==position){
                    id=cursor.getInt(0)
                    break
                }
                cursor.moveToNext()
            }
            var fm=requireFragmentManager().beginTransaction()
            fm.addToBackStack(fm.javaClass.name)
            fm.replace(R.id.layout,NewsInfoFragment(null,id)).commit()
        }}
        return b.root
    }
}