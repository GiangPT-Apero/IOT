package com.example.iot.retrofit.api

import com.example.iot.model.PageResponse
import com.example.iot.model.SensorData
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface SensorDataApi {

    @GET("/sensor-data/newest")
    suspend fun getNewestData() : SensorData

    @GET("/sensor-data/temperature/{temperature}")
    suspend fun getByTemperature(
        @Path("temperature") temperature: Float,
        @Query("page") page: Int = 0,
        @Query("size") size: Int = 20
    ): PageResponse<SensorData>

    @GET("/sensor-data/humidity/{humidity}")
    suspend fun getByHumidity(
        @Path("humidity") humidity: Float,
        @Query("page") page: Int = 0,
        @Query("size") size: Int = 20
    ): PageResponse<SensorData>

    @GET("/sensor-data/light/{light}")
    suspend fun getByLight(
        @Path("light") light: Float,
        @Query("page") page: Int = 0,
        @Query("size") size: Int = 20
    ): PageResponse<SensorData>

    @GET("/sensor-data/all")
    suspend fun getAllData(
        @Query("page") page: Int = 0,
        @Query("size") size: Int = 20
    ): PageResponse<SensorData>
}


