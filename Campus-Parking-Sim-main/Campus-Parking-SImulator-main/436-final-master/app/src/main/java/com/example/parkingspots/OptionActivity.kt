package com.example.parkingspots

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Button
import android.widget.CompoundButton
import android.widget.Switch
import android.widget.Toast
import android.widget.ToggleButton
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.SwitchCompat
import com.google.android.material.button.MaterialButton
import androidx.core.content.edit

class OptionActivity : AppCompatActivity() {

    private lateinit var toggle : ToggleButton

    private lateinit var notificationSwitch : Switch

    private lateinit var pref : SharedPreferences
    private lateinit var backButton : Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContentView(R.layout.activity_option)

        toggle = findViewById<ToggleButton>(R.id.theme_switch)


        pref  = getSharedPreferences(packageName + "_preferences", Context.MODE_PRIVATE)
        toggle.isChecked = pref.getBoolean("isDark", false)
        toggle.setOnCheckedChangeListener(checkListener())
        notificationSwitch = findViewById<Switch>(R.id.notification_switch)
        notificationSwitch.isChecked = pref.getBoolean("notificationsEnabled", true)
        notificationSwitch.setOnCheckedChangeListener(notificationCheckListener())

        backButton = findViewById<Button>(R.id.option_back)
        backButton.setOnClickListener { goBack() }
        changeTheme()


    }

    override fun onResume(){
        super.onResume()
        changeTheme()
    }



    fun changeTheme(){



        var isDark = pref.getBoolean("isDark",false)


        val newMode = if(isDark){
            AppCompatDelegate.MODE_NIGHT_YES
        }else{
            AppCompatDelegate.MODE_NIGHT_NO
        }


        if(AppCompatDelegate.getDefaultNightMode() != newMode ) {
            AppCompatDelegate.setDefaultNightMode(
                newMode


            )
            recreate()
        }
    }


    fun goBack(){
        finish()

    }



    inner class checkListener : CompoundButton.OnCheckedChangeListener  {

        override fun onCheckedChanged(
            buttonView: CompoundButton,
            isChecked: Boolean
        ) {


            pref.edit { putBoolean("isDark", isChecked) }




            val newMode = if(isChecked){
                AppCompatDelegate.MODE_NIGHT_YES
            }else{
                AppCompatDelegate.MODE_NIGHT_NO
            }


            if(AppCompatDelegate.getDefaultNightMode() != newMode ) {
                AppCompatDelegate.setDefaultNightMode(
                    newMode


                )
                recreate()
            }







        }

    }

    inner class notificationCheckListener : CompoundButton.OnCheckedChangeListener {
        override fun onCheckedChanged(buttonView: CompoundButton, isChecked: Boolean) {
            pref.edit { putBoolean("notificationsEnabled", isChecked) }
            val message = if(isChecked)
                "Notifications ON, you will receive messages about available spots"
            else
                "Notifications OFF"
            Toast.makeText(this@OptionActivity, message, Toast.LENGTH_SHORT).show()
        }
    }







}