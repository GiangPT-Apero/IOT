package com.example.iot.ui.fragment

import android.annotation.SuppressLint
import android.content.Context.LAYOUT_INFLATER_SERVICE
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import android.widget.PopupWindow
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.getSystemService
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.iot.R
import com.example.iot.adapter.TableAdapter
import com.example.iot.databinding.FragmentChartBinding
import com.example.iot.databinding.TopBarDeviceTableBinding
import com.example.iot.databinding.TopBarSensorTableBinding
import com.example.iot.model.TypeSearchLed
import com.example.iot.model.TypeSearchSensor
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
    private var sortType = 1
    private var typeSearchSensor: TypeSearchSensor = TypeSearchSensor.ALL
    private var typeSearchLed: TypeSearchLed = TypeSearchLed.ALL

    override fun getViewBinding(): FragmentChartBinding {
        return FragmentChartBinding.inflate(layoutInflater)
    }

    override fun initView() {
        with(binding) {
            txtSensor.setOnClickListener {
                changeTable(isDevice = false)
                tableAdapter.setTypeToDevice(isDevice = false)
                sensorViewModel.fetchSensorData(sort = sortType)
                binding.txtPage.text = "${sensorViewModel.listSensorResponseTable.value?.number?: 0 + 1}/${sensorViewModel.listSensorResponseTable.value?.totalPages}"
                loadingDialog.show(childFragmentManager, "")
            }
            txtDevice.setOnClickListener {
                changeTable(isDevice = true)
                tableAdapter.setTypeToDevice(isDevice = true)
                ledViewModel.fetchLedData(sort = sortType)
                binding.txtPage.text = "${ledViewModel.listDeviceResponse.value?.number?: 0 + 1}/${ledViewModel.listDeviceResponse.value?.totalPages}"
                loadingDialog.show(childFragmentManager, "")
            }
            rvChart.apply {
                adapter = tableAdapter
                layoutManager = LinearLayoutManager(requireContext())
            }

            imgNext.setOnClickListener {
                if (tableAdapter.isDeviceType()) {
                    ledViewModel.navigatePage(true, sort = sortType)
                } else {
                    sensorViewModel.navigatePage(true,  sort = sortType)
                }
                loadingDialog.show(childFragmentManager , "")
            }

            imgPrevious.setOnClickListener {
                if (tableAdapter.isDeviceType()) {
                    ledViewModel.navigatePage(false, sort = sortType)
                } else {
                    sensorViewModel.navigatePage(false, sort = sortType)
                }
                loadingDialog.show(childFragmentManager , "")
            }

            imgUpDown.setOnClickListener {
                if (sortType == 1) {
                    sortType = 0
                    imgUpDown.setImageResource(R.drawable.ic_sort_down)
                } else {
                    sortType = 1
                    imgUpDown.setImageResource(R.drawable.ic_sort_up)
                }
            }

            imgSort.setOnClickListener {
                if (tableAdapter.isDeviceType()) {
                    showPopupLed(imgSort)
                } else {
                    showPopupSensor(imgSort)
                }
            }

            imgSearch.setOnClickListener {
                var temp = ""
                if (edtSearch.text != null) temp = edtSearch.text.toString()
                if (tableAdapter.isDeviceType()) {
                    ledViewModel.search(typeSearchLed, temp, sortType)
                    loadingDialog.show(childFragmentManager , "")
                } else {
                    sensorViewModel.search(typeSearchSensor, temp, sortType)
                    loadingDialog.show(childFragmentManager , "")
                }
            }
        }
    }

    override fun observeViewModel() {
        viewLifecycleOwner.lifecycleScope.launch {
            ledViewModel.listDeviceResponse.filterNotNull().collect { listDeviceResponse ->
                Log.d("GiangPT led", "${listDeviceResponse.number} - ${listDeviceResponse.totalPages}")
                tableAdapter.setListDevice(ArrayList(listDeviceResponse.content))
                binding.txtPage.text = "${listDeviceResponse.number + 1}/${listDeviceResponse.totalPages}"
                ledViewModel.pageIndex = listDeviceResponse.number
                if (loadingDialog.isAdded) {
                    loadingDialog.dismiss()
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            sensorViewModel.listSensorResponseTable.filterNotNull().collect { listSensorResponseTable ->
                Log.d("GiangPT sensor", "${listSensorResponseTable.number} - ${listSensorResponseTable.totalPages}")
                tableAdapter.setListSensor(ArrayList(listSensorResponseTable.content))
                binding.txtPage.text = "${listSensorResponseTable.number + 1}/${listSensorResponseTable.totalPages}"
                sensorViewModel.pageIndex = listSensorResponseTable.number
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

    @SuppressLint("MissingInflatedId")
    private fun showPopupSensor(anchorView: View) {
        val popupView =
            LayoutInflater.from(context).inflate(R.layout.popup_layout_sensor, null)
        val popupWindow = PopupWindow(
            popupView,
            WindowManager.LayoutParams.WRAP_CONTENT,
            WindowManager.LayoutParams.WRAP_CONTENT
        )

        popupWindow.isOutsideTouchable = true
        popupWindow.isFocusable = true

        popupWindow.showAsDropDown(anchorView)

        // Xử lý sự kiện click cho các mục trong PopupWindow
        val txtTemp = popupView.findViewById<TextView>(R.id.txtPopupTemp)
        val txtHum = popupView.findViewById<TextView>(R.id.txtPopupHum)
        val txtLight = popupView.findViewById<TextView>(R.id.txtPopupLight)

        txtTemp.setOnClickListener {
            typeSearchSensor = TypeSearchSensor.TEMP
            popupWindow.dismiss()
        }
        txtHum.setOnClickListener {
            typeSearchSensor = TypeSearchSensor.HUM
            popupWindow.dismiss()
        }
        txtLight.setOnClickListener {
            typeSearchSensor = TypeSearchSensor.LIGHT
            popupWindow.dismiss()
        }
    }

    @SuppressLint("MissingInflatedId")
    private fun showPopupLed(anchorView: View) {
        val popupView =
            LayoutInflater.from(context).inflate(R.layout.popup_layout_led, null)
        val popupWindow = PopupWindow(
            popupView,
            WindowManager.LayoutParams.WRAP_CONTENT,
            WindowManager.LayoutParams.WRAP_CONTENT
        )

        popupWindow.isOutsideTouchable = true
        popupWindow.isFocusable = true

        popupWindow.showAsDropDown(anchorView)

        // Xử lý sự kiện click cho các mục trong PopupWindow
        val txtName = popupView.findViewById<TextView>(R.id.txtPopupName)
        val txtAction = popupView.findViewById<TextView>(R.id.txtPopupAction)

        txtName.setOnClickListener {
            typeSearchLed = TypeSearchLed.NAME
            popupWindow.dismiss()
        }
        txtAction.setOnClickListener {
            typeSearchLed = TypeSearchLed.ACTION
            popupWindow.dismiss()
        }
    }
}