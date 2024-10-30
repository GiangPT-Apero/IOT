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
        @Query("size") size: Int = 20,
        @Query("sort") sort: Int = 1,
    ): PageResponse<LedData>

    @GET("/led/state")
    suspend fun getLedState(): LedState

    @GET("/led/ledName/{ledName}")
    suspend fun getByLedName(
        @Path("ledName") ledName: String,
        @Query("page") page: Int = 0,
        @Query("size") size: Int = 20000,
        @Query("sort") sort: Int = 1,
    ): PageResponse<LedData>

    @GET("/led/action/{action}")
    suspend fun getByAction(
        @Path("action") action: String,
        @Query("page") page: Int = 0,
        @Query("size") size: Int = 20000,
        @Query("sort") sort: Int = 1,
    ): PageResponse<LedData>

    @GET("/led/time")
    suspend fun getByTimeStamp(
        @Query("timestamp") timestamp: String,
        @Query("page") page: Int = 0,
        @Query("size") size: Int = 20000,
        @Query("sort") sort: Int = 1,
    ): PageResponse<LedData>
}
