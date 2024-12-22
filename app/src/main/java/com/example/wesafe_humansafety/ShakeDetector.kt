package com.example.wesafe_humansafety

import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager

class ShakeDetector(private val onShakeListener: OnShakeListener) : SensorEventListener {

    interface OnShakeListener {
        fun onShake()
    }

    private var lastShakeTime: Long = 0

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}

    override fun onSensorChanged(event: SensorEvent?) {
        if (event?.sensor?.type == Sensor.TYPE_ACCELEROMETER) {
            val x = event.values[0]
            val y = event.values[1]
            val z = event.values[2]

            val gX = x / SensorManager.GRAVITY_EARTH
            val gY = y / SensorManager.GRAVITY_EARTH
            val gZ = z / SensorManager.GRAVITY_EARTH

            val gForce = Math.sqrt((gX * gX + gY * gY + gZ * gZ).toDouble())

            if (gForce > SHAKE_THRESHOLD_GRAVITY) {
                val currentTime = System.currentTimeMillis()
                if (currentTime - lastShakeTime > SHAKE_SLOP_TIME_MS) {
                    lastShakeTime = currentTime
                    onShakeListener.onShake()
                }
            }
        }
    }

    companion object {
        private const val SHAKE_THRESHOLD_GRAVITY = 2.7f
        private const val SHAKE_SLOP_TIME_MS = 500
    }
}