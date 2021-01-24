package ipvc.estg.projetopm.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.CheckBox
import ipvc.estg.projetopm.R

class Definicoes : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_definicoes)
    }

    fun onCheckboxClicked(view: View) {
        if (view is CheckBox) {
            val checked: Boolean = view.isChecked

            when (view.id) {
                R.id.notifications_checkbox -> {
                    if (checked) {
                    }
                    else {
                    }
                }
                R.id.notifications_api_checkbox -> {
                    if (checked) {
                    }
                    else {
                    }
                }
                R.id.notifications_internal_checkbox -> {
                    if (checked) {
                    }
                    else {
                    }
                }
                R.id.notifications_temperature_rate_checkbox -> {
                    if (checked) {
                    }
                    else {
                    }
                }
            }
        }
    }
}