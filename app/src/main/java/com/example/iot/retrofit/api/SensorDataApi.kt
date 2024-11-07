package com.example.iot.retrofit.api

import com.example.iot.model.PageResponse
import com.example.iot.model.RandomData
import com.example.iot.model.SensorData
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import java.sql.Timestamp

interface SensorDataApi {

    @GET("/sensor-data/newest")
    suspend fun getNewestData() : SensorData

    @GET("/sensor-data/random/newest")
    suspend fun getNewestRandomData() : RandomData

    @GET("/sensor-data/temperature/{temperature}")
    suspend fun getByTemperature(
        @Path("temperature") temperature: Float,
        @Query("page") page: Int = 0,
        @Query("size") size: Int = 20000,
        @Query("sort") sort: Int = 1,
    ): PageResponse<SensorData>

    @GET("/sensor-data/humidity/{humidity}")
    suspend fun getByHumidity(
        @Path("humidity") humidity: Float,
        @Query("page") page: Int = 0,
        @Query("size") size: Int = 20000,
        @Query("sort") sort: Int = 1,
    ): PageResponse<SensorData>

    @GET("/sensor-data/light/{light}")
    suspend fun getByLight(
        @Path("light") light: Float,
        @Query("page") page: Int = 0,
        @Query("size") size: Int = 20000,
        @Query("sort") sort: Int = 1,
    ): PageResponse<SensorData>

    @GET("/sensor-data/time")
    suspend fun getByTimeStamp(
        @Query("timestamp") timestamp: String,
        @Query("page") page: Int = 0,
        @Query("size") size: Int = 20000,
        @Query("sort") sort: Int = 1,
    ) : PageResponse<SensorData>

    @GET("/sensor-data/all")
    suspend fun getAllData(
        @Query("page") page: Int = 0,
        @Query("size") size: Int = 20,
        @Query("sort") sort: Int = 1,
        @Query("params") params: String = "id",
    ): PageResponse<SensorData>

}


