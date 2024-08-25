package com.example.iot.model

class SensorResponse(
    val id: Int,
    val time: Long,
    val tempResponse: Long,
    val humResponse: Long,
    val brightResponse: Long,
)