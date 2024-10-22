package com.example.iot.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.iot.model.PageResponse
import com.example.iot.model.SensorData
import com.example.iot.model.TypeSearchSensor
import com.example.iot.retrofit.RetrofitInstance
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineDataSet
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

class SensorViewModel : ViewModel() {
    companion object {
        const val MAX_ENTRIES = 20
    }

    private var indexChart = 0
    var itemPerPage = 20
    var pageIndex = 0

    private val _listSensorResponseTable = MutableStateFlow<PageResponse<SensorData>?>(null)
    val listSensorResponseTable = _listSensorResponseTable.asStateFlow()

    private val _newestSensorResponse = MutableLiveData<SensorData>()
    val newestSensorResponse: LiveData<SensorData>
        get() = _newestSensorResponse

    private val _temperatureDataSet =
        MutableLiveData<LineDataSet?>(LineDataSet(mutableListOf(), "Temperature"))
    val temperatureDataSet: LiveData<LineDataSet?>
        get() = _temperatureDataSet

    private val _humidityDataSet =
        MutableLiveData<LineDataSet?>(LineDataSet(mutableListOf(), "Humidity"))
    val humidityDataSet: LiveData<LineDataSet?>
        get() = _humidityDataSet

    private val _lightDataSet = MutableLiveData<LineDataSet?>(LineDataSet(mutableListOf(), "Light"))
    val lightDataSet: LiveData<LineDataSet?>
        get() = _lightDataSet

    private fun startUpdateRunnable() {
        viewModelScope.launch(Dispatchers.IO) {
            while (true) {
                getNewestSensorResponse()
                delay(1000)
            }
        }
    }

    fun navigatePage(isNext: Boolean, sort: Int) {
        if (isNext && pageIndex + 1 == _listSensorResponseTable.value?.totalPages) return
        if (!isNext && pageIndex - 1 < 0) return
        pageIndex = if (isNext) pageIndex + 1 else pageIndex - 1
        fetchSensorData(sort)
    }

    init {
        startUpdateRunnable()
    }

    fun search(type: TypeSearchSensor, request: String, sort: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                var param = request.toFloatOrNull()
                if (param == null) param = 0f
                val response = when (type) {
                    TypeSearchSensor.TEMP -> RetrofitInstance.sensorApi.getByTemperature(param,  sort = sort)
                    TypeSearchSensor.HUM -> RetrofitInstance.sensorApi.getByHumidity(param,  sort = sort)
                    TypeSearchSensor.LIGHT -> RetrofitInstance.sensorApi.getByLight(param, sort = sort)
                    else -> RetrofitInstance.sensorApi.getAllData(pageIndex, itemPerPage,  sort = sort)
                }
                if (_listSensorResponseTable.value != response) {
                    _listSensorResponseTable.emit(response)
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

    fun fetchSensorData(sort: Int = 1) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = RetrofitInstance.sensorApi.getAllData(pageIndex, itemPerPage,  sort = sort)
                if (_listSensorResponseTable.value != response) {
                    _listSensorResponseTable.emit(response)
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

    private suspend fun getNewestSensorResponse() {
        try {
            val response = RetrofitInstance.sensorApi.getNewestData()
            if (_newestSensorResponse.value != response) {
                updateNewestSensorResponse(response)
            }
        } catch (e: IOException) {
            // Xử lý lỗi kết nối
            //Log.d("GiangPT newest IOE", e.toString())
        } catch (e: HttpException) {
            // Xử lý lỗi HTTP
            Log.d("GiangPT 3 HTTP", e.toString())
        }
    }

    private fun updateNewestSensorResponse(response: SensorData) {
        _newestSensorResponse.postValue(response)

        val tempDataSet = _temperatureDataSet.value?.apply {
            if (entryCount >= MAX_ENTRIES) {
                removeFirst()
            }
            addEntry(
                Entry(
                    indexChart.toFloat(),
                    response.temperature.toFloat()
                )
            )
            notifyDataSetChanged()
        }

        val humDataSet = _humidityDataSet.value?.apply {
            if (entryCount >= MAX_ENTRIES) {
                removeFirst()
            }
            addEntry(
                Entry(
                    indexChart.toFloat(),
                    response.humidity.toFloat()
                )
            )
            notifyDataSetChanged()
        }

        val lightDataSet = _lightDataSet.value?.apply {
            if (entryCount >= MAX_ENTRIES) {
                removeFirst()
            }
            addEntry(
                Entry(
                    indexChart.toFloat(),
                    response.light.toFloat()
                )
            )
            notifyDataSetChanged()
        }

        indexChart++

        _humidityDataSet.postValue(humDataSet)
        _lightDataSet.postValue(lightDataSet)
        _temperatureDataSet.postValue(tempDataSet)
    }

}