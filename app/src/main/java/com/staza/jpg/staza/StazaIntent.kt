/*
package com.staza.jpg.staza

import android.annotation.SuppressLint
import android.app.IntentService
import android.content.Intent
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.net.Uri
import android.preference.PreferenceManager
import android.support.v4.content.ContextCompat
import android.telephony.SmsManager
import android.widget.Toast

/**
 * An [IntentService] subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 *
 *
 * TODO: Customize class - update intent actions, extra parameters and static
 * helper methods.
 */
class StazaIntent : IntentService("StazaIntent") {

    override fun onHandleIntent(intent: Intent?) {
        if (intent != null) {
            val action = intent.action
            if (ACTION_FOO == action) {
                val param1 = intent.getStringExtra(EXTRA_PARAM1)
                val param2 = intent.getStringExtra(EXTRA_PARAM2)
                handleActionFoo(param1, param2)
            } else if (ACTION_BAZ == action) {
                val param1 = intent.getStringExtra(EXTRA_PARAM1)
                val param2 = intent.getStringExtra(EXTRA_PARAM2)
                handleActionBaz(param1, param2)
            }
        }
    }
    //var context: Context? = null
    @SuppressLint("MissingPermission")


    override fun onCreate() {
        super.onCreate()
        val locationProvider = LocationManager.GPS_PROVIDER
        val manager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        val settings = PreferenceManager.getDefaultSharedPreferences(this)
        val text = settings.getString("msg", "")
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show()
        if(settings.getString("msg", "") == settings.getString("pass", ""))
        if (manager.isProviderEnabled(locationProvider) && ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            val location = manager.getLastKnownLocation(locationProvider) as Location
            val currentLatitude = location.latitude
            val currentLongitude = location.longitude
            val msg = ("Your latitude is: " + currentLatitude +
                    "Your longitude is: " + currentLongitude)
            val sm = SmsManager.getDefault()
            sm.sendTextMessage(settings.getString("mob", ""), null, msg, null, null)
        }
      /*public static final String INBOX = "content://sms/inbox";
        public static final String SENT = "content://sms/sent";
        public static final String DRAFT = "content://sms/draft";
        val cursor = contentResolver.query(Uri.parse("content://sms/inbox"), null, null, null, null)

        if (cursor!!.moveToFirst()) { // must check the result to prevent exception
            do {
                var msgData = ""
                for (idx in 0 until cursor.columnCount) {
                    msgData += " " + cursor.getColumnName(idx) + ":" + cursor.getString(idx)
                }
                // use msgData
            } while (cursor.moveToNext())
        } else {
            // empty box, no SMS
        }
        cursor.close()*/

    }



    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        return START_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()
        Toast.makeText(this,"destroy",Toast.LENGTH_LONG).show()
    }

    /**
     * Handle action Foo in the provided background thread with the provided
     * parameters.
     */
    private fun handleActionFoo(param1: String, param2: String) {
        // TODO: Handle action Foo
        throw UnsupportedOperationException("Not yet implemented")
    }

    /**
     * Handle action Baz in the provided background thread with the provided
     * parameters.
     */
    private fun handleActionBaz(param1: String, param2: String) {
        // TODO: Handle action Baz
        throw UnsupportedOperationException("Not yet implemented")
    }

    companion object {
        // TODO: Rename actions, choose action names that describe tasks that this
        // IntentService can perform, e.g. ACTION_FETCH_NEW_ITEMS
        private val ACTION_FOO = "com.staza.jpg.staza.action.FOO"
        private val ACTION_BAZ = "com.staza.jpg.staza.action.BAZ"

        // TODO: Rename parameters
        private val EXTRA_PARAM1 = "com.staza.jpg.staza.extra.PARAM1"
        private val EXTRA_PARAM2 = "com.staza.jpg.staza.extra.PARAM2"

        /**
         * Starts this service to perform action Foo with the given parameters. If
         * the service is already performing a task this action will be queued.
         *
         * @see IntentService
         */
        // TODO: Customize helper method
        fun startActionFoo(context: Context, param1: String, param2: String) {
            val intent = Intent(context, StazaIntent::class.java)
            intent.action = ACTION_FOO
            intent.putExtra(EXTRA_PARAM1, param1)
            intent.putExtra(EXTRA_PARAM2, param2)
            context.startService(intent)
        }

        /**
         * Starts this service to perform action Baz with the given parameters. If
         * the service is already performing a task this action will be queued.
         *
         * @see IntentService
         */
        // TODO: Customize helper method
        fun startActionBaz(context: Context, param1: String, param2: String) {
            val intent = Intent(context, StazaIntent::class.java)
            intent.action = ACTION_BAZ
            intent.putExtra(EXTRA_PARAM1, param1)
            intent.putExtra(EXTRA_PARAM2, param2)
            context.startService(intent)
        }
    }
}
*/