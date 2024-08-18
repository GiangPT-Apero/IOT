package com.example.iot.ui.fragment.home

import com.example.iot.R
import com.example.iot.databinding.FragmentHomeBinding
import com.example.iot.ui.base.BaseFragment
import com.google.android.material.tabs.TabLayoutMediator

class HomeFragment : BaseFragment<FragmentHomeBinding>(R.layout.fragment_home) {

    override fun getViewBinding(): FragmentHomeBinding {
        return FragmentHomeBinding.inflate(layoutInflater)
    }

    override fun initView() {
        binding.viewPager.adapter = HomeAdapter(requireActivity())
        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            when (position) {
                0 -> tab.text = "Temperature"
                1 -> tab.text = "Humidity"
                2 -> tab.text = "Brightness"
            }
        }.attach()
    }

    override fun observeViewModel() {
        TODO("Not yet implemented")
    }

}