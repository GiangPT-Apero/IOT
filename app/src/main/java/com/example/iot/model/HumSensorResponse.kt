package com.example.iot.model

data class HumSensorResponse(
    val id: Int,
    val name: String = "Humidity",
    val time: Long,
    val result: Long,
)
