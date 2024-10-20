package com.example.iot.ui

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.iot.R
import com.example.iot.databinding.ActivityMainBinding
import com.example.iot.viewmodel.ControlViewModel
import com.example.iot.viewmodel.DeviceViewModel
import com.example.iot.viewmodel.SensorViewModel

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController

    private val controlViewModel: ControlViewModel by viewModels<ControlViewModel>()
    private val sensorViewModel: SensorViewModel by viewModels<SensorViewModel>()
    private val ledViewModel: DeviceViewModel by viewModels<DeviceViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        supportActionBar?.hide()
        binding.myNavHostFragment.post {
            navController = findNavController(R.id.my_nav_host_fragment)
            binding.bottomNavigationView.setupWithNavController(navController)

            binding.bottomNavigationView.setOnItemSelectedListener { item ->
                when (item.itemId) {
                    R.id.action_home -> {
                        navController.navigate(R.id.homeFragment)
                        true
                    }

                    R.id.action_user -> {
                        navController.navigate(R.id.userFragment)
                        true
                    }

                    R.id.action_control -> {
                        navController.navigate(R.id.controlFragment)
                        true
                    }

                    R.id.action_chart -> {
                        navController.navigate(R.id.chartFragment)
                        true
                    }

                    else -> false
                }
            }
        }
    }

}