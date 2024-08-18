package com.example.iot.ui.fragment

import com.example.iot.R
import com.example.iot.databinding.FragmentControlBinding
import com.example.iot.ui.base.BaseFragment

class ControlFragment : BaseFragment<FragmentControlBinding>(R.layout.fragment_control) {

    override fun getViewBinding(): FragmentControlBinding {
        return FragmentControlBinding.inflate(layoutInflater)
    }

    override fun initView() {
        TODO("Not yet implemented")
    }

    override fun observeViewModel() {
        TODO("Not yet implemented")
    }

}