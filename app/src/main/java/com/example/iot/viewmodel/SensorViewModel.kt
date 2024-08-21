package com.example.iot.viewmodel

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.iot.model.BrightSensorResponse
import com.example.iot.model.HumSensorResponse
import com.example.iot.model.TempSensorResponse

class SensorViewModel(application: Application) : ViewModel() {
    private val _listTempResponse = MutableLiveData<ArrayList<TempSensorResponse>>()
    val listTempResponse: LiveData<ArrayList<TempSensorResponse>>
        get() = _listTempResponse

    private val _listHumResponse = MutableLiveData<ArrayList<HumSensorResponse>>()
    val listHumResponse: LiveData<ArrayList<HumSensorResponse>>
        get() = _listHumResponse

    private val _listBrightResponse = MutableLiveData<ArrayList<BrightSensorResponse>>()
    val listBrightResponse: LiveData<ArrayList<BrightSensorResponse>>
        get() = _listBrightResponse


    fun generateSampleData() {
        val brightSensorData = List(10) { index ->
            BrightSensorResponse(
                id = index + 1,
                time = System.currentTimeMillis() - (index * 1000L),
                result = (0..100).random().toLong()  // Generating a random brightness value between 0 and 100
            )
        }

        val humSensorData = List(10) { index ->
            HumSensorResponse(
                id = index + 1,
                time = System.currentTimeMillis() - (index * 1000L),
                result = (0..100).random().toLong()  // Generating a random humidity value between 0 and 100
            )
        }

        val tempSensorData = List(10) { index ->
            TempSensorResponse(
                id = index + 1,
                time = System.currentTimeMillis() - (index * 1000L),
                result = (0..40).random().toLong()  // Generating a random temperature value between -20 and 40 degrees Celsius
            )
        }

        _listBrightResponse.value = ArrayList(brightSensorData)
        _listHumResponse.value = ArrayList(humSensorData)
        _listTempResponse.value = ArrayList(tempSensorData)
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