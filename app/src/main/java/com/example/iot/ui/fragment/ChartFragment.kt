package com.example.iot.ui.fragment

import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.iot.R
import com.example.iot.adapter.TableAdapter
import com.example.iot.databinding.FragmentChartBinding
import com.example.iot.databinding.TopBarDeviceTableBinding
import com.example.iot.databinding.TopBarSensorTableBinding
import com.example.iot.ui.base.BaseFragment
import com.example.iot.viewmodel.DeviceViewModel
import com.example.iot.viewmodel.SensorViewModel

class ChartFragment : BaseFragment<FragmentChartBinding>(R.layout.fragment_chart) {

    private val tableAdapter = TableAdapter()

    private val deviceViewModel: DeviceViewModel by lazy {
        ViewModelProvider(
            requireActivity(),
            DeviceViewModel.DeviceViewModelFactory(requireActivity().application)
        )[DeviceViewModel::class.java]
    }

    private val sensorViewModel: SensorViewModel by lazy {
        ViewModelProvider(
            requireActivity(),
            SensorViewModel.SensorViewModelFactory(requireActivity().application)
        )[SensorViewModel::class.java]
    }

    override fun getViewBinding(): FragmentChartBinding {
        return FragmentChartBinding.inflate(layoutInflater)
    }

    override fun initView() {
        binding.txtSensor.setOnClickListener {
            changeTable(isDevice = false)
            tableAdapter.setTypeToDevice(isDevice = false)
        }
        binding.txtDevice.setOnClickListener {
            changeTable(isDevice = true)
            tableAdapter.setTypeToDevice(isDevice = true)
        }
        binding.rvChart.apply {
            adapter = tableAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }
    }

    override fun observeViewModel() {
        deviceViewModel.generateSampleData()
        sensorViewModel.generateSampleDataTable()
        tableAdapter.setListDevice(deviceViewModel.listDeviceResponse.value!!)
        tableAdapter.setListSensor(sensorViewModel.listSensorResponseTable.value!!)
    }

    private fun changeTable(isDevice: Boolean) {
        if (!isDevice) {
            binding.txtSensor.setTextColor(ContextCompat.getColorStateList(requireContext(), R.color.white))
            binding.txtSensor.backgroundTintList = ContextCompat.getColorStateList(requireContext(), R.color.dark_464646)
            binding.txtDevice.setTextColor(ContextCompat.getColorStateList(requireContext(), R.color.dark_464646))
            binding.txtDevice.backgroundTintList = ContextCompat.getColorStateList(requireContext(), R.color.white)

            val sensorTopBar = TopBarSensorTableBinding.inflate(layoutInflater, binding.llTopBar, false)
            binding.llTopBar.apply {
                removeAllViews()
                addView(sensorTopBar.root)
            }
        } else {
            binding.txtDevice.setTextColor(ContextCompat.getColorStateList(requireContext(), R.color.white))
            binding.txtDevice.backgroundTintList = ContextCompat.getColorStateList(requireContext(), R.color.dark_464646)
            binding.txtSensor.setTextColor(ContextCompat.getColorStateList(requireContext(), R.color.dark_464646))
            binding.txtSensor.backgroundTintList = ContextCompat.getColorStateList(requireContext(), R.color.white)

            val deviceTopBar = TopBarDeviceTableBinding.inflate(layoutInflater, binding.llTopBar, false)
            binding.llTopBar.apply {
                removeAllViews()
                addView(deviceTopBar.root)
            }
        }
    }
}