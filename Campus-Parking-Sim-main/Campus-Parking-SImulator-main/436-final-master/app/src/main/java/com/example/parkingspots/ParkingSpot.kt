package com.example.parkingspots

import com.google.android.gms.maps.model.LatLng


//parking spot type holds information about parking spots
//its name, gps position, spots available and max spots.
class ParkingSpot {


    private var name : String = ""
    private var taken : Int = 0
    private var max : Int = 0
    private lateinit var latLng : LatLng



    constructor (name : String , taken: Int , max : Int , lat : Double, long : Double){


        this.name = name
        this.taken = taken
        this.max = max
        latLng = LatLng(lat,long)



    }

    fun getTaken() : Int {

        return taken
    }

    fun getMax() : Int {

        return max
    }

    fun getPos() : LatLng{

        return latLng


    }

    fun getName() : String {
        return name
    }



    fun getAvailable() : Int {
        return max - taken
    }
}