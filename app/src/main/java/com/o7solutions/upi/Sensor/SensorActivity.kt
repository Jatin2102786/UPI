package com.o7solutions.upi.Sensor

import android.content.Context
import android.content.pm.PackageManager
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.hardware.camera2.CameraManager
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.o7solutions.upi.R
import kotlin.math.sqrt

class SensorActivity : AppCompatActivity(), SensorEventListener {

//    accessing android hardware's sensor capability
    lateinit var sensorMananger: SensorManager

//    Sensor is accelerometer
    lateinit var accMeter: Sensor

    var isFlashOn = false

//    accessing camera manager
    lateinit var camMng: CameraManager

    var camId: String ?= null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_sensor)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

//        Checking if camera permission is granted or not
        if(ContextCompat.checkSelfPermission(this,android.Manifest.permission.CAMERA) !=
            PackageManager.PERMISSION_GRANTED
        ) {
//            requesting permission if not granted
            ActivityCompat.requestPermissions(this,arrayOf(android.Manifest.permission.CAMERA),1)
        } else {

//            initializing sensor manager
            sensorMananger = getSystemService(Context.SENSOR_SERVICE) as SensorManager


//            initializing accelerometer
            accMeter = sensorMananger.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)!!

//            accessing camera manager service
            camMng = getSystemService(Context.CAMERA_SERVICE) as  CameraManager
            camId = camMng.cameraIdList[0]

        }
    }

    override fun onResume() {
        super.onResume()

        accMeter.also { accel->
            sensorMananger.registerListener(this,accel, SensorManager.SENSOR_DELAY_NORMAL)
        }
    }

    override fun onSensorChanged(event: SensorEvent?) {

        val x = event?.values[0] ?: 0f
        val y = event?.values[1] ?: 0f
        val z = event?.values[2] ?: 0f

        val movement = sqrt((x * x + y * y + z * z))

        if (movement > 15) {
//            toggling flash
            toggleFlash()
        }
    }

    private fun toggleFlash() {

        isFlashOn = !isFlashOn
        camMng.setTorchMode(camId!!,isFlashOn,)
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
    }
}