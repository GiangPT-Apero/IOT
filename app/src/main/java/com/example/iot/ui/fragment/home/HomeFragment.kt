package com.example.iot.ui.fragment.home

import android.util.Log
import androidx.fragment.app.viewModels
import com.example.iot.R
import com.example.iot.adapter.HomeAdapter
import com.example.iot.databinding.FragmentHomeBinding
import com.example.iot.ui.base.BaseFragment
import com.example.iot.viewmodel.DeviceViewModel
import com.example.iot.viewmodel.SensorViewModel
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.LineData
import com.google.android.material.tabs.TabLayoutMediator

class HomeFragment : BaseFragment<FragmentHomeBinding>(R.layout.fragment_home) {

    private val sensorViewModel: SensorViewModel by viewModels()
    private val ledViewModel: DeviceViewModel by viewModels()

    override fun getViewBinding(): FragmentHomeBinding {
        return FragmentHomeBinding.inflate(layoutInflater)
    }

    override fun initView() {
        fetchData()

        binding.viewPager.adapter = HomeAdapter(requireActivity())
        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            when (position) {
                0 -> tab.text = "Temperature"
                1 -> tab.text = "Humidity"
                2 -> tab.text = "Brightness"
            }
        }.attach()

        setupChart()
    }

    override fun observeViewModel() {
        sensorViewModel.temperatureDataSet.observe(viewLifecycleOwner) {
            Log.d("GiangPT", "update chart")
            sensorViewModel.apply {
                val lineData = LineData(
                    temperatureDataSet.value,
                    humidityDataSet.value,
                    lightDataSet.value
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
        sensorViewModel.temperatureDataSet.value?.apply {
            color = resources.getColor(android.R.color.holo_red_dark)
            axisDependency = YAxis.AxisDependency.LEFT
        }

        sensorViewModel.humidityDataSet.value?.apply {
            color = resources.getColor(android.R.color.holo_blue_dark)
            axisDependency = YAxis.AxisDependency.LEFT
        }

        sensorViewModel.lightDataSet.value?.apply {
            color = resources.getColor(android.R.color.holo_orange_dark)
            axisDependency = YAxis.AxisDependency.RIGHT
        }

        val lineData = LineData(sensorViewModel.temperatureDataSet.value, sensorViewModel.humidityDataSet.value, sensorViewModel.lightDataSet.value)
        binding.lineChart.data = lineData
        binding.lineChart.description.isEnabled = false

        binding.lineChart.xAxis.isEnabled = false

        // Cấu hình trục Y
        binding.lineChart.axisLeft.apply {
            setDrawGridLines(false)
            axisMinimum = 0f // Đặt giá trị tối thiểu cho trục Y
            axisMaximum = 100f // Đặt giá trị tối đa cho trục Y
        }

        binding.lineChart.axisRight.apply {
            setDrawGridLines(false)
            axisMaximum = 1000f
            axisMinimum = 0f
        }
    }

    private fun fetchData() {
        sensorViewModel.fetchSensorData()
        ledViewModel.fetchLedData()
    }

    override fun onDestroyView() {
        super.onDestroyView()
    }
}
