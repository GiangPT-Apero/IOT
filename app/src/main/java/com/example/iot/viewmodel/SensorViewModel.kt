package com.example.iot.viewmodel

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.iot.model.SensorResponse

class SensorViewModel(application: Application) : ViewModel() {
    private val _listSensorResponse = MutableLiveData<ArrayList<SensorResponse>>()
    val listSensorResponse: LiveData<ArrayList<SensorResponse>>
        get() = _listSensorResponse

    fun generateSampleData() {
        val sensorData = List(20) { index ->
            SensorResponse (
                id = index + 1,
                time = System.currentTimeMillis() - (index * 1000L),
                brightResponse = (0..100).random().toLong(),
                humResponse = (0..100).random().toLong(),
                tempResponse = (0..40).random().toLong()
            )
        }

        _listSensorResponse.value = ArrayList(sensorData)
    }

    class SensorViewModelFactory(private val application: Application) : ViewModelProvider.Factory {
        override fun <T: ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(SensorViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return SensorViewModel(application) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}