package com.example.iot.ui.fragment.home

import android.graphics.Color
import android.os.Handler
import android.os.Looper
import com.example.iot.R
import com.example.iot.databinding.FragmentHomeBinding
import com.example.iot.ui.base.BaseFragment
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

    private val temperatureDataSet = LineDataSet(mutableListOf(), "Temperature")
    private val humidityDataSet = LineDataSet(mutableListOf(), "Humidity")
    private val lightDataSet = LineDataSet(mutableListOf(), "Light")

    private val handler = Handler(Looper.getMainLooper())
    private val updateRunnable = object : Runnable {
        private var index = 0
        private val xValues = mutableListOf<String>()

        override fun run() {
            if (index == 0) {
                handler.postDelayed(this, 5000)
            }
            xValues.add(index.toString())
            val temperatureEntry = Entry(index.toFloat(), Random.nextFloat() * 30)
            val humidityEntry = Entry(index.toFloat(), Random.nextFloat() * 100)
            val lightEntry = Entry(index.toFloat(), Random.nextFloat() * 1000)

            temperatureDataSet.addEntry(temperatureEntry)
            humidityDataSet.addEntry(humidityEntry)
            lightDataSet.addEntry(lightEntry)


            if (temperatureDataSet.entryCount > MAX_ENTRIES) {
                xValues.removeFirst()
                temperatureDataSet.removeFirst()
                humidityDataSet.removeFirst()
                lightDataSet.removeFirst()
            }

            temperatureDataSet.notifyDataSetChanged()
            humidityDataSet.notifyDataSetChanged()
            lightDataSet.notifyDataSetChanged()

            binding.lineChart.data.notifyDataChanged()
            binding.lineChart.notifyDataSetChanged()
            binding.lineChart.invalidate() // Refresh chart view

            index += 1
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
        temperatureDataSet.apply {
            color = resources.getColor(android.R.color.holo_red_dark)
            axisDependency = YAxis.AxisDependency.LEFT
        }

        humidityDataSet.apply {
            color = resources.getColor(android.R.color.holo_blue_dark)
            axisDependency = YAxis.AxisDependency.LEFT
        }

        lightDataSet.apply {
            color = resources.getColor(android.R.color.holo_orange_dark)
            axisDependency = YAxis.AxisDependency.RIGHT
        }

        val lineData = LineData(temperatureDataSet, humidityDataSet, lightDataSet)
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
