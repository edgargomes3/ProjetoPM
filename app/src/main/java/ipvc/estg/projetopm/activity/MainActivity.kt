package ipvc.estg.projetopm.activity

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.location.Location
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.google.android.gms.location.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import ipvc.estg.projetopm.R
import org.json.JSONException
import org.json.JSONObject

class MainActivity : AppCompatActivity(), SensorEventListener {
    private lateinit var mMap: GoogleMap
    private lateinit var sensorManager: SensorManager
    private lateinit var locationRequest: LocationRequest
    private lateinit var locationCallback: LocationCallback
    private lateinit var lastLocation: Location
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    private lateinit var external_temperature: TextView
    private lateinit var external_humidity: TextView
    private lateinit var internal_temperature: TextView
    private lateinit var internal_humidity: TextView
    private lateinit var probability_ice: TextView

    private var API_TEMPERATURA: Float = 0.0f
    private var API_HUMIDADE: Float = 0.0f
    private var TEMPERATURA: Float = 0.0f
    private var HUMIDADE: Float = 0.0f

    private val LOCATION_PERMISSION_REQUEST_CODE = 1



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        external_temperature = findViewById(R.id.external_temperature_value)
        external_humidity = findViewById(R.id.external_humidity_value)
        internal_temperature = findViewById(R.id.internal_temperature_value)
        internal_humidity = findViewById(R.id.internal_humidity_value)
        probability_ice = findViewById(R.id.probability_ice_value)

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
            TEMPERATURA = event.values[0]
            Log.d( "***TEMPERATURA***", "$TEMPERATURA")
        }

        if( event.sensor.type == Sensor.TYPE_RELATIVE_HUMIDITY ) {
            HUMIDADE = event.values[0]
            Log.d( "***HUMIDADE***", "$HUMIDADE")
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

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.menu_main, menu)
        return true
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menu_refresh-> {
                refresh_data()
                Log.d("***API***", "${API_TEMPERATURA}ºC, ${API_HUMIDADE}%")
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun refresh_data() {
        val url = "http://api.openweathermap.org/data/2.5/weather?lat=${lastLocation.latitude}&lon=${lastLocation.longitude}&appid=fb6baa35cf48b1197abed43584e41e39&units=metric"

        var jor = JsonObjectRequest(Request.Method.GET, url, null, { response ->
            try {
                var main = response.getJSONObject("main")
                var weather = response.getJSONArray("weather")
                var object1 = weather.getJSONObject(0)

                var temp_min = main.getDouble("temp_min")
                var temp_max = main.getDouble("temp_max")

                API_HUMIDADE = main.getDouble("humidity").toFloat()
                var temp = (temp_min + temp_max)/2
                API_TEMPERATURA = temp.toFloat()

                external_temperature.text = "${API_TEMPERATURA}ºC"
                external_humidity.text = "${API_HUMIDADE}%"
                internal_temperature.text = "${TEMPERATURA}ºC"
                internal_humidity.text = "${HUMIDADE}%"
                probability_ice.text = "0%"
            } catch (e: JSONException) {
                e.printStackTrace()
            }
        }, {
        })
        var queue = Volley.newRequestQueue(this)
        queue.add(jor)
    }
}