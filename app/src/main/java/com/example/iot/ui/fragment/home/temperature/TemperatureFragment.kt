package com.example.iot.ui.fragment.home

import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import com.example.iot.R
import com.example.iot.databinding.FragmentTemperatureBinding
import com.example.iot.ui.base.BaseFragment
import com.example.iot.viewmodel.SensorViewModel

class TemperatureFragment : BaseFragment<FragmentTemperatureBinding>(R.layout.fragment_temperature) {

    private val sensorViewModel: SensorViewModel by viewModels()

    override fun getViewBinding(): FragmentTemperatureBinding {
        return FragmentTemperatureBinding.inflate(layoutInflater)
    }

    override fun initView() {
    }

    override fun observeViewModel() {
        sensorViewModel.newestSensorResponse.observe(viewLifecycleOwner) {
            binding.txtTemperature.text = it.temperature.toString() + "°C"
            if (it.temperature <= 18) {
                binding.imgTemperature.setImageResource(R.drawable.temp_1)
            } else if (it.temperature <= 28) {
                binding.imgTemperature.setImageResource(R.drawable.temp_2)
            } else if (it.temperature <= 35){
                binding.imgTemperature.setImageResource(R.drawable.temp_3)
            } else {
                binding.imgTemperature.setImageResource(R.drawable.temp_4)
            }
        }
    }

}