package com.example.iot.ui.fragment.home.humidity

import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import com.example.iot.R
import com.example.iot.databinding.FragmentHumidityBinding
import com.example.iot.ui.base.BaseFragment
import com.example.iot.viewmodel.SensorViewModel

class HumidityFragment : BaseFragment<FragmentHumidityBinding>(R.layout.fragment_humidity) {

    private val sensorViewModel: SensorViewModel by activityViewModels<SensorViewModel>()

    override fun getViewBinding(): FragmentHumidityBinding {
        return FragmentHumidityBinding.inflate(layoutInflater)
    }

    override fun initView() {
    }

    override fun observeViewModel() {
        sensorViewModel.newestSensorResponse.observe(viewLifecycleOwner) {
            binding.txtHumidity.text = it.humidity.toString() + " %"
            if (it.humidity > 50) {
                binding.imgHumidity.setImageResource(R.drawable.hum_2)
            } else {
                binding.imgHumidity.setImageResource(R.drawable.hum_1)
            }
        }
    }

}