package com.example.parkingspots

class Parking {




    private var carParked : Int = 0






    fun getParked() : Int {

        return carParked
    }

    fun incrementParked() {

        carParked++
    }






    companion object {


        var lotList : ArrayList<ParkingSpot> = ArrayList<ParkingSpot>()
    }
}