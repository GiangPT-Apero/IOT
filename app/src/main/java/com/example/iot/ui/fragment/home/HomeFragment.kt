package com.example.iot.ui.fragment.home

import android.os.Handler
import android.os.Looper
import androidx.lifecycle.ViewModelProvider
import com.example.iot.R
import com.example.iot.adapter.HomeAdapter
import com.example.iot.databinding.FragmentHomeBinding
import com.example.iot.ui.base.BaseFragment
import com.example.iot.viewmodel.SensorViewModel
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.google.android.material.tabs.TabLayoutMediator
import kotlin.random.Random

class HomeFragment : BaseFragment<FragmentHomeBinding>(R.layout.fragment_home) {

    private val MAX_ENTRIES = 20 // Số lượng giá trị mới nhất muốn giữ lại

    private val sensorViewModel: SensorViewModel by lazy {
        ViewModelProvider(
            requireActivity(),
            SensorViewModel.SensorViewModelFactory(requireActivity().application)
        )[SensorViewModel::class.java]
    }

    private val handler = Handler(Looper.getMainLooper())
    private val updateRunnable = object : Runnable {
        private var index = sensorViewModel.temperatureDataSet.entryCount
        private val xValues = mutableListOf<String>()

        override fun run() {
            if (index == 0) {
                handler.postDelayed(this, 5000)
            }
            xValues.add(index.toString())

            sensorViewModel.getNewestSensorResponse(index)

            if (sensorViewModel.temperatureDataSet.entryCount > MAX_ENTRIES) {
                xValues.removeFirst()
                sensorViewModel.temperatureDataSet.removeFirst()
                sensorViewModel.humidityDataSet.removeFirst()
                sensorViewModel.lightDataSet.removeFirst()
            }

            sensorViewModel.temperatureDataSet.notifyDataSetChanged()
            sensorViewModel.humidityDataSet.notifyDataSetChanged()
            sensorViewModel.lightDataSet.notifyDataSetChanged()

            binding.lineChart.data.notifyDataChanged()
            binding.lineChart.notifyDataSetChanged()
            binding.lineChart.invalidate() // Refresh chart view

            binding.lineChart.xAxis.valueFormatter = IndexAxisValueFormatter(xValues)
            binding.lineChart.xAxis.position = XAxis.XAxisPosition.BOTTOM
            binding.lineChart.xAxis.labelCount = xValues.size

            handler.postDelayed(this, 1000) // Update every second
        }
    }

    override fun getViewBinding(): FragmentHomeBinding {
        return FragmentHomeBinding.inflate(layoutInflater)
    }

    override fun initView() {
        binding.viewPager.adapter = HomeAdapter(requireActivity())
        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            when (position) {
                0 -> tab.text = "Temperature"
                1 -> tab.text = "Humidity"
                2 -> tab.text = "Brightness"
            }
        }.attach()

        setupChart()
        startDataUpdates()
    }

    override fun observeViewModel() {
    }

    private fun setupChart() {
        sensorViewModel.temperatureDataSet.apply {
            color = resources.getColor(android.R.color.holo_red_dark)
            axisDependency = YAxis.AxisDependency.LEFT
        }

        sensorViewModel.humidityDataSet.apply {
            color = resources.getColor(android.R.color.holo_blue_dark)
            axisDependency = YAxis.AxisDependency.LEFT
        }

        sensorViewModel.lightDataSet.apply {
            color = resources.getColor(android.R.color.holo_orange_dark)
            axisDependency = YAxis.AxisDependency.RIGHT
        }

        val lineData = LineData(sensorViewModel.temperatureDataSet, sensorViewModel.humidityDataSet, sensorViewModel.lightDataSet)
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


    private fun startDataUpdates() {
        updateRunnable.run()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        handler.removeCallbacks(updateRunnable)
    }
}
