package com.staza.jpg.staza

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_authenticate.*
import android.preference.PreferenceManager
import android.widget.Toast
import android.content.DialogInterface
import android.os.Build
import android.support.v7.app.AlertDialog


class Authenticate : AppCompatActivity() {

    private val REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS = 124
    private var allow = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_authenticate)

        //Get Default SharedPreferences
        val settings = PreferenceManager.getDefaultSharedPreferences(applicationContext)
        val pass = settings.getString("pass", "")

        //Take permissions from the user
        takePermissions()

        //Authenticate and Switch to Main Activity
        if (allow) {
            pass_enter.setOnClickListener {
                if (pass_field.text.toString() == pass) {

                    //Switch to Main Activity
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)

                    //Display Success Message
                    Toast.makeText(this@Authenticate, "Welcome", Toast.LENGTH_LONG).show()

                    //Finish current Activity
                    finish()
                } else {
                    //Display Error Message
                    Toast.makeText(this@Authenticate, "Incorrect Password", Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    private fun takePermissions() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            allow = true
            return
        }

        val permissionsNeeded = ArrayList<String>()
        val permissionsList = ArrayList<String>()

        if (!addPermission(permissionsList, Manifest.permission.ACCESS_FINE_LOCATION))
            permissionsNeeded.add("GPS")
        if (!addPermission(permissionsList, Manifest.permission.READ_SMS))
            permissionsNeeded.add("Read SMS")
        if (!addPermission(permissionsList, Manifest.permission.SEND_SMS))
            permissionsNeeded.add("Send SMS")

        if (permissionsList.size > 0) {
            if (permissionsNeeded.size > 0) {
                // Need Rationale
                var message = "You need to grant access to " + permissionsNeeded[0]
                for (i in 1 until permissionsNeeded.size)
                    message = message + ", " + permissionsNeeded[i]
                message += ". Please do not click on never ask again."
                showMessageOKCancel(message,
                        DialogInterface.OnClickListener { dialog, which ->
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                requestPermissions(permissionsList.toArray(arrayOfNulls(permissionsList.size)),
                                        REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS)
                            }
                        })
                return
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(permissionsList.toTypedArray(),
                        REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS)
            }
            return
        }
        allow = true
    }

    private fun addPermission(permissionsList: MutableList<String>, permission: String): Boolean {
        if (if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            checkSelfPermission(permission) != PackageManager.PERMISSION_GRANTED
        } else {
            TODO("VERSION.SDK_INT < M")
        }) {
            permissionsList.add(permission)
            // Check for Rationale Option
            if (if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                !shouldShowRequestPermissionRationale(permission)
            } else {
                TODO("VERSION.SDK_INT < M")
            })
                return false
        }
        return true
    }

    private fun showMessageOKCancel(message: String, okListener: DialogInterface.OnClickListener) {
        AlertDialog.Builder(this)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", null)
                .create()
                .show()
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        when (requestCode) {
            REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS -> {
                val perms = HashMap<String, Int>()
                // Initial
                perms.put(Manifest.permission.ACCESS_FINE_LOCATION, PackageManager.PERMISSION_GRANTED)
                perms.put(Manifest.permission.READ_SMS, PackageManager.PERMISSION_GRANTED)
                perms.put(Manifest.permission.SEND_SMS, PackageManager.PERMISSION_GRANTED)
                // Fill with results
                for (i in permissions.indices)
                    perms.put(permissions[i], grantResults[i])
                // Check for ACCESS_FINE_LOCATION
                if (perms[Manifest.permission.ACCESS_FINE_LOCATION] == PackageManager.PERMISSION_GRANTED
                        && perms[Manifest.permission.READ_SMS] == PackageManager.PERMISSION_GRANTED
                        && perms[Manifest.permission.SEND_SMS] == PackageManager.PERMISSION_GRANTED) {
                    // All Permissions Granted
                    allow = true
                } else {
                    // Permission Denied
                    Toast.makeText(this, "Some Permission is Denied", Toast.LENGTH_SHORT)
                            .show()
                    android.os.Process.killProcess(android.os.Process.myPid())
                    System.exit(1)
                }
            }
            else -> super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        }
    }
}