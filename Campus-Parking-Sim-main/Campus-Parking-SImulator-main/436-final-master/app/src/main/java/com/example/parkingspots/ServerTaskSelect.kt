package com.example.parkingspots

import android.util.Log
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.io.InputStream
import java.net.URL
import java.util.Scanner

class ServerTaskSelect : Thread {



    private var result : String = ""
    private val select: String = //Server String

    constructor(){



    }


    override fun run() {
        super.run()
        try {
            //create url


            var url : URL = URL(select)
            //create input stream
            var iStream : InputStream = url.openStream()
            var scan : Scanner = Scanner(iStream)


            //read from input stream
            while(scan.hasNext()){
                result+= scan.nextLine()
            }
            //read






        } catch (e : Exception){

            result = e.message.toString()

            Log.w("ServerTaskSelect", "" + e.message)
        }

        Log.w("ServerTaskSelect", "$result")
        var jString = parseJson(result)

        populateParkingSpot(jString)




    }


    fun populateParkingSpot(json : String ) {

        if(json != "JSON_ERR"){

            Parking.lotList.clear()


            var jsonArray : JSONArray = JSONArray(json)


            for(i in 0..(jsonArray.length()-1)){

                var jsonObject : JSONObject = jsonArray.getJSONObject(i)

                var name : String = jsonObject.getString("name")
                var taken : Int = jsonObject.getInt("taken")
                var max : Int   = jsonObject.getInt("max")
                var lat : Double = jsonObject.getDouble("lat")
                var long : Double = jsonObject.getDouble("long")
                var tempPark : ParkingSpot = ParkingSpot(name,taken,max,lat,long)

                Parking.lotList.add(tempPark)
                Log.w("ServerTaskSelect", "${Parking.lotList}")

            }










        }

    }

    fun parseJson(json : String) :  String {

        var result : String = ""

        try {

            Log.w("ServerJson","dood var $json")


            var jsonArray : JSONArray = JSONArray(json)

            result = json

        }catch (e : JSONException) {

            result = "JSON_ERR"

        }

        Log.w("ServerTaskSelect","$result")
        return result

    }






}
