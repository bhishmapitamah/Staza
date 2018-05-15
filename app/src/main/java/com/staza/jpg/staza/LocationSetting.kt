package com.staza.jpg.staza

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.IntentSender
import android.location.LocationManager
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.Toast
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.common.api.PendingResult
import com.google.android.gms.location.*

open class LocationSetting : AppCompatActivity(), GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    //Private Members
    private lateinit var googleApiClient: GoogleApiClient
    private lateinit var locationManager: LocationManager
    private lateinit var locationRequest: LocationRequest
    private lateinit var locationSettingsRequest: LocationSettingsRequest.Builder
    private lateinit var context: Context
    private lateinit var pendingResult: PendingResult<LocationSettingsResult>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //Enable GPS
        Log.d("debug","in loc settings")

        call()
        Log.d("debug","call done")

    }

    //Wrapper for Enabling GPS
    private fun call(){

        Log.d("debug","in call")
        context = this


        val j = Intent()
        j.setClass(this, ReqLoc_SendSMS::class.java)
        j.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        val bundle1 = intent.extras

        Log.d("debug","in call before if")

        locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            Log.d("debug","gps enabled")

            Toast.makeText(this, "Gps is Enabled", Toast.LENGTH_SHORT).show()
            ContextCompat.startActivity(this, j, bundle1)
            Log.d("debug","new activity started")

        } else {
            Log.d("debug","gps not enabled")

            mEnableGps()
        }
    }

    private fun mEnableGps() {
        Log.d("debug","in enable gps")

        googleApiClient = GoogleApiClient.Builder(context)
                .addApi(LocationServices.API).addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build()
        googleApiClient.connect()
        mLocationSetting()
    }

    private fun mLocationSetting() {
        Log.d("debug","in loc set")

        locationRequest = LocationRequest.create()
        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        locationRequest.interval = (1 * 1000).toLong()
        locationRequest.fastestInterval = (1 * 1000).toLong()
        locationSettingsRequest = LocationSettingsRequest.Builder().addLocationRequest(locationRequest)
        mResult()
    }

    private fun mResult() {
        Log.d("debug","in loc result")

        pendingResult = LocationServices.SettingsApi.checkLocationSettings(googleApiClient, locationSettingsRequest.build())
        pendingResult.setResultCallback { locationSettingsResult ->
            val status = locationSettingsResult.status
            when (status.statusCode) {
                LocationSettingsStatusCodes.SUCCESS -> {
                }
                LocationSettingsStatusCodes.RESOLUTION_REQUIRED ->
                    try {
                        status.startResolutionForResult(this@LocationSetting, REQUEST_LOCATION)
                    } catch (e: IntentSender.SendIntentException) {

                    }
                LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE -> {
                }
            }
            // All location settings are satisfied. The client can initialize location
            // requests here.
            // Location settings are not satisfied. However, we have no way to fix the
            // settings so we won't show the dialog.
            Log.d("debug","all loc settings satisfied")


            val i = Intent()
            i.setClass(this, ReqLoc_SendSMS::class.java)
            i.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            val bundle = intent.extras

            ContextCompat.startActivity(this, i, bundle)
            Log.d("debug","going out of mresult")

        }
    }

    //callback method
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        Log.d("debug","in on activity result")

        val i = Intent()
        i.setClass(context, ReqLoc_SendSMS::class.java)
        i.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        val bundle = intent.extras


        when (requestCode) {
            REQUEST_LOCATION -> when (resultCode) {
                Activity.RESULT_OK -> {
                    Toast.makeText(context, "Gps is Enabled", Toast.LENGTH_SHORT).show()
                    ContextCompat.startActivity(context, i, bundle)

                }
                Activity.RESULT_CANCELED ->
                    //Recursive call to Enable GPS
                    call()
                else -> {
                }
            }
        }
        Log.d("debug","last line of activity result")

    }

    override fun onDestroy() {
        super.onDestroy()
        //Recursive call to activity
        val i = Intent()
        i.setClass(context, LocationSetting::class.java)
        i.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        val bundle = intent.extras
        ContextCompat.startActivity(context, i, bundle)
    }

    override fun onConnected(bundle: Bundle?) = Unit

    override fun onConnectionSuspended(i: Int) = Unit

    override fun onConnectionFailed(connectionResult: ConnectionResult) = Unit

    companion object {
        const val REQUEST_LOCATION = 1
    }
}