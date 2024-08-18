package com.example.iot.ui.fragment.home

import com.example.iot.R
import com.example.iot.databinding.FragmentHumidityBinding
import com.example.iot.ui.base.BaseFragment

class HumidityFragment : BaseFragment<FragmentHumidityBinding>(R.layout.fragment_humidity) {

    override fun getViewBinding(): FragmentHumidityBinding {
        return FragmentHumidityBinding.inflate(layoutInflater)
    }

    override fun initView() {
        TODO("Not yet implemented")
    }

    override fun observeViewModel() {
        TODO("Not yet implemented")
    }

}