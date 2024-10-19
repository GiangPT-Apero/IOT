package com.example.iot.model

data class LedData(
    val id: Int,
    val ledName: String,
    val action: Boolean,
    val timeStamp: Long,
)
