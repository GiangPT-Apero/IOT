package com.example.iot.model

data class TempSensorResponse(
    val id: Int,
    val name: String = "Temperature",
    val time: Long,
    val result: Long,
)
