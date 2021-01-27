package ipvc.estg.projetopm.activity

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.CheckBox
import android.widget.EditText
import ipvc.estg.projetopm.R

class Definicoes : AppCompatActivity() {
    private lateinit var notifications: CheckBox
    private lateinit var notifications_api: CheckBox
    private lateinit var notifications_internal: CheckBox
    private lateinit var notifications_temperature_rate: CheckBox
    private lateinit var notifications_temperature_rate_value: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_definicoes)

        notifications = findViewById(R.id.notifications_checkbox)
        notifications_api = findViewById(R.id.notifications_api_checkbox)
        notifications_internal = findViewById(R.id.notifications_internal_checkbox)
        notifications_temperature_rate = findViewById(R.id.notifications_temperature_rate_checkbox)
        notifications_temperature_rate_value = findViewById(R.id.notifications_temperature_rate_value)

        val sharedPref: SharedPreferences = getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE)
        val notifications_check = sharedPref.getBoolean(getString(R.string.notifications_enabled), false)
        if( notifications_check ) notifications.isChecked = true
        val notifications_api_check = sharedPref.getBoolean(getString(R.string.notifications_api_enabled), false)
        if( notifications_api_check ) notifications_api.isChecked = true
        val notifications_internal_check = sharedPref.getBoolean(getString(R.string.notifications_internal_enabled), false)
        if( notifications_internal_check ) notifications_internal.isChecked = true
        val notifications_temperature_rate_check = sharedPref.getBoolean(getString(R.string.notifications_temperature_rate_enabled), false)
        if( notifications_temperature_rate_check ) notifications_temperature_rate.isChecked = true

        var notifications_temperature_rate_value_number = sharedPref.getInt(getString(R.string.notifications_temperature_rate_value), 0)
        notifications_temperature_rate_value.setText( notifications_temperature_rate_value_number.toString() )
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
                    }
                    else {
                        editor.putBoolean(getString(R.string.notifications_enabled), false)
                    }
                }
                R.id.notifications_api_checkbox -> {
                    if (checked) {
                        editor.putBoolean(getString(R.string.notifications_api_enabled), true)
                    }
                    else {
                        editor.putBoolean(getString(R.string.notifications_api_enabled), false)
                    }
                }
                R.id.notifications_internal_checkbox -> {
                    if (checked) {
                        editor.putBoolean(getString(R.string.notifications_internal_enabled), true)
                    }
                    else {
                        editor.putBoolean(getString(R.string.notifications_internal_enabled), false)
                    }
                }
                R.id.notifications_temperature_rate_checkbox -> {
                    if (checked) {
                        editor.putBoolean(getString(R.string.notifications_temperature_rate_enabled), true)
                        editor.putInt(getString(R.string.notifications_temperature_rate_value), notifications_temperature_rate_value.text.toString().toInt())
                    }
                    else {
                        editor.putBoolean(getString(R.string.notifications_temperature_rate_enabled), false)
                        editor.putInt(getString(R.string.notifications_temperature_rate_value), 0)
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
            R.id.menu_home-> {
                val sharedPref: SharedPreferences = getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE)
                var editor = sharedPref.edit()
                editor.putInt(getString(R.string.notifications_temperature_rate_value), notifications_temperature_rate_value.text.toString().toInt())
                editor.commit()

                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}