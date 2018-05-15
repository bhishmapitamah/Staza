package com.staza.jpg.staza

import android.Manifest
import android.location.Location
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.google.android.gms.common.api.GoogleApiClient
import android.os.Looper
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.SettingsClient
import com.google.android.gms.location.LocationSettingsRequest
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices.getFusedLocationProviderClient
import android.widget.Toast
import android.support.annotation.NonNull
import android.util.Log
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.android.gms.location.FusedLocationProviderClient
import android.Manifest.permission
import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.content.IntentFilter
import android.content.IntentSender
import android.support.v4.app.ActivityCompat
import android.content.pm.PackageManager
import android.support.v4.content.ContextCompat








public open class location : AppCompatActivity(), GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {


    val yourBR = SmsListener()
    //yourBR = new SmsListener();

    //yourBR.setMainActivityHandler(this);
    var callInterceptorIntentFilter = IntentFilter("android.intent.action.ANY_ACTION");
    //registerReceiver(yourBR,  callInterceptorIntentFilter);

    override fun onConnectionFailed(connectionResult: ConnectionResult) {
        //TODO("not implemented") //To change body of created functions use File | Settings | File Templates.

        Toast.makeText(this, "disconnected", Toast.LENGTH_SHORT).show()


    }

    override fun onConnectionSuspended(p0: Int) {
        //TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        Toast.makeText(this, "disonnected", Toast.LENGTH_SHORT).show()

    }

    override fun onConnected(p0: Bundle?) {
        //TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        Toast.makeText(this, "Connected", Toast.LENGTH_SHORT).show()

    }

    private var mLocationRequest: LocationRequest? = null

    private val UPDATE_INTERVAL = (10 * 1000).toLong()  /* 10 secs */
    private val FASTEST_INTERVAL: Long = 2000 /* 2 sec */

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Toast.makeText(this, "created", Toast.LENGTH_SHORT).show()
        //yourBR.setMainActivityHandler(this)
        registerReceiver(yourBR,  callInterceptorIntentFilter)


        setContentView(R.layout.activity_main)
        startLocationUpdates()
    }

    // Trigger new location updates at interval
    fun startLocationUpdates() {

        // Create the location request to start receiving updates
        mLocationRequest = LocationRequest()
        mLocationRequest!!.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        mLocationRequest!!.interval = UPDATE_INTERVAL
        mLocationRequest!!.fastestInterval = FASTEST_INTERVAL

        // Create LocationSettingsRequest object using location request
        val builder = LocationSettingsRequest.Builder()
        builder.addLocationRequest(mLocationRequest!!)
        val locationSettingsRequest = builder.build()

        // Check whether location settings are satisfied
        // https://developers.google.com/android/reference/com/google/android/gms/location/SettingsClient
        val settingsClient = LocationServices.getSettingsClient(this)
        settingsClient.checkLocationSettings(locationSettingsRequest)

        // new Google API SDK v11 uses getFusedLocationProviderClient(this)
        if(ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            getFusedLocationProviderClient(this).requestLocationUpdates(mLocationRequest, object : LocationCallback() {
                override fun onLocationResult(locationResult: LocationResult?) {
                    // do work here
                    onLocationChanged(locationResult!!.lastLocation)
                }
            },
                    Looper.myLooper())
        }
    }

    fun onLocationChanged(location: Location) {
        // New location has now been determined
        val msg = "Updated Location: " +
                java.lang.Double.toString(location.getLatitude()) + "," +
                java.lang.Double.toString(location.getLongitude())
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
        // You can now create a LatLng Object for use with maps
        //val latLng = LatLng(location.getLatitude(), location.getLongitude())
    }

    fun getLastLocation() {
        // Get last known recent location using new Google Play Services SDK (v11+)
        val locationClient = getFusedLocationProviderClient(this)

        if(ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            locationClient.lastLocation
                    .addOnSuccessListener { location ->
                        // GPS location can be null if GPS is switched off
                        if (location != null) {
                            onLocationChanged(location)
                            Toast.makeText(this, "success", Toast.LENGTH_SHORT).show()

                        }
                    }
                    .addOnFailureListener { e ->
                        Log.d("MapDemoActivity", "Error trying to get last GPS location")
                        e.printStackTrace()
                    }
        }
    }



    /*private fun requestPermissions() {
        ActivityCompat.requestPermissions(this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                REQUEST_FINE_LOCATION)
    }*/


}