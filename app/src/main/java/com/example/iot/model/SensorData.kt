package com.example.iot.model

data class SensorData(
    val id: Long,
    val temperature: Float,
    val humidity: Float,
    val light: Float,
    val timestamp: String
)