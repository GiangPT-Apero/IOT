package com.example.iot.viewmodel

import android.app.Application
import androidx.annotation.DrawableRes
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class DeviceViewModel(application: Application): ViewModel() {
    private val _deviceList = MutableLiveData<List<Device>>()
    val deviceList: LiveData<List<Device>>
        get() = _deviceList

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

enum class Device (
    val id: Int,
    val deviceName: String,
    @DrawableRes
    val icon: Int,
    val isOn: Boolean,
) {

}