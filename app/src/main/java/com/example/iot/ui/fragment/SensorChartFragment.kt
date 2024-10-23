package com.example.iot.ui.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import android.widget.PopupWindow
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.iot.R
import com.example.iot.adapter.TableAdapter
import com.example.iot.databinding.FragmentChartBinding
import com.example.iot.databinding.TopBarSensorTableBinding
import com.example.iot.model.TypeSearchSensor
import com.example.iot.ui.base.BaseFragment
import com.example.iot.ui.fragment.dialog.LoadingDialog
import com.example.iot.viewmodel.SensorViewModel
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.launch

class SensorChartFragment : BaseFragment<FragmentChartBinding>(R.layout.fragment_chart) {

    private val tableAdapter = TableAdapter()
    private val loadingDialog = LoadingDialog()
    private val sensorViewModel: SensorViewModel by activityViewModels<SensorViewModel>()
    private var sortType = 1
    private var typeSearchSensor: TypeSearchSensor = TypeSearchSensor.ALL

    override fun getViewBinding(): FragmentChartBinding {
        return FragmentChartBinding.inflate(layoutInflater)
    }

    override fun initView() {
        with(binding) {
            setupView()
            setupRecyclerView()
            setupClickListeners()
        }
    }

    private fun setupView() {
        binding.txtSensor.setTextColor(ContextCompat.getColor(requireContext(), R.color.white))
        binding.txtSensor.backgroundTintList = ContextCompat.getColorStateList(requireContext(), R.color.dark_464646)
        binding.txtDevice.setTextColor(ContextCompat.getColor(requireContext(), R.color.dark_464646))
        binding.txtDevice.backgroundTintList = ContextCompat.getColorStateList(requireContext(), R.color.white)

        val sensorTopBar = TopBarSensorTableBinding.inflate(layoutInflater, binding.llTopBar, false)
        binding.llTopBar.apply {
            removeAllViews()
            addView(sensorTopBar.root)
        }
    }

    private fun setupRecyclerView() {
        binding.rvChart.apply {
            adapter = tableAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }
    }

    private fun setupClickListeners() {
        binding.txtSensor.setOnClickListener {
            Log.d("GiangPT", "click sensor")
            sensorViewModel.pageIndex = 0
            sensorViewModel.fetchSensorData(sortType)
            loadingDialog.show(childFragmentManager, "")
        }
        binding.imgSort.setOnClickListener { showPopupSensor(binding.imgSort) }
        binding.imgSearch.setOnClickListener { performSearch() }
        binding.imgNext.setOnClickListener {
            sensorViewModel.navigatePage(true, sortType)
            loadingDialog.show(childFragmentManager , "")
        }
        binding.imgPrevious.setOnClickListener {
            sensorViewModel.navigatePage(false, sortType)
            loadingDialog.show(childFragmentManager , "")
        }
        binding.imgUpDown.setOnClickListener {
            if (sortType == 1) {
                sortType = 0
                binding.imgUpDown.setImageResource(R.drawable.ic_sort_down)
            } else {
                sortType = 1
                binding.imgUpDown.setImageResource(R.drawable.ic_sort_up)
            }
        }
    }

    private fun performSearch() {
        val temp = binding.edtSearch.text.toString()
        sensorViewModel.search(typeSearchSensor, temp, sortType)
        loadingDialog.show(childFragmentManager, "")
    }

    override fun observeViewModel() {
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

    @SuppressLint("MissingInflatedId")
    private fun showPopupSensor(anchorView: View) {
        val popupView = LayoutInflater.from(context).inflate(R.layout.popup_layout_sensor, null)
        val popupWindow = PopupWindow(popupView, WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT)

        popupWindow.isOutsideTouchable = true
        popupWindow.isFocusable = true
        popupWindow.showAsDropDown(anchorView)

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
}
