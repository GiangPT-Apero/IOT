package com.example.iot.ui.fragment.home

import androidx.lifecycle.ViewModelProvider
import com.example.iot.R
import com.example.iot.databinding.FragmentBrightnessBinding
import com.example.iot.ui.base.BaseFragment
import com.example.iot.viewmodel.SensorViewModel

class BrightnessFragment : BaseFragment<FragmentBrightnessBinding>(R.layout.fragment_brightness) {

    private val sensorViewModel: SensorViewModel by lazy {
        ViewModelProvider(
            requireActivity(),
            SensorViewModel.SensorViewModelFactory(requireActivity().application)
        )[SensorViewModel::class.java]
    }

    override fun getViewBinding(): FragmentBrightnessBinding {
        return FragmentBrightnessBinding.inflate(layoutInflater)
    }

    override fun initView() {
    }

    override fun observeViewModel() {
        sensorViewModel.newestSensorResponse.observe(viewLifecycleOwner) {
            binding.txtBrightness.text = it.tempResponse.toString() + " Lx"
            if (it.tempResponse < 400) {
                binding.imgBrightness.setImageResource(R.drawable.bright_3)
            } else if (it.tempResponse < 700) {
                binding.imgBrightness.setImageResource(R.drawable.bright_2)
            } else {
                binding.imgBrightness.setImageResource(R.drawable.bright_1)
            }
        }
    }

}