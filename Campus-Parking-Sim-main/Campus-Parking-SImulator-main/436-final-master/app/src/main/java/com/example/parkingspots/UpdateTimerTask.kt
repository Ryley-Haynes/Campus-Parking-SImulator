package com.example.parkingspots

import android.util.Log
import java.util.TimerTask

class UpdateTimerTask : TimerTask {


    private var activity : MapActivity
    constructor(activity :  MapActivity) {

        this.activity = activity

    }


    override fun run() {
        //update model and update view

        Log.w("UpdateTimerTask","run task")

        activity.runOnUiThread {    activity.refresh()}

    }


}