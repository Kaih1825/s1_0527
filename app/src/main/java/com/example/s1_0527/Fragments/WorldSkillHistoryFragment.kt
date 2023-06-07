package com.example.s1_0527.Fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.s1_0527.R
import com.example.s1_0527.databinding.FragmentWorldSkillHistoryBinding

class WorldSkillHistoryFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var b=FragmentWorldSkillHistoryBinding.inflate(layoutInflater)
        b.webView.loadUrl("file:///android_asset/技能競賽主題網站.html")
        b.webView.settings.allowFileAccess=true
        return b.root
    }
}