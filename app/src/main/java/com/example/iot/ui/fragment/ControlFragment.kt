package com.example.iot.ui.fragment

import com.example.iot.R
import com.example.iot.databinding.FragmentControlBinding
import com.example.iot.ui.base.BaseFragment
import com.example.iot.ui.fragment.dialog.LoadingDialog

class ControlFragment : BaseFragment<FragmentControlBinding>(R.layout.fragment_control) {

    private val loadingDialog = LoadingDialog()

    override fun getViewBinding(): FragmentControlBinding {
        return FragmentControlBinding.inflate(layoutInflater)
    }

    override fun initView() {
        binding.btnFan.setOnClickListener {
            loadingDialog.show(childFragmentManager , "")
        }
        binding.btnLight.setOnClickListener {
            loadingDialog.show(childFragmentManager , "")
        }
        binding.btnAC.setOnClickListener {
            loadingDialog.show(childFragmentManager , "")
        }
    }

    override fun observeViewModel() {
    }

}