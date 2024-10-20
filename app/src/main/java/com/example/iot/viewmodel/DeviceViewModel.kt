package com.example.iot.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.iot.model.LedData
import com.example.iot.model.PageResponse
import com.example.iot.retrofit.RetrofitInstance
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

class DeviceViewModel: ViewModel() {
    private val _listDeviceResponse = MutableStateFlow<PageResponse<LedData>?>(null)
    val listDeviceResponse = _listDeviceResponse.asStateFlow()

    var itemPerPage = 20
    var pageIndex = 0

    fun navigatePage(isNext: Boolean) {
        if (pageIndex + 1 == _listDeviceResponse.value?.totalPages) return
        pageIndex = if (isNext) pageIndex + 1 else pageIndex - 1
        fetchLedData()
    }

    fun fetchLedData() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = RetrofitInstance.ledApi.getAllData(pageIndex, itemPerPage)
                _listDeviceResponse.emit(response)
            } catch (e: IOException) {
                // Xử lý lỗi kết nối
                Log.d("GiangPT IOE", e.toString())
            } catch (e: HttpException) {
                // Xử lý lỗi HTTP
                Log.d("GiangPT 1 HTTP", e.toString())
            }
        }
    }
}