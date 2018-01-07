package com.staza.jpg.staza

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import android.support.v4.app.NotificationCompat.getExtras
import android.content.Intent
import android.content.BroadcastReceiver
import android.content.Context
import android.preference.PreferenceManager
import android.telephony.SmsMessage
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.support.v4.content.ContextCompat
import android.telephony.SmsManager


//import kotlinx.android.synthetic.main.activity_change_password.*


/**
 * Created by chinmay on 07-Jan-18.
 */
class SmsListener : BroadcastReceiver() {

    private val preferences: SharedPreferences? = null

    override fun onReceive(context: Context, intent: Intent) {
        // TODO Auto-generated method stub
        val bundle = intent.extras

        Toast.makeText(context, "poruka: ", Toast.LENGTH_SHORT).show()


        if (bundle != null) {
            //---get the SMS message passed in---
            var msgs: Array<SmsMessage?>? = null
            var msg_from: String
            if (bundle != null) {
                //---retrieve the SMS message received---
                try {
                    val pdus = bundle.get("pdus") as Array<Any>
                    val s = pdus.size
                    val settings = PreferenceManager.getDefaultSharedPreferences(context)
                    msgs = arrayOfNulls<SmsMessage>(s) //msgs = new SmsMessage[pdus.length];
                    for (i in msgs.indices) {
                        msgs[i] = SmsMessage.createFromPdu(pdus[i] as ByteArray)
                        msg_from = msgs[i]!!.originatingAddress
                        val editor = settings.edit()
                        editor.putString("mob",msg_from)
                        editor.apply()
                        val msgBody = msgs[i]!!.messageBody
                        val editor2 = settings.edit()
                        editor2.putString("msg",msgBody)
                        editor2.apply()

                        val toast1 = Toast.makeText(context, "poruka: " + msgBody, Toast.LENGTH_SHORT)
                        toast1.show()



                        @SuppressLint("MissingPermission")
                        val locationProvider = LocationManager.GPS_PROVIDER
                        val manager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
                        val settings = PreferenceManager.getDefaultSharedPreferences(context)
                        val text = settings.getString("msg", "")
                        Toast.makeText(context, text, Toast.LENGTH_SHORT).show()
                        if(settings.getString("msg", "") == settings.getString("key", ""))
                            if (manager.isProviderEnabled(locationProvider) && ContextCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                                val location = manager.getLastKnownLocation(locationProvider) as Location
                                val currentLatitude = location.latitude
                                val currentLongitude = location.longitude
                                val msg = ("Your latitude is: " + currentLatitude +
                                        "Your longitude is: " + currentLongitude)
                                val sm = SmsManager.getDefault()
                                sm.sendTextMessage(settings.getString("mob", ""), null, msg, null, null)
                            }
                    }
                    //val senderNo = msgs[0]!!.getDisplayOriginatingAddress()
                } catch (e: Exception) {
                    //                            Log.d("Exception caught",e.getMessage());
                }

            }
        }
    }
}