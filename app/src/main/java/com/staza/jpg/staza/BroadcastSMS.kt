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

/**
* Created by chinmay on 07-Jan-18.
*/

class SmsListener : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {

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

                @SuppressLint("MissingPermission")
                val locationProvider = LocationManager.GPS_PROVIDER
                val manager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager

                Toast.makeText(context, msgBody, Toast.LENGTH_SHORT).show()

                //Get Current Location and Send back SMS
                if(msgBody == settings.getString("key", ""))
                    if (manager.isProviderEnabled(locationProvider) && ContextCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                        val location = manager.getLastKnownLocation(locationProvider) as Location
                        val currentLatitude = location.latitude
                        val currentLongitude = location.longitude
                        val msg = "Your Phone is Located at : ( $currentLatitude, $currentLongitude )"
                        val sm = SmsManager.getDefault()
                        sm.sendTextMessage(msgFrom, null, msg, null, null)
                    }
            } catch (e: Exception) {
                //Log.d("Exception caught",e.getMessage());
            }
        }
    }
}