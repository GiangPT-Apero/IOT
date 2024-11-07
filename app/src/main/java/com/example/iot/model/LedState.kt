package com.example.iot.model

data class LedState(val led1: String, val led2: String, val led3: String, val led4: String) {
    companion object {
        const val ON = "ON"
        const val OFF = "OFF"
    }
}