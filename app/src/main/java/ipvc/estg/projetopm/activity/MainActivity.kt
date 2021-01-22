package ipvc.estg.projetopm.activity

import android.annotation.SuppressLint
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import ipvc.estg.projetopm.R

class MainActivity : AppCompatActivity(), SensorEventListener {
    private lateinit var sensorManager: SensorManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        sensorManager = getSystemService(SENSOR_SERVICE) as SensorManager
    }

    @SuppressLint("NewApi")
    override fun onResume() {
        super.onResume()

        sensorManager.getDefaultSensor(Sensor.TYPE_AMBIENT_TEMPERATURE)?.also { ambient_temperature ->
            sensorManager.registerListener(
                    this,
                    ambient_temperature,
                    SensorManager.SENSOR_DELAY_NORMAL,
                    SensorManager.SENSOR_DELAY_UI
            )
        }

        sensorManager.getDefaultSensor(Sensor.TYPE_RELATIVE_HUMIDITY)?.also { humidity ->
            sensorManager.registerListener(
                    this,
                    humidity,
                    SensorManager.SENSOR_DELAY_NORMAL,
                    SensorManager.SENSOR_DELAY_UI
            )
        }
    }

    override fun onPause() {
        super.onPause()
        sensorManager.unregisterListener(this)
    }

    override fun onSensorChanged(event: SensorEvent) {
        if( event.sensor.type == Sensor.TYPE_AMBIENT_TEMPERATURE ) {
            Log.d( "***TEMPERATURA***", "${event.values[0]}")
        }

        if( event.sensor.type == Sensor.TYPE_RELATIVE_HUMIDITY ) {
            Log.d( "***HUMIDADE***", "${event.values[0]}")
        }
    }

    override fun onAccuracyChanged(sensor: Sensor, accuracy: Int) {
    }
}