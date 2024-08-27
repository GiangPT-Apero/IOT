package com.example.iot.ui.fragment.home

import androidx.lifecycle.ViewModelProvider
import com.example.iot.R
import com.example.iot.databinding.FragmentTemperatureBinding
import com.example.iot.ui.base.BaseFragment
import com.example.iot.viewmodel.SensorViewModel

class TemperatureFragment : BaseFragment<FragmentTemperatureBinding>(R.layout.fragment_temperature) {

    private val sensorViewModel: SensorViewModel by lazy {
        ViewModelProvider(
            requireActivity(),
            SensorViewModel.SensorViewModelFactory(requireActivity().application)
        )[SensorViewModel::class.java]
    }

    override fun getViewBinding(): FragmentTemperatureBinding {
        return FragmentTemperatureBinding.inflate(layoutInflater)
    }

    override fun initView() {
    }

    override fun observeViewModel() {
        sensorViewModel.newestSensorResponse.observe(viewLifecycleOwner) {
            binding.txtTemperature.text = it.tempResponse.toString() + "°C"
            if (it.tempResponse <= 18) {
                binding.imgTemperature.setImageResource(R.drawable.temp_1)
            } else if (it.tempResponse <= 28) {
                binding.imgTemperature.setImageResource(R.drawable.temp_2)
            } else if (it.tempResponse <= 35){
                binding.imgTemperature.setImageResource(R.drawable.temp_3)
            } else {
                binding.imgTemperature.setImageResource(R.drawable.temp_4)
            }
        }
    }

}