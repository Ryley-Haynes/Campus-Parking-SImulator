package com.example.parkingspots

import android.os.Bundle
import android.view.View.VISIBLE
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.maps.CameraUpdate
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.VisibleRegion
import java.util.Timer

class MapActivity : AppCompatActivity(), OnMapReadyCallback {


    private lateinit var map : GoogleMap


    private lateinit var addButton : Button
    private lateinit var  removeButton : Button

    private lateinit var backButton : Button

    private lateinit var lotName : TextView

    private lateinit var dispTaken : TextView

    private var currentSpot : ParkingSpot? = null

    private lateinit var dispMax : TextView
    private lateinit var progressBar: ProgressBar

    private var park : Parking = MainActivity.park




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContentView(R.layout.activity_map)


        var mapFragment : SupportMapFragment = supportFragmentManager.findFragmentById(R.id.mapFragment) as SupportMapFragment

        mapFragment.getMapAsync (this)


        addButton = findViewById<Button>(R.id.addCar)
        addButton.setOnClickListener { addClick() }

        removeButton = findViewById<Button>(R.id.removeCar)
        removeButton.setOnClickListener { removeClick() }
        lotName = findViewById<TextView>(R.id.lotName)


        dispTaken = findViewById<TextView>(R.id.takenSpots)
        dispMax = findViewById<TextView>(R.id.maxSpots)
        progressBar = findViewById<ProgressBar>(R.id.progressBar)

        backButton = findViewById<Button>(R.id.mapBack)
        backButton.setOnClickListener{goBack()}


        var timerTask : UpdateTimerTask = UpdateTimerTask(this)

        var timer : Timer = Timer()

        timer.schedule(timerTask,4000,4000)








    }

    override fun onMapReady(p0: GoogleMap) {

        map= p0
        var umd : LatLng =
            LatLng( 38.9889183, -76.9360543 )

        val update : CameraUpdate =
            CameraUpdateFactory.newLatLngZoom( umd, 12.0f )
        map.moveCamera( update )



        addParkingSpots()

        map.setOnMarkerClickListener (markerListener())

    }



    fun addClick() {

        if(currentSpot != null && currentSpot!!.getTaken() +1 < currentSpot!!.getMax()) {

            var task: ServerTaskUpdate =
                ServerTaskUpdate(currentSpot!!.getName(), "taken", currentSpot!!.getTaken() + 1)

            task.start()
            task.join()

            var rePop: ServerTaskSelect = ServerTaskSelect()

            rePop.start()
            rePop.join()


            map.clear()
            addParkingSpots()

            dispTaken.text = (dispTaken.text.toString().toInt() + 1).toString()

            for (spot in Parking.lotList) {

                if (lotName.text == spot.getName()) {
                    currentSpot = spot


                }

            }



            addButton.visibility = VISIBLE
            removeButton.visibility = VISIBLE

            lotName.text = currentSpot!!.getName()

            dispTaken.text = currentSpot!!.getTaken().toString()

            dispMax.text = currentSpot!!.getMax().toString()

            progressBar.max = currentSpot!!.getMax()

            progressBar.setProgress(currentSpot!!.getTaken(), true)

            park.incrementParked()


        }else {

            var toast : Toast = Toast.makeText(this,"No Selection",Toast.LENGTH_SHORT)

            toast.show()
        }
    }

    fun removeClick() {




        if(currentSpot != null && currentSpot!!.getTaken() > 0) {

            var task: ServerTaskUpdate =
                ServerTaskUpdate(currentSpot!!.getName(), "taken", currentSpot!!.getTaken() - 1)

            task.start()
            task.join()

            var rePop: ServerTaskSelect = ServerTaskSelect()

            rePop.start()
            rePop.join()


            map.clear()
            addParkingSpots()

            dispTaken.text = (dispTaken.text.toString().toInt() -1 ).toString()

            for (spot in Parking.lotList) {

                if (lotName.text == spot.getName()) {
                    currentSpot = spot


                }

            }



            addButton.visibility = VISIBLE
            removeButton.visibility = VISIBLE

            lotName.text = currentSpot!!.getName()

            dispTaken.text = currentSpot!!.getTaken().toString()

            dispMax.text = currentSpot!!.getMax().toString()

            progressBar.max = currentSpot!!.getMax()

            progressBar.setProgress(currentSpot!!.getTaken(), true)


        }else {

            var toast : Toast = Toast.makeText(this,"No Selection",Toast.LENGTH_SHORT)

            toast.show()
        }
















    }

    fun goBack() {

        finish()
    }


    fun refresh (){

        var tempTask = ServerTaskSelect()
        tempTask.start()
        tempTask.join()

        //map.clear()

        //addParkingSpots()
        if(currentSpot != null) {
            for (spot in Parking.lotList) {
                if (currentSpot!!.getName() == spot.getName()){
                    currentSpot = spot
                }
            }


            updateUi(currentSpot!!)





        }



    }

    fun updateUi(spot : ParkingSpot){

        lotName.text = spot.getName()

        dispTaken.text = spot.getTaken().toString()

        dispMax.text = spot.getMax().toString()

        progressBar.max = spot.getMax()

        progressBar.setProgress(spot.getTaken(),true)


    }

    fun addParkingSpots() {




        for (spot in Parking.lotList){
            var tempMarker = MarkerOptions().position(spot.getPos()).title(spot.getName())

            map.addMarker(tempMarker)

        }





    }

    inner class markerListener : GoogleMap.OnMarkerClickListener {
        override fun onMarkerClick(p0: Marker): Boolean {


            var tempSpot : ParkingSpot? = null
            var foundMarker = false
            for (spot in Parking.lotList) {

                if (p0.title == spot.getName()) {
                    tempSpot = spot

                    foundMarker = true
                    currentSpot = spot
                }

            }

            if(foundMarker){

                var useSpot = tempSpot!!

                addButton.visibility = VISIBLE
                removeButton.visibility = VISIBLE

                updateUi(tempSpot)



















            }

            return true
        }

    }
}