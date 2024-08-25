package com.example.iot.viewmodel

import android.app.Application
import androidx.annotation.DrawableRes
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.iot.model.DeviceResponse
import com.example.iot.model.SensorResponse
import kotlin.random.Random

class DeviceViewModel(application: Application): ViewModel() {
    private val _listDeviceResponse = MutableLiveData<ArrayList<DeviceResponse>>()
    val listDeviceResponse: LiveData<ArrayList<DeviceResponse>>
        get() = _listDeviceResponse

    fun generateSampleData() {
        val random = Random.Default

        val deviceData = List(20) { index ->
            DeviceResponse(
                id = index + 1,
                name = "Device ${index + 1}",
                action = random.nextBoolean(),
                time = System.currentTimeMillis() - (index * 1000L)
            )
        }

        _listDeviceResponse.value = ArrayList(deviceData)
    }

    class DeviceViewModelFactory(private val application: Application) : ViewModelProvider.Factory {
        override fun <T: ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(DeviceViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return DeviceViewModel(application) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}