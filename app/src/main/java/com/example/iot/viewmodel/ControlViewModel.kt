package com.example.iot.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.iot.model.LedState
import com.example.iot.retrofit.RetrofitInstance
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.HttpException
import java.io.IOException
import retrofit2.Callback
import retrofit2.Response

class ControlViewModel: ViewModel() {
    private val _stateLed = MutableStateFlow<LedState?>(null)
    val stateLed = _stateLed.asStateFlow()

    init {
        startObserverLedState()
    }

    private fun startObserverLedState() {
        viewModelScope.launch(Dispatchers.IO) {
            while (true) {
                getStateLed()
                delay(2000)
            }
        }
    }

    private suspend fun getStateLed() {
        try {
            val response = RetrofitInstance.ledApi.getLedState()
            if (_stateLed.value != response) {
                _stateLed.emit(response)
            }
        } catch (e: IOException) {
            // Xử lý lỗi kết nối
            //Log.d("GiangPT IOE", e.toString())
        } catch (e: HttpException) {
            // Xử lý lỗi HTTP
            Log.d("GiangPT 2 HTTP", e.toString())
        }
    }

    fun toggleLed(led : String) {
        val action = when (led) {
            "led1" -> _stateLed.value?.led1
            "led2" -> _stateLed.value?.led2
            "led3" -> _stateLed.value?.led3
            else -> null
        }
        if (action == null) return
        val command = if (action == LedState.ON) LedState.OFF else LedState.ON
        viewModelScope.launch(Dispatchers.IO) {
            sendActionLed(led, command)
        }
    }

    private fun sendActionLed(led: String, command: String) {
        RetrofitInstance.ledApi.toggleLed(led, command).enqueue(object : Callback<String> {
            override fun onResponse(call: Call<String>, response: Response<String>) {
                if (response.isSuccessful) {
                    Log.d("MainActivity", "LED toggled successfully: ${response.body()}")
                } else {
                    Log.e("MainActivity", "Error toggling LED: ${response.errorBody()?.string()}")
                }
            }

            override fun onFailure(call: Call<String>, t: Throwable) {
                Log.e("MainActivity", "Failed to toggle LED: ${t.message}")
            }
        })
    }
}