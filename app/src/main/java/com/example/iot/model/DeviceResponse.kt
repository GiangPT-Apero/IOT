package com.example.iot.model

data class DeviceResponse(
    val id: Int,
    val name: String,
    val action: Boolean,
    val time: Long,
)
