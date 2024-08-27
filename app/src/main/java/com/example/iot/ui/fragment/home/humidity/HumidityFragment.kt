package com.example.iot.ui.fragment.home

import androidx.lifecycle.ViewModelProvider
import com.example.iot.R
import com.example.iot.databinding.FragmentHumidityBinding
import com.example.iot.ui.base.BaseFragment
import com.example.iot.viewmodel.SensorViewModel

class HumidityFragment : BaseFragment<FragmentHumidityBinding>(R.layout.fragment_humidity) {

    private val sensorViewModel: SensorViewModel by lazy {
        ViewModelProvider(
            requireActivity(),
            SensorViewModel.SensorViewModelFactory(requireActivity().application)
        )[SensorViewModel::class.java]
    }

    override fun getViewBinding(): FragmentHumidityBinding {
        return FragmentHumidityBinding.inflate(layoutInflater)
    }

    override fun initView() {
    }

    override fun observeViewModel() {
        sensorViewModel.newestSensorResponse.observe(viewLifecycleOwner) {
            binding.txtHumidity.text = it.humResponse.toString() + " %"
            if (it.humResponse > 50) {
                binding.imgHumidity.setImageResource(R.drawable.hum_2)
            } else {
                binding.imgHumidity.setImageResource(R.drawable.hum_1)
            }
        }
    }

}