package com.example.iot.retrofit

import com.example.iot.retrofit.api.LedApi
import com.example.iot.retrofit.api.SensorDataApi
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {
    private const val BASE_URL = "http://192.168.10.133:8080/"

    val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val sensorApi: SensorDataApi by lazy {
        retrofit.create(SensorDataApi::class.java)
    }

    val ledApi: LedApi by lazy {
        retrofit.create(LedApi::class.java)
    }
}
