package com.example.parkingspots

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextClock
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
import androidx.core.graphics.toColorInt

class MainActivity : AppCompatActivity() {

    private lateinit var startButton: Button
    private lateinit var optionsButton: Button
    private var hasShownNotification = false
    private lateinit var availabilityText: TextView

    private lateinit var countClick : TextView

    private lateinit var timeClock: TextClock
    private lateinit var timeMessage: TextView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        changeTheme()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        MobileAds.initialize(this) {}
        val adView = findViewById<AdView>(R.id.adView)
        val adRequest = AdRequest.Builder().build()
        adView.loadAd(adRequest)

        timeClock = findViewById(R.id.timeClock)
        timeMessage = findViewById(R.id.timeMessage)

        startButton = findViewById<Button>(R.id.startToMap)

        optionsButton = findViewById<Button>(R.id.startToOption)
        countClick = findViewById<Button>(R.id.count)

        availabilityText = findViewById<TextView>(R.id.availabilityText)

        optionsButton.setOnClickListener { _ -> goToOptions() }

        startButton.setOnClickListener { _ -> goToMap() }

        countClick.text = MainActivity.park.getParked().toString()


        var task : ServerTaskSelect = ServerTaskSelect()

        task.start()
        task.join()

        updateAvailability()
        Log.w("MainActivity",Parking.lotList.toString())


    }



    override fun onResume() {
        super.onResume()
        changeTheme()
        updateTimeMessage()
        if (!hasShownNotification) {
            showNotification()
            hasShownNotification = true
        }

        countClick.text = MainActivity.park.getParked().toString()
    }



    fun goToMap(){
        var newIntent : Intent = Intent(this, MapActivity::class.java)

        startActivity(newIntent)

        hasShownNotification = false

    }

    fun goToOptions(){
        var newIntent : Intent = Intent(this, OptionActivity::class.java)
        startActivity(newIntent)

        hasShownNotification = false
    }
    fun changeTheme(){
        var pref : SharedPreferences = this.getSharedPreferences(this.packageName + "_preferences",
            Context.MODE_PRIVATE)



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

    fun showNotification() {
        val pref: SharedPreferences = getSharedPreferences(packageName + "_preferences", Context.MODE_PRIVATE)
        val enabled = pref.getBoolean("notificationsEnabled", true)

        if(enabled) {
            var totalAvailable = 0
            for(spot in Parking.lotList) {
                totalAvailable += spot.getAvailable()
            }

            Toast.makeText(this, "$totalAvailable parking spots available!", Toast.LENGTH_SHORT).show()
        }
    }

    fun updateAvailability() {
        var available = 0
        var spots = 0

        for(spot in Parking.lotList) {
            available += spot.getAvailable()
            spots += spot.getMax()
        }

        var percentTaken = 0.0
        if (spots > 0) {
            percentTaken = ((spots - available).toDouble() / spots.toDouble()) * 100
        }

        availabilityText.text = "$available spots available"

        when {
            percentTaken >= 75 -> {
                availabilityText.setTextColor(Color.RED)
            }

            percentTaken >= 50 -> {
                availabilityText.setTextColor("#FFA500".toColorInt())
            }

            else -> {
                availabilityText.setTextColor(Color.GREEN)
            }
        }
    }

    fun updateTimeMessage() {
        val tzone = java.time.ZoneId.of("America/New_York")
        val curTime = java.time.ZonedDateTime.now(tzone)
        val curHour = curTime.hour
        val message = when (curHour) {
            in 7..9 -> "Morning commute; greater parking activity expected."
            in 10..15 -> "Midday; fewer spaces may be available."
            in 16..18 -> "Evening commute; more spaces will start becoming available."
            else -> "Overnight; most spaces will be free."
        }    
        timeMessage.text = message
    }

    companion object {



        var park = Parking()
    }




}
