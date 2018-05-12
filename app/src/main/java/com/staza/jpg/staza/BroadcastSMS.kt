package com.staza.jpg.staza

import android.Manifest
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
import android.os.Handler
import android.support.v4.content.ContextCompat
import android.telephony.SmsManager
import android.util.Log
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.tasks.OnSuccessListener
import com.google.android.gms.tasks.Task
import android.support.annotation.NonNull
import android.support.v4.content.ContextCompat.startActivity
import com.google.android.gms.location.LocationServices.getFusedLocationProviderClient
import com.google.android.gms.tasks.OnFailureListener

class SmsListener : BroadcastReceiver() {
    private var mFusedLocationClient: FusedLocationProviderClient? = null


    override fun onReceive(context: Context, intent: Intent) {
        val i = Intent()
        i.setClass(context, location_setting::class.java!!)
        i.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        val bundle = intent.extras

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
                    startActivity(context, i, bundle)
                    val handler = Handler()
                    handler.postDelayed({
                        // Actions to do after 10 seconds

                        Toast.makeText(context, "sjdks", Toast.LENGTH_SHORT).show()
                        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(context)
                        if (ContextCompat.checkSelfPermission(context,
                                        Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)

                        mFusedLocationClient!!.lastLocation.addOnSuccessListener({

                        location->
                        // Got last known location. In some rare situations this can be null.

                        if (location != null) {
                            // Logic to handle location object
                            val currentLatitude = location.latitude
                            val currentLongitude = location.longitude
                            val msg = "Your Phone is Located at : ( $currentLatitude, $currentLongitude )"
                            val sm = SmsManager.getDefault()
                            sm.sendTextMessage(msgFrom, null, msg, null, null)
                            Log.d("Location Coordinates", "$currentLatitude, $currentLongitude")
                            Toast.makeText(context, "$currentLatitude, $currentLongitude", Toast.LENGTH_SHORT).show()
                        }
                    })
                    }, 100000)
                }
            } catch (e: kotlin.Exception) {
                //Log.d("Exception caught",e.getMessage());
            }
        }
    }
}