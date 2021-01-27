package ipvc.estg.projetopm.activity

import android.app.TimePickerDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Build
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.CheckBox
import android.widget.EditText
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import ipvc.estg.projetopm.R
import java.util.*


class Definicoes : AppCompatActivity() {
    private lateinit var notifications: CheckBox
    private lateinit var notifications_api: CheckBox
    private lateinit var notifications_internal: CheckBox
    private lateinit var notifications_temperature_rate: CheckBox
    private lateinit var notifications_temperature_rate_value: EditText
    private lateinit var notifications_hour_value: EditText
    private lateinit var notifications_hour: CheckBox

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_definicoes)

        notifications = findViewById(R.id.notifications_checkbox)
        notifications_api = findViewById(R.id.notifications_api_checkbox)
        notifications_internal = findViewById(R.id.notifications_internal_checkbox)
        notifications_temperature_rate = findViewById(R.id.notifications_temperature_rate_checkbox)
        notifications_temperature_rate_value = findViewById(R.id.notifications_temperature_rate_value)
        notifications_hour_value = findViewById(R.id.notifications_hour_value)
        notifications_hour = findViewById(R.id.notifications_hour_checkbox)
        notifications_hour_value!!.showSoftInputOnFocus = false

        val sharedPref: SharedPreferences = getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE)
        val notifications_check = sharedPref.getBoolean(getString(R.string.notifications_enabled), false)
        if( notifications_check ) notifications.isChecked = true
        val notifications_api_check = sharedPref.getBoolean(getString(R.string.notifications_api_enabled), false)
        if( notifications_api_check ) notifications_api.isChecked = true
        val notifications_internal_check = sharedPref.getBoolean(getString(R.string.notifications_internal_enabled), false)
        if( notifications_internal_check ) notifications_internal.isChecked = true
        val notifications_temperature_rate_check = sharedPref.getBoolean(getString(R.string.notifications_temperature_rate_enabled), false)
        if( notifications_temperature_rate_check ) notifications_temperature_rate.isChecked = true
        val notifications_hour_check = sharedPref.getBoolean(getString(R.string.notifications_hour_enabled), false)
        if( notifications_hour_check ) notifications_hour.isChecked = true

        var notifications_temperature_rate_value_number = sharedPref.getInt(getString(R.string.notifications_temperature_rate_value), 0)
        notifications_temperature_rate_value.setText(notifications_temperature_rate_value_number.toString())

        var notifications_hour = sharedPref.getString(getString(R.string.notifications_hour), "00:00")
        notifications_hour_value.setText(notifications_hour.toString())
    }

    fun onCheckboxClicked(view: View) {
        if (view is CheckBox) {
            val checked: Boolean = view.isChecked

            val sharedPref: SharedPreferences = getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE)
            var editor = sharedPref.edit()

            when (view.id) {
                R.id.notifications_checkbox -> {
                    if (checked) {
                        editor.putBoolean(getString(R.string.notifications_enabled), true)
                    } else {
                        editor.putBoolean(getString(R.string.notifications_enabled), false)
                    }
                }
                R.id.notifications_api_checkbox -> {
                    if (checked) {
                        editor.putBoolean(getString(R.string.notifications_api_enabled), true)
                    } else {
                        editor.putBoolean(getString(R.string.notifications_api_enabled), false)
                    }
                }
                R.id.notifications_internal_checkbox -> {
                    if (checked) {
                        editor.putBoolean(getString(R.string.notifications_internal_enabled), true)
                    } else {
                        editor.putBoolean(getString(R.string.notifications_internal_enabled), false)
                    }
                }
                R.id.notifications_temperature_rate_checkbox -> {
                    if (checked) {
                        editor.putBoolean(getString(R.string.notifications_temperature_rate_enabled), true)
                        editor.putInt(getString(R.string.notifications_temperature_rate_value), notifications_temperature_rate_value.text.toString().toInt())
                    } else {
                        editor.putBoolean(getString(R.string.notifications_temperature_rate_enabled), false)
                        editor.putInt(getString(R.string.notifications_temperature_rate_value), 0)
                    }
                }
                R.id.notifications_hour_checkbox -> {
                    if (checked) {
                        editor.putBoolean(getString(R.string.notifications_hour_enabled), true)
                        editor.putString(getString(R.string.notifications_hour), notifications_hour_value.text.toString())
                    } else {
                        editor.putBoolean(getString(R.string.notifications_hour_enabled), false)
                        editor.putString(getString(R.string.notifications_hour), notifications_hour_value.text.toString())
                    }
                }
            }
            editor.commit()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.menu_definicoes, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menu_home -> {
                val sharedPref: SharedPreferences = getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE)
                var editor = sharedPref.edit()
                editor.putInt(getString(R.string.notifications_temperature_rate_value), notifications_temperature_rate_value.text.toString().toInt())
                editor.putString(getString(R.string.notifications_hour), notifications_hour_value.text.toString())
                editor.commit()

                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    fun onClick(view: View) {
        val c = Calendar.getInstance()
        val mHour = c[Calendar.HOUR_OF_DAY]
        val mMinute = c[Calendar.MINUTE]

        val timePickerDialog = TimePickerDialog(this,
                { view, hourOfDay, minute -> notifications_hour_value.setText("$hourOfDay:$minute") }, mHour, mMinute, false)
        timePickerDialog.show()
    }
}