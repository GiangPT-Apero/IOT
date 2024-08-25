package com.example.iot.ui.fragment

import com.example.iot.R
import com.example.iot.databinding.FragmentUserBinding
import com.example.iot.ui.base.BaseFragment

class UserFragment : BaseFragment<FragmentUserBinding>(R.layout.fragment_user) {

    override fun getViewBinding(): FragmentUserBinding {
        return FragmentUserBinding.inflate(layoutInflater)
    }

    override fun initView() {
    }

    override fun observeViewModel() {
    }

}