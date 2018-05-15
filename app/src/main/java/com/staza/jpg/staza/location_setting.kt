package com.staza.jpg.staza



import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.IntentSender
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.preference.PreferenceManager
import android.support.v7.app.AppCompatActivity
import android.widget.Toast

import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.common.api.PendingResult
import com.google.android.gms.common.api.ResultCallback
import com.google.android.gms.common.api.Status
import com.google.android.gms.location.*
import com.google.android.gms.tasks.Task
import java.security.Key
import android.os.Looper
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.SettingsClient
import com.google.android.gms.location.LocationSettingsRequest
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices.getFusedLocationProviderClient


public open class location_setting : AppCompatActivity(), GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    internal lateinit var googleApiClient: GoogleApiClient
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    internal lateinit var locationManager: LocationManager
    internal lateinit var locationRequest: LocationRequest
    internal lateinit var locationSettingsRequest: LocationSettingsRequest.Builder
    internal lateinit var context: Context
    var requestingLocationUpdates = false
    private var mLocationRequest: LocationRequest? = null

    private val UPDATE_INTERVAL = (10 * 1000).toLong()  /* 10 secs */
    private val FASTEST_INTERVAL: Long = 2000 /* 2 sec */

    private lateinit var locationCallback: LocationCallback

    internal lateinit var pendingResult: PendingResult<LocationSettingsResult>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_location_setting)
        call()
    }

    override fun onResume() {
        super.onResume()
        if (requestingLocationUpdates) startLocationUpdates()
    }

    private fun startLocationUpdates() {
        //fusedLocationClient.requestLocationUpdates(locationRequest,
        //        locationCallback,
        //        null /* Looper */)

        mLocationRequest = LocationRequest()
        mLocationRequest!!.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
        mLocationRequest!!.setInterval(UPDATE_INTERVAL)
        mLocationRequest!!.setFastestInterval(FASTEST_INTERVAL)

        // Create LocationSettingsRequest object using location request
        val builder = LocationSettingsRequest.Builder()
        builder.addLocationRequest(mLocationRequest!!)
        val locationSettingsRequest = builder.build()

        // Check whether location settings are satisfied
        // https://developers.google.com/android/reference/com/google/android/gms/location/SettingsClient
        val settingsClient = LocationServices.getSettingsClient(this)
        settingsClient.checkLocationSettings(locationSettingsRequest)

        // new Google API SDK v11 uses getFusedLocationProviderClient(this)
        if (ContextCompat.checkSelfPermission(this,
                        Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)
        getFusedLocationProviderClient(this).requestLocationUpdates(mLocationRequest, object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult?) {
                // do work here
                onLocationChanged(locationResult!!.lastLocation)
            }
        },
                Looper.myLooper())

    }




    fun onLocationChanged(location: Location) {
        // New location has now been determined
        val msg = "Updated Location: " +
                java.lang.Double.toString(location.getLatitude()) + "," +
                java.lang.Double.toString(location.getLongitude())
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
        // You can now create a LatLng Object for use with maps
       // val latLng = LatLng(location.getLatitude(), location.getLongitude())
    }

    fun createLocationRequest() {
        val locationRequest = LocationRequest().apply {
            interval = 10000
            fastestInterval = 5000
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        }

        val builder = LocationSettingsRequest.Builder()
                .addLocationRequest(locationRequest)
        val client: SettingsClient = LocationServices.getSettingsClient(this)
        val task: Task<LocationSettingsResponse> = client.checkLocationSettings(builder.build())

        task.addOnSuccessListener { locationSettingsResponse ->
            // All location settings are satisfied. The client can initialize
            // location requests here.
            // ...
            startLocationUpdates()
        }

    }

    fun islocation(location: String){
        val prefs = PreferenceManager.getDefaultSharedPreferences(applicationContext)
        val editor = prefs.edit()

        //Change Key
        editor.putString("location", location)
        editor.apply()
    }


    fun call(){

        context = this
        locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            Toast.makeText(this, "Gps is Enabled", Toast.LENGTH_SHORT).show()

        } else {
            mEnableGps()
        }
        requestingLocationUpdates = true
    }
    fun mEnableGps() {
        googleApiClient = GoogleApiClient.Builder(context)
                .addApi(LocationServices.API).addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build()
        googleApiClient.connect()

        mLocationSetting()
        //createLocationRequest()
    }

    fun mLocationSetting() {
        locationRequest = LocationRequest.create()
        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        locationRequest.interval = (1 * 1000).toLong()
        locationRequest.fastestInterval = (1 * 1000).toLong()

        locationSettingsRequest = LocationSettingsRequest.Builder().addLocationRequest(locationRequest)

        mResult()

    }

    fun mResult() {
        pendingResult = LocationServices.SettingsApi.checkLocationSettings(googleApiClient, locationSettingsRequest.build())
        pendingResult.setResultCallback { locationSettingsResult ->
            val status = locationSettingsResult.status


            when (status.statusCode) {
                LocationSettingsStatusCodes.SUCCESS -> {
                }
                LocationSettingsStatusCodes.RESOLUTION_REQUIRED ->

                    try {

                        status.startResolutionForResult(this@location_setting, REQUEST_LOCATION)
                    } catch (e: IntentSender.SendIntentException) {

                    }

                LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE -> {
                }
            }// All location settings are satisfied. The client can initialize location
            // requests here.
            // Location settings are not satisfied. However, we have no way to fix the
            // settings so we won't show the dialog.
        //    startLocationUpdates()
        }
    }

    //callback method
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        val states = LocationSettingsStates.fromIntent(data)
        when (requestCode) {
            REQUEST_LOCATION -> when (resultCode) {
                Activity.RESULT_OK -> {
                    // All required changes were successfully made
                    Toast.makeText(context, "Gps enabled", Toast.LENGTH_SHORT).show()
                    islocation("1")

                }

                Activity.RESULT_CANCELED -> {
                    // The user was asked to change settings, but chose not to
                    call()
                    islocation("0")
                }
                else -> {
                    islocation("0")
                }
            }
        }
    }


    override fun onConnected(bundle: Bundle?) {

    }

    override fun onConnectionSuspended(i: Int) {

    }

    override fun onConnectionFailed(connectionResult: ConnectionResult) {

    }

    companion object {

        val REQUEST_LOCATION = 1
    }

    override fun onPause() {
        super.onPause()
        stopLocationUpdates()
    }

    private fun stopLocationUpdates() {
        fusedLocationClient.removeLocationUpdates(locationCallback)
    }
}