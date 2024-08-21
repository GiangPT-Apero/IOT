package com.example.iot.model

data class BrightSensorResponse(
    val id: Int,
    val name: String = "Brightness",
    val time: Long,
    val result: Long,
)
