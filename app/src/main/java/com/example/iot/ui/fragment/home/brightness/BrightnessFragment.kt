package com.example.iot.ui.fragment.home.brightness

import android.os.Handler
import android.util.Log
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import com.example.iot.R
import com.example.iot.databinding.FragmentBrightnessBinding
import com.example.iot.databinding.FragmentHomeBinding
import com.example.iot.ui.base.BaseFragment
import com.example.iot.viewmodel.DeviceViewModel
import com.example.iot.viewmodel.SensorViewModel
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.LineData

class BrightnessFragment : BaseFragment<FragmentBrightnessBinding>(R.layout.fragment_brightness) {

    private val sensorViewModel: SensorViewModel by activityViewModels<SensorViewModel>()
    private val ledViewModel: DeviceViewModel by activityViewModels<DeviceViewModel>()

    private val handler = Handler()
    private var isBlinking = false

    override fun getViewBinding(): FragmentBrightnessBinding {
        return FragmentBrightnessBinding.inflate(layoutInflater)
    }

    override fun initView() {
        setupChart()
        observeData()
    }

    private fun startBlinking() {
        if (isBlinking) return

        isBlinking = true
        var count = 0
        val blinkRunnable = object : Runnable {
            override fun run() {
                if (count % 2 == 0) {
                    binding.imgTemperature.setImageResource(R.drawable.ic_warning_red)
                } else {
                    binding.imgTemperature.setImageResource(R.drawable.ic_warning)
                }
                count += 1
                handler.postDelayed(this, 500L)
            }
        }

        handler.post(blinkRunnable)
    }

    private fun stopBlinking() {
        isBlinking = false
        handler.removeCallbacksAndMessages(null) // Loại bỏ tất cả các Runnable
        binding.imgTemperature.isVisible = true // Đảm bảo ImageView vẫn hiển thị (hoặc bạn có thể ẩn nó)
    }

    private fun observeData() {
        sensorViewModel.newestRandomResponse.observe(viewLifecycleOwner) {
            binding.txtTemperature.text = it.value.toString() + " m/s"
            if (it.value >= 60) {
                startBlinking()
            } else {
                binding.imgTemperature.setImageResource(R.drawable.ic_warning)
                stopBlinking()
            }
        }

    }
    override fun observeViewModel() {
        sensorViewModel.randomDataSet.observe(viewLifecycleOwner) {
            Log.d("GiangPT", "update chart")
            sensorViewModel.apply {
                val lineData = LineData(
                    randomDataSet.value
                )
                binding.lineChart.data = lineData
                binding.lineChart.data.notifyDataChanged()
                binding.lineChart.notifyDataSetChanged()
                binding.lineChart.invalidate()

                binding.lineChart.xAxis.position = XAxis.XAxisPosition.BOTTOM
                binding.lineChart.xAxis.labelCount = temperatureDataSet.value?.entryCount ?: 0
            }
        }
    }

    private fun setupChart() {
        sensorViewModel.randomDataSet.value?.apply {
            color = resources.getColor(android.R.color.holo_red_dark)
            axisDependency = YAxis.AxisDependency.LEFT
        }

        val lineData = LineData(sensorViewModel.temperatureDataSet.value)
        binding.lineChart.data = lineData
        binding.lineChart.description.isEnabled = false

        binding.lineChart.xAxis.isEnabled = false

        // Cấu hình trục Y
        binding.lineChart.axisLeft.apply {
            setDrawGridLines(false)
            axisMinimum = 0f // Đặt giá trị tối thiểu cho trục Y
            axisMaximum = 100f // Đặt giá trị tối đa cho trục Y
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
    }

}