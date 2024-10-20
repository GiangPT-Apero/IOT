package com.example.iot.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.iot.databinding.DeviceItemLayoutBinding
import com.example.iot.databinding.SensorItemLayoutBinding
import com.example.iot.model.LedData
import com.example.iot.model.SensorData

class TableAdapter() : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val SENSOR_TYPE = 0
    private val DEVICE_TYPE = 1
    private var currentType = SENSOR_TYPE
    private var listSensor = ArrayList<SensorData>()
    private var listDevice = ArrayList<LedData>()

    fun setListSensor(listSensor: ArrayList<SensorData>) {
        this.listSensor = listSensor
    }

    fun setListDevice(listDevice: ArrayList<LedData>) {
        this.listDevice = listDevice
    }

    fun setTypeToDevice(isDevice: Boolean) {
        currentType = if (isDevice) {
            DEVICE_TYPE
        } else {
            SENSOR_TYPE
        }
        notifyDataSetChanged()
    }

    fun isDeviceType(): Boolean {
        return currentType == DEVICE_TYPE
    }

    override fun getItemViewType(position: Int): Int {
        return currentType
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        when (viewType) {
            SENSOR_TYPE -> {
                val binding = SensorItemLayoutBinding.inflate(inflater, parent, false)
                return SensorViewHolder(binding.root)
            }

            else -> {
                val binding = DeviceItemLayoutBinding.inflate(inflater, parent, false)
                return DeviceViewHolder(binding.root)
            }
        }
    }

    override fun getItemCount(): Int {
        return if (currentType == SENSOR_TYPE) {
            listSensor.size
        } else {
            listDevice.size
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (currentType == SENSOR_TYPE) {
            val sensorViewHolder = holder as SensorViewHolder
            val sensor = listSensor[position]
            sensorViewHolder.onBind(sensor)
        } else {
            val deviceViewHolder = holder as DeviceViewHolder
            val device = listDevice[position]
            deviceViewHolder.onBind(device)

        }

    }

    class SensorViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {
        private val binding = SensorItemLayoutBinding.bind(itemView)

        fun onBind(sensor: SensorData) {
            binding.txtId.text = sensor.id.toString()
            binding.txtTemperature.text = sensor.temperature.toString()
            binding.txtHumidity.text = sensor.humidity.toString()
            binding.txtBrightness.text = sensor.light.toString()
            binding.txtTime.text = sensor.timestamp
        }

    }

    class DeviceViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {
        private val binding = DeviceItemLayoutBinding.bind(itemView)

        fun onBind(device: LedData) {
            binding.txtId.text = device.id.toString()
            binding.txtName.text = device.ledName
            binding.txtAction.text = device.action.toString()
            binding.txtTime.text = device.timeStamp.toString()
        }
    }
}