package com.example.iot.ui.fragment

import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.iot.R
import com.example.iot.adapter.TableAdapter
import com.example.iot.databinding.FragmentChartBinding
import com.example.iot.databinding.TopBarDeviceTableBinding
import com.example.iot.databinding.TopBarSensorTableBinding
import com.example.iot.ui.base.BaseFragment
import com.example.iot.ui.fragment.dialog.LoadingDialog
import com.example.iot.viewmodel.DeviceViewModel
import com.example.iot.viewmodel.SensorViewModel
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.launch

class ChartFragment : BaseFragment<FragmentChartBinding>(R.layout.fragment_chart) {

    private val tableAdapter = TableAdapter()
    private val loadingDialog = LoadingDialog()

    private val sensorViewModel: SensorViewModel by activityViewModels<SensorViewModel>()
    private val ledViewModel: DeviceViewModel by activityViewModels<DeviceViewModel>()

    override fun getViewBinding(): FragmentChartBinding {
        return FragmentChartBinding.inflate(layoutInflater)
    }

    override fun initView() {
        with(binding) {
            txtSensor.setOnClickListener {
                changeTable(isDevice = false)
                tableAdapter.setTypeToDevice(isDevice = false)
            }
            txtDevice.setOnClickListener {
                changeTable(isDevice = true)
                tableAdapter.setTypeToDevice(isDevice = true)
            }
            rvChart.apply {
                adapter = tableAdapter
                layoutManager = LinearLayoutManager(requireContext())
            }

            imgNext.setOnClickListener {
                if (tableAdapter.isDeviceType()) {
                    ledViewModel.navigatePage(true)
                } else {
                    sensorViewModel.navigatePage(true)
                }
                loadingDialog.show(childFragmentManager , "")
            }

            imgPrevious.setOnClickListener {
                if (tableAdapter.isDeviceType()) {
                    ledViewModel.navigatePage(false)
                } else {
                    sensorViewModel.navigatePage(false)
                }
                loadingDialog.show(childFragmentManager , "")
            }
        }
    }

    override fun observeViewModel() {
        viewLifecycleOwner.lifecycleScope.launch {
            ledViewModel.listDeviceResponse.filterNotNull().collect { listDeviceResponse ->
                tableAdapter.setListDevice(ArrayList(listDeviceResponse.content))
                binding.txtPage.text = "${listDeviceResponse.number + 1}/${listDeviceResponse.totalPages}"
                if (loadingDialog.isAdded) {
                    loadingDialog.dismiss()
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            sensorViewModel.listSensorResponseTable.filterNotNull().collect { listSensorResponseTable ->
                tableAdapter.setListSensor(ArrayList(listSensorResponseTable.content))
                binding.txtPage.text = "${listSensorResponseTable.number + 1}/${listSensorResponseTable.totalPages}"
                if (loadingDialog.isAdded) {
                    loadingDialog.dismiss()
                }
            }
        }
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