package com.example.iot.viewmodel

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.iot.model.SensorResponse
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import kotlin.random.Random

class SensorViewModel(application: Application) : ViewModel() {
    private val _listSensorResponseTable = MutableLiveData<ArrayList<SensorResponse>>()
    val listSensorResponseTable: LiveData<ArrayList<SensorResponse>>
        get() = _listSensorResponseTable

    private val _newestSensorResponse = MutableLiveData<SensorResponse>()
    val newestSensorResponse: LiveData<SensorResponse>
        get() = _newestSensorResponse

    val temperatureDataSet = LineDataSet(mutableListOf(), "Temperature")
    val humidityDataSet = LineDataSet(mutableListOf(), "Humidity")
    val lightDataSet = LineDataSet(mutableListOf(), "Light")

    fun generateSampleDataTable() {
        val sensorData = List(20) { index ->
            SensorResponse (
                id = index + 1,
                time = System.currentTimeMillis() - (index * 1000L),
                brightResponse = (0..1000).random().toLong(),
                humResponse = (0..100).random().toLong(),
                tempResponse = (0..40).random().toLong()
            )
        }

        _listSensorResponseTable.value = ArrayList(sensorData)
    }

    fun getNewestSensorResponse(index: Int) {
        _newestSensorResponse.value = SensorResponse(
            index,
            0,
            tempResponse = (0..40).random().toLong(),
            humResponse = (0..100).random().toLong(),
            brightResponse = (0..1000).random().toLong(),
        )

        temperatureDataSet.addEntry(Entry(index.toFloat(), _newestSensorResponse.value!!.tempResponse.toFloat()))
        humidityDataSet.addEntry(Entry(index.toFloat(), _newestSensorResponse.value!!.humResponse.toFloat()))
        lightDataSet.addEntry(Entry(index.toFloat(), _newestSensorResponse.value!!.brightResponse.toFloat()))
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