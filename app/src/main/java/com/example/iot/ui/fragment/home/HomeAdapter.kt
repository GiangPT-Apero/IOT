package com.example.iot.ui.fragment.home

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

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