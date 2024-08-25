package com.example.iot.ui.fragment.home

import com.example.iot.R
import com.example.iot.databinding.FragmentTemperatureBinding
import com.example.iot.ui.base.BaseFragment

class TemperatureFragment : BaseFragment<FragmentTemperatureBinding>(R.layout.fragment_temperature) {

    override fun getViewBinding(): FragmentTemperatureBinding {
        return FragmentTemperatureBinding.inflate(layoutInflater)
    }

    override fun initView() {
    }

    override fun observeViewModel() {
    }

}