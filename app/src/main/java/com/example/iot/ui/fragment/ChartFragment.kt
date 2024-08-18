package com.example.iot.ui.fragment

import com.example.iot.R
import com.example.iot.databinding.FragmentChartBinding
import com.example.iot.ui.base.BaseFragment

class ChartFragment : BaseFragment<FragmentChartBinding>(R.layout.fragment_chart) {

    override fun getViewBinding(): FragmentChartBinding {
        return FragmentChartBinding.inflate(layoutInflater)
    }

    override fun initView() {
        TODO("Not yet implemented")
    }

    override fun observeViewModel() {
        TODO("Not yet implemented")
    }

}