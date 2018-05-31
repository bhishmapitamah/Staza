package com.staza.jpg.staza
import android.annotation.SuppressLint
import android.content.IntentSender
import android.location.Location
import android.os.Bundle
import android.preference.PreferenceManager
import android.support.v7.app.AppCompatActivity
import android.telephony.SmsManager
import android.util.Log
import android.widget.Toast

import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener
import com.google.android.gms.location.LocationListener
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.staza.jpg.staza.R

class ReqLoc_SendSMS : AppCompatActivity(), ConnectionCallbacks, OnConnectionFailedListener, LocationListener {
    private var mGoogleApiClient: GoogleApiClient? = null
    private var mLocationRequest: LocationRequest? = null
    private var currentLatitude: Double = 0.toDouble()
    private var currentLongitude: Double = 0.toDouble()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_home)

        mGoogleApiClient = GoogleApiClient.Builder(this)
                // The next two lines tell the new client that “this” current class will handle connection stuff
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                //fourth line adds the LocationServices API endpoint from GooglePlayServices
                .addApi(LocationServices.API)
                .build()

        // Create the LocationRequest object
        mLocationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval((10 * 1000).toLong())        // 10 seconds, in milliseconds
                .setFastestInterval((1 * 1000).toLong()) // 1 second, in milliseconds

    }

    override fun onResume() {
        super.onResume()
        //Now lets connect to the API
        mGoogleApiClient!!.connect()
    }

    override fun onPause() {
        super.onPause()
        Log.v(this.javaClass.simpleName, "onPause()")

        //Disconnect from API onPause()
        if (mGoogleApiClient!!.isConnected) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this)
            mGoogleApiClient!!.disconnect()
        }


    }

    /**
     * If connected get lat and long
     *
     */

    @SuppressLint("MissingPermission")
    override fun onConnected(bundle: Bundle?) {
        val location = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient)

        if (location == null) {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this)

        } else {
            //If everything went fine lets get latitude and longitude
            currentLatitude = location.latitude
            currentLongitude = location.longitude

            val settings = PreferenceManager.getDefaultSharedPreferences(applicationContext)
            val msgFrom = settings.getString("msgFrom", "")
            //val currentLatitude = mCurrentLocation!!.latitude
            //val currentLongitude = mCurrentLocation!!.longitude
            val msg = "Your Phone is Located at : ( $currentLatitude, $currentLongitude )"
            val sm = SmsManager.getDefault()
            sm.sendTextMessage(msgFrom, null, msg, null, null)

            Toast.makeText(this, currentLatitude.toString() + " WORKS " + currentLongitude + "", Toast.LENGTH_LONG).show()
        }
    }


    override fun onConnectionSuspended(i: Int) {}

    override fun onConnectionFailed(connectionResult: ConnectionResult) {
        /*
             * Google Play services can resolve some errors it detects.
             * If the error has a resolution, try sending an Intent to
             * start a Google Play services activity that can resolve
             * error.
             */
        if (connectionResult.hasResolution()) {
            try {
                // Start an Activity that tries to resolve the error
                connectionResult.startResolutionForResult(this, CONNECTION_FAILURE_RESOLUTION_REQUEST)
                /*
                     * Thrown if Google Play services canceled the original
                     * PendingIntent
                     */
            } catch (e: IntentSender.SendIntentException) {
                // Log the error
                e.printStackTrace()
            }

        } else {
            /*
                 * If no resolution is available, display a dialog to the
                 * user with the error.
                 */
            Log.e("Error", "Location services connection failed with code " + connectionResult.errorCode)
        }
    }

    /**
     * If locationChanges change lat and long
     *
     *
     * @param location
     */
    override fun onLocationChanged(location: Location) {
        currentLatitude = location.getLatitude()
        currentLongitude = location.getLongitude()

        val settings = PreferenceManager.getDefaultSharedPreferences(applicationContext)
        val msgFrom = settings.getString("msgFrom", "")
        //val currentLatitude = mCurrentLocation!!.latitude
        //val currentLongitude = mCurrentLocation!!.longitude
        val msg = "Your Phone is Located at : ( $currentLatitude, $currentLongitude )"
        val sm = SmsManager.getDefault()
        sm.sendTextMessage(msgFrom, null, msg, null, null)

        Toast.makeText(this, currentLatitude.toString() + " WORKS " + currentLongitude + "", Toast.LENGTH_LONG).show()
    }

    companion object {

        //Define a request code to send to Google Play services
        private val CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000
    }

}