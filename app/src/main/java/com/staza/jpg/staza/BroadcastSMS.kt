package com.staza.jpg.staza

import android.annotation.SuppressLint
import android.widget.Toast
import android.content.Intent
import android.content.BroadcastReceiver
import android.content.Context
import android.preference.PreferenceManager
import android.telephony.SmsMessage
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.support.v4.content.ContextCompat
import android.telephony.SmsManager
import android.util.Log
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.tasks.OnSuccessListener
import com.google.android.gms.tasks.Task
import android.support.annotation.NonNull
import com.google.android.gms.location.LocationServices.getFusedLocationProviderClient
import com.google.android.gms.tasks.OnFailureListener







/**
* Created by chinmay on 07-Jan-18.
*/

class SmsListener : BroadcastReceiver() {
    private var mFusedLocationClient: FusedLocationProviderClient? = null

    @SuppressLint("MissingPermission")
    override fun onReceive(context: Context, intent: Intent) {

        val bundle = intent.extras
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(context)
        mFusedLocationClient!!.lastLocation.addOnSuccessListener({
            // Task completed successfully
            // ...
            location->
            // Got last known location. In some rare situations this can be null.
            if (location != null) {
                // Logic to handle location object
                val currentLatitude = location.latitude
                val currentLongitude = location.longitude
                Log.d("yo", "$currentLatitude, $currentLongitude")
                Toast.makeText(context, "$currentLatitude, $currentLongitude", Toast.LENGTH_SHORT).show()
            }
        })



        Toast.makeText(context, "Message Received", Toast.LENGTH_SHORT).show()

        if (bundle != null) {
            //---get the SMS message passed in---
            val msgArray: Array<SmsMessage?>?
            var msgFrom = ""
            var msgBody = ""
            try {
                val pdus = bundle.get("pdus") as Array<*>
                val settings = PreferenceManager.getDefaultSharedPreferences(context)
                msgArray = arrayOfNulls(size = pdus.size)
                for (i in msgArray.indices) {
                    msgArray[i] = SmsMessage.createFromPdu(pdus[i] as ByteArray)
                    msgFrom = msgArray[i]!!.originatingAddress
                    msgBody += msgArray[i]!!.messageBody
                }

                Toast.makeText(context, "Staza: " + msgBody, Toast.LENGTH_SHORT).show()

                //Get Current Location and Send back SMS
                if(msgBody == settings.getString("key", "")){

                    mFusedLocationClient!!.lastLocation.addOnSuccessListener({
                        // Task completed successfully
                        // ...
                        location->
                        // Got last known location. In some rare situations this can be null.
                        //val sm1 = SmsManager.getDefault()
                        //sm1.sendTextMessage(msgFrom, null, "yo baby", null, null)
                        if (location != null) {
                            // Logic to handle location object
                            val currentLatitude = location.latitude
                            val currentLongitude = location.longitude
                            val msg = "Your Phone is Located at : ( $currentLatitude, $currentLongitude )"
                            val sm = SmsManager.getDefault()
                            sm.sendTextMessage(msgFrom, null, msg, null, null)
                            Log.d("yo", "$currentLatitude, $currentLongitude")
                            Toast.makeText(context, "$currentLatitude, $currentLongitude", Toast.LENGTH_SHORT).show()
                        }
                    })

                        //val msg = "Your Phone is Located at : ( $currentLatitude, $currentLongitude )"
                        //val sm = SmsManager.getDefault()
                        //sm.sendTextMessage(msgFrom, null, msg, null, null)
                    Log.d("yo", "matched")
                }
            } catch (e: kotlin.Exception) {
                //Log.d("Exception caught",e.getMessage());
            }
        }
    }
}