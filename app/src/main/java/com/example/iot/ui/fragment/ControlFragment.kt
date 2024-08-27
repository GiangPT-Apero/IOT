package com.example.iot.ui.fragment

import android.graphics.Color
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.work.Constraints
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
        binding.btnLight.setOnClickListener {
            loadingDialog.show(childFragmentManager , "")
            updateUi(binding.txtStateLight, binding.txtLight, binding.loLight, binding.imgLight, binding.btnLight.isChecked)
        }
        binding.btnFan.setOnClickListener {
            loadingDialog.show(childFragmentManager , "")
            updateUi(binding.txtStateFan, binding.txtFan, binding.loFan, binding.imgFan, binding.btnFan.isChecked)
        }
        binding.btnAC.setOnClickListener {
            loadingDialog.show(childFragmentManager , "")
            updateUi(binding.txtStateAC, binding.txtAC, binding.loAC, binding.imgAC, binding.btnAC.isChecked)
        }
    }

    override fun observeViewModel() {
    }


    private fun updateUi(txtState: TextView, text: TextView, background: ConstraintLayout, icon: ImageView, state: Boolean) {
        if (state) {
            txtState.text = "ON"
            txtState.setTextColor(ContextCompat.getColorStateList(requireContext(), R.color.white))
            text.setTextColor(ContextCompat.getColorStateList(requireContext(), R.color.white))
            background.backgroundTintList = ContextCompat.getColorStateList(requireContext(), R.color.dark_464646)
            icon.imageTintList = ContextCompat.getColorStateList(requireContext(), R.color.white)
        } else {
            txtState.text = "OFF"
            txtState.setTextColor(ContextCompat.getColorStateList(requireContext(), R.color.dark_464646))
            text.setTextColor(ContextCompat.getColorStateList(requireContext(), R.color.dark_464646))
            background.backgroundTintList = ContextCompat.getColorStateList(requireContext(), R.color.white)
            icon.imageTintList = ContextCompat.getColorStateList(requireContext(), R.color.gray_BDBDBD)
        }
    }
}