package com.example.parkingspots

import android.util.Log
import org.json.JSONArray
import org.json.JSONException
import java.io.InputStream
import java.net.URL
import java.util.Scanner

class ServerTaskUpdate : Thread {


    private lateinit var activity: MainActivity

    private  var result: String = ""

    private lateinit var dataString: String

    private val update: String = //Server String

    private lateinit var data: Any

    private lateinit var name: String
    private lateinit var field: String


    constructor( name: String, field: String, data: Any) {


        this.name = name
        this.field = field
        this.data = data


    }


    fun setDataString(d: String) {

        dataString = d
    }

    override fun run() {

        super.run()





        try {


            //create url

            var dataP = "name=$name&field=$field&data=$data"
            var url: URL = URL(update + "?$dataP")


            var iStream: InputStream = url.openStream()
            var scan: Scanner = Scanner(iStream)

            //read from input stream
            while (scan.hasNext()) {
                result += scan.nextLine()
            }

            Log.w("ServerTaskUpdate",dataP)


        } catch (e: Exception) {


            result = e.message.toString()
        }



        Log.w("ServerTaskUpdate",result)


    }

    fun parseJson(json: String): Array<String>? {

        var result: Array<String>? = null


        try {

            var jsonArray: JSONArray = JSONArray(json)

            result = Array<String>(jsonArray.length()) { i -> jsonArray.getString(i) }


        } catch (e: JSONException) {

            result = arrayOf(e.toString())
        }

        return result
    }
}
