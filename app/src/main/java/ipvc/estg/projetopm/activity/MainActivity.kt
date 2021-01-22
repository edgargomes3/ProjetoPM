package ipvc.estg.projetopm.activity

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.location.Location
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import ipvc.estg.projetopm.R

class MainActivity : AppCompatActivity(), SensorEventListener {
    private lateinit var mMap: GoogleMap
    private lateinit var sensorManager: SensorManager
    private lateinit var locationRequest: LocationRequest
    private lateinit var locationCallback: LocationCallback
    private lateinit var lastLocation: Location
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private val LOCATION_PERMISSION_REQUEST_CODE = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        sensorManager = getSystemService(SENSOR_SERVICE) as SensorManager

        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult?) {
                locationResult ?: return
                for (location in locationResult.locations){
                    lastLocation = location
                    var loc = LatLng(lastLocation.latitude, lastLocation.longitude)
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(loc, 15.0f))
                    Log.d(
                            "****LOCATION****",
                            "new location received - " + loc.latitude + " -" + loc.longitude
                    )
                }
            }
        }

        getLocationUpdates()
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

        startLocationUpdates()
        Log.d("****LOCATION****", "onResume - startLocationUpdates")
    }

    override fun onPause() {
        super.onPause()
        sensorManager.unregisterListener(this)
        stopLocationUpdates()
        Log.d("****LOCATION****", "onPause - removeLocationUpdates")
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

    private fun getLocationUpdates()
    {

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        locationRequest = LocationRequest()
        locationRequest.interval = 5000
        locationRequest.fastestInterval = 5000
        locationRequest.smallestDisplacement = 170f // 170 m = 0.1 mile
        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY //set according to your app function
        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult?) {
                locationResult ?: return

                if (locationResult.locations.isNotEmpty()) {
                    lastLocation = locationResult.lastLocation
                    var loc = LatLng(lastLocation.latitude, lastLocation.longitude)
                    Log.d(
                            "****LOCATION****",
                            "New Location Received - " + loc.latitude + " - " + loc.longitude
                    )
                }
            }
        }
    }

    private fun startLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                    this,
                    arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
                    LOCATION_PERMISSION_REQUEST_CODE
            )
            return
        }
        fusedLocationClient.requestLocationUpdates(
                locationRequest,
                locationCallback,
                null /* Looper */
        )
    }

    private fun stopLocationUpdates() {
        fusedLocationClient.removeLocationUpdates(locationCallback)
    }
}