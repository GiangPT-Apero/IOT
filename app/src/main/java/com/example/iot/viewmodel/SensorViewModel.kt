package com.example.iot.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.iot.model.SensorResponse
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.random.Random

class SensorViewModel(application: Application) : ViewModel() {
    private val MAX_ENTRIES = 20 // Số lượng giá trị mới nhất muốn giữ lại

    private var indexChart = 0

    private val _listSensorResponseTable = MutableLiveData<ArrayList<SensorResponse>>()
    val listSensorResponseTable: LiveData<ArrayList<SensorResponse>>
        get() = _listSensorResponseTable

    private val _newestSensorResponse = MutableLiveData<SensorResponse>()
    val newestSensorResponse: LiveData<SensorResponse>
        get() = _newestSensorResponse

    private val _temperatureDataSet = MutableLiveData<LineDataSet?>(LineDataSet(mutableListOf(), "Temperature"))
    val temperatureDataSet: LiveData<LineDataSet?>
        get() = _temperatureDataSet

    private val _humidityDataSet = MutableLiveData<LineDataSet?>(LineDataSet(mutableListOf(), "Humidity"))
    val humidityDataSet: LiveData<LineDataSet?>
        get() = _humidityDataSet

    private val _lightDataSet = MutableLiveData<LineDataSet?>(LineDataSet(mutableListOf(), "Light"))
    val lightDataSet: LiveData<LineDataSet?>
        get() = _lightDataSet

    private fun startUpdateRunnable() {
        viewModelScope.launch {
            while (true) {
                getNewestSensorResponse()
                delay(1000)
            }
        }
    }

    init {
        startUpdateRunnable()
    }

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

    private fun getNewestSensorResponse() {
        _newestSensorResponse.value = SensorResponse(
            indexChart,
            0,
            tempResponse = (0..40).random().toLong(),
            humResponse = (0..100).random().toLong(),
            brightResponse = (0..1000).random().toLong(),
        )
        Log.d("GiangPT", "${_newestSensorResponse.value}")

        val tempDataSet = _temperatureDataSet.value?.apply {
            if (entryCount >= MAX_ENTRIES) {
                removeFirst()
            }
            addEntry(Entry(indexChart.toFloat(), _newestSensorResponse.value!!.tempResponse.toFloat()))
            notifyDataSetChanged()
        }

        val humDataSet = _humidityDataSet.value?.apply {
            if (entryCount >= MAX_ENTRIES) {
                removeFirst()
            }
            addEntry(Entry(indexChart.toFloat(), _newestSensorResponse.value!!.humResponse.toFloat()))
            notifyDataSetChanged()
        }

        val lightDataSet = _lightDataSet.value?.apply {
            if (entryCount >= MAX_ENTRIES) {
                removeFirst()
            }
            addEntry(Entry(indexChart.toFloat(), _newestSensorResponse.value!!.brightResponse.toFloat()))
            notifyDataSetChanged()
        }

        indexChart++

        _humidityDataSet.postValue(humDataSet)
        _lightDataSet.postValue(lightDataSet)
        _temperatureDataSet.postValue(tempDataSet)

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