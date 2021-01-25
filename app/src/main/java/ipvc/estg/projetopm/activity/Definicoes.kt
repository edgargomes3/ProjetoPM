package ipvc.estg.projetopm.activity

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.CheckBox
import ipvc.estg.projetopm.R

class Definicoes : AppCompatActivity() {
    private lateinit var notifications: CheckBox
    private lateinit var notifications_api: CheckBox
    private lateinit var notifications_internal: CheckBox
    private lateinit var notifications_temperature_rate: CheckBox

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_definicoes)

        notifications = findViewById(R.id.notifications_checkbox)
        notifications_api = findViewById(R.id.notifications_api_checkbox)
        notifications_internal = findViewById(R.id.notifications_internal_checkbox)
        notifications_temperature_rate = findViewById(R.id.notifications_temperature_rate_checkbox)

        val sharedPref: SharedPreferences = getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE)
        val notifications_check = sharedPref.getBoolean(getString(R.string.notifications_enabled), false)
        if( notifications_check ) notifications.isChecked = true
        val notifications_api_check = sharedPref.getBoolean(getString(R.string.notifications_api_enabled), false)
        if( notifications_api_check ) notifications_api.isChecked = true
        val notifications_internal_check = sharedPref.getBoolean(getString(R.string.notifications_internal_enabled), false)
        if( notifications_internal_check ) notifications_internal.isChecked = true
        val notifications_temperature_rate_check = sharedPref.getBoolean(getString(R.string.notifications_temperature_enabled), false)
        if( notifications_temperature_rate_check ) notifications_temperature_rate.isChecked = true
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
                        editor.putBoolean(getString(R.string.notifications_internal_enabled), true)
                    }
                }
                R.id.notifications_temperature_rate_checkbox -> {
                    if (checked) {
                        editor.putBoolean(getString(R.string.notifications_temperature_enabled), true)
                    }
                    else {
                        editor.putBoolean(getString(R.string.notifications_temperature_enabled), true)
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
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}