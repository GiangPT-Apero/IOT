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
import com.example.iot.databinding.TopBarDeviceTableBinding
import com.example.iot.model.TypeSearchLed
import com.example.iot.ui.base.BaseFragment
import com.example.iot.ui.fragment.dialog.LoadingDialog
import com.example.iot.viewmodel.DeviceViewModel
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.launch

class LedChartFragment : BaseFragment<FragmentChartBinding>(R.layout.fragment_chart) {

    private val tableAdapter = TableAdapter()
    private val loadingDialog = LoadingDialog()
    private val ledViewModel: DeviceViewModel by activityViewModels<DeviceViewModel>()
    private var sortType = 1
    private var typeSearchLed: TypeSearchLed = TypeSearchLed.ALL

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
        binding.txtDevice.setTextColor(ContextCompat.getColor(requireContext(), R.color.white))
        binding.txtDevice.backgroundTintList = ContextCompat.getColorStateList(requireContext(), R.color.dark_464646)
        binding.txtSensor.setTextColor(ContextCompat.getColor(requireContext(), R.color.dark_464646))
        binding.txtSensor.backgroundTintList = ContextCompat.getColorStateList(requireContext(), R.color.white)

        val deviceTopBar = TopBarDeviceTableBinding.inflate(layoutInflater, binding.llTopBar, false)
        binding.llTopBar.apply {
            removeAllViews()
            addView(deviceTopBar.root)
        }
    }

    private fun setupRecyclerView() {
        tableAdapter.setTypeToDevice(true)
        binding.rvChart.apply {
            adapter = tableAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }
    }

    private fun setupClickListeners() {
        binding.txtDevice.setOnClickListener {
            Log.d("GiangPT", "click led")
            ledViewModel.pageIndex = 0
            ledViewModel.fetchLedData(sortType)
            loadingDialog.show(childFragmentManager, "")
        }
        binding.imgSort.setOnClickListener { showPopupLed(binding.imgSort) }
        binding.imgSearch.setOnClickListener { performSearch() }
        binding.imgNext.setOnClickListener {
            ledViewModel.navigatePage(true, sortType)
            loadingDialog.show(childFragmentManager , "")
        }
        binding.imgPrevious.setOnClickListener {
            ledViewModel.navigatePage(false, sortType)
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
        ledViewModel.search(typeSearchLed, temp, sortType)
        loadingDialog.show(childFragmentManager, "")
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
    }

    @SuppressLint("MissingInflatedId")
    private fun showPopupLed(anchorView: View) {
        val popupView = LayoutInflater.from(context).inflate(R.layout.popup_layout_led, null)
        val popupWindow = PopupWindow(popupView, WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT)

        popupWindow.isOutsideTouchable = true
        popupWindow.isFocusable = true
        popupWindow.showAsDropDown(anchorView)

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
