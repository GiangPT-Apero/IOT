package com.example.iot.ui.fragment

import android.graphics.Color
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.widget.SwitchCompat
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.work.Constraints
import com.example.iot.R
import com.example.iot.databinding.FragmentControlBinding
import com.example.iot.model.LedState
import com.example.iot.ui.base.BaseFragment
import com.example.iot.ui.fragment.dialog.LoadingDialog
import com.example.iot.viewmodel.ControlViewModel
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.launch

class ControlFragment : BaseFragment<FragmentControlBinding>(R.layout.fragment_control) {

    private val controlViewModel: ControlViewModel by viewModels()

    private val loadingDialog = LoadingDialog()

    override fun getViewBinding(): FragmentControlBinding {
        return FragmentControlBinding.inflate(layoutInflater)
    }

    override fun initView() {
        binding.btnLight.setOnClickListener {
            loadingDialog.show(childFragmentManager , "")
            controlViewModel.toggleLed("led1")
        }
        binding.btnFan.setOnClickListener {
            loadingDialog.show(childFragmentManager , "")
            controlViewModel.toggleLed("led2")
        }
        binding.btnAC.setOnClickListener {
            loadingDialog.show(childFragmentManager , "")
            controlViewModel.toggleLed("led3")
        }
    }

    override fun observeViewModel() {
        viewLifecycleOwner.lifecycleScope.launch {
            controlViewModel.stateLed.filterNotNull().collect { ledState ->
                updateUi(ledState.led1, binding.txtStateLight, binding.txtLight, binding.loLight, binding.imgLight, binding.btnLight)
                updateUi(ledState.led2, binding.txtStateFan, binding.txtFan, binding.loFan, binding.imgFan, binding.btnFan)
                updateUi(ledState.led3, binding.txtStateAC, binding.txtAC, binding.loAC, binding.imgAC, binding.btnAC)
                loadingDialog.dismiss()
            }
        }
    }


    private fun updateUi(state: String, txtState: TextView, text: TextView, background: ConstraintLayout, icon: ImageView, stateBtn: SwitchCompat) {
        if (state == LedState.ON) {
            txtState.text = "ON"
            txtState.setTextColor(ContextCompat.getColorStateList(requireContext(), R.color.white))
            text.setTextColor(ContextCompat.getColorStateList(requireContext(), R.color.white))
            background.backgroundTintList = ContextCompat.getColorStateList(requireContext(), R.color.dark_464646)
            icon.imageTintList = ContextCompat.getColorStateList(requireContext(), R.color.white)
            stateBtn.isChecked = true
        } else {
            txtState.text = "OFF"
            txtState.setTextColor(ContextCompat.getColorStateList(requireContext(), R.color.dark_464646))
            text.setTextColor(ContextCompat.getColorStateList(requireContext(), R.color.dark_464646))
            background.backgroundTintList = ContextCompat.getColorStateList(requireContext(), R.color.white)
            icon.imageTintList = ContextCompat.getColorStateList(requireContext(), R.color.gray_BDBDBD)
            stateBtn.isChecked = false
        }
    }
}