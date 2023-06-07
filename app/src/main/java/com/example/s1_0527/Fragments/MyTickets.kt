package com.example.s1_0527.Fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.s1_0527.Adapters.TicketsListAdapter
import com.example.s1_0527.R
import com.example.s1_0527.SqlMethod
import com.example.s1_0527.databinding.FragmentMyTicketsBinding

class MyTickets : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var b=FragmentMyTicketsBinding.inflate(layoutInflater)
        b.list.adapter=TicketsListAdapter(requireContext(),SqlMethod.tickets(requireContext()).getAll())
        return b.root
    }
}