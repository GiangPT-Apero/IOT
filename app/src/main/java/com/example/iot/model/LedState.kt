package com.example.iot.model

data class LedState(val led1: String, val led2: String, val led3: String) {
    companion object {
        const val ON: String = "ON"
        const val OFF: String = "OFF"
    }
}