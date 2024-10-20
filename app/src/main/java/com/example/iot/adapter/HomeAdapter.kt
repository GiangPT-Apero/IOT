package com.example.iot.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.iot.ui.fragment.home.brightness.BrightnessFragment
import com.example.iot.ui.fragment.home.humidity.HumidityFragment
import com.example.iot.ui.fragment.home.temperature.TemperatureFragment

class HomeAdapter(activity: FragmentActivity) : FragmentStateAdapter(activity) {

    override fun getItemCount(): Int {
        return 3 // Số lượng fragment
    }

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> TemperatureFragment()
            1 -> HumidityFragment()
            2 -> BrightnessFragment()
            else -> TemperatureFragment()
        }
    }
}