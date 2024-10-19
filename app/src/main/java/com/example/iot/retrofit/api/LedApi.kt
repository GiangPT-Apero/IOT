package com.example.iot.retrofit.api

import com.example.iot.model.LedData
import com.example.iot.model.LedState
import com.example.iot.model.PageResponse
import com.example.iot.model.SensorData
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface LedApi {
    @POST("/led/toggle/{ledId}")
    fun toggleLed(
        @Path("ledId") ledId: String,
        @Body command: String
    ): Call<String>

    @GET("/led/data")
    suspend fun getAllData(
        @Query("page") page: Int = 0,
        @Query("size") size: Int = 10
    ): PageResponse<LedData>

    @GET("/led/state")
    suspend fun getLedState(): LedState
}
