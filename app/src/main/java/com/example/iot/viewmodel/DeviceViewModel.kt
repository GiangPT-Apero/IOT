package com.example.iot.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.iot.model.LedData
import com.example.iot.model.PageResponse
import com.example.iot.model.TypeSearchLed
import com.example.iot.model.TypeSearchSensor
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

    fun navigatePage(isNext: Boolean, sort: Int) {
        if (isNext && pageIndex + 1 == _listDeviceResponse.value?.totalPages) return
        if (!isNext && pageIndex - 1 < 0) return
        pageIndex = if (isNext) pageIndex + 1 else pageIndex - 1
        fetchLedData(sort)
    }

    fun search(type: TypeSearchLed, request: String, sort: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val param = request
                val response = when (type) {
                    TypeSearchLed.NAME -> RetrofitInstance.ledApi.getByLedName(param, sort = sort)
                    TypeSearchLed.ACTION -> RetrofitInstance.ledApi.getByAction(param, sort = sort)
                    else -> RetrofitInstance.ledApi.getAllData(pageIndex, itemPerPage, sort = sort)
                }
                if (_listDeviceResponse.value != response) {
                    _listDeviceResponse.emit(response)
                }
            } catch (e: IOException) {
                // Xử lý lỗi kết nối
                Log.d("GiangPT all sensor data IOE", e.toString())
            } catch (e: HttpException) {
                // Xử lý lỗi HTTP
                Log.d("GiangPT all sensor data HTTP", e.toString())
            }
        }
    }

    fun fetchLedData(sort: Int = 1) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                Log.d("GiangPT", "fetch led data $pageIndex - $itemPerPage")
                val response = RetrofitInstance.ledApi.getAllData(pageIndex, itemPerPage,  sort = sort)
                if (_listDeviceResponse.value != response) {
                    _listDeviceResponse.emit(response)
                }
            } catch (e: IOException) {
                // Xử lý lỗi kết nối
                Log.d("GiangPT IOE LED", e.toString())
            } catch (e: HttpException) {
                // Xử lý lỗi HTTP
                Log.d("GiangPT 1 HTTP", e.toString())
            }
        }
    }
}