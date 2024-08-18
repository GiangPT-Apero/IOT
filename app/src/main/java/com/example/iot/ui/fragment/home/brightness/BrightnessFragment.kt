package com.example.iot.ui.fragment.home

import com.example.iot.R
import com.example.iot.databinding.FragmentBrightnessBinding
import com.example.iot.ui.base.BaseFragment

class BrightnessFragment : BaseFragment<FragmentBrightnessBinding>(R.layout.fragment_brightness) {

    override fun getViewBinding(): FragmentBrightnessBinding {
        return FragmentBrightnessBinding.inflate(layoutInflater)
    }

    override fun initView() {
        TODO("Not yet implemented")
    }

    override fun observeViewModel() {
        TODO("Not yet implemented")
    }

}