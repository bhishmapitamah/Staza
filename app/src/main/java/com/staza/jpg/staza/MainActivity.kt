package com.staza.jpg.staza

import android.Manifest
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.preference.PreferenceManager
import android.support.v7.app.AlertDialog
import android.widget.Toast
//import com.staza.jpg.staza.R.id.*
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*

val random = Random()
class MainActivity : AppCompatActivity() {

    private var count = 6
    private var Key = ""

    private val REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS = 124
    private var allow = false

    private fun rand(from: Int, to: Int) : Int {
        return random.nextInt(to - from) + from
    }

    //code
    private fun randygenrator(){

        //Generate New Code if Not Present
        if(Key.isEmpty()) {
            while (count > 0) {
                Key += rand(0, 9).toString()
                --count
            }
            count = 6

            //Get Default SharedPreferences
            val prefs = PreferenceManager.getDefaultSharedPreferences(applicationContext)
            val editor = prefs.edit()

            //Change Key
            editor.putString("key", Key)
            editor.apply()
        }

        //Display New Key
        Toast.makeText(this@MainActivity, Key, Toast.LENGTH_LONG).show()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //Get Default SharedPreferences
        val settings = PreferenceManager.getDefaultSharedPreferences(applicationContext)
        Key = settings.getString("key", "")

        takePermissions()
        Toast.makeText(this, "$allow", Toast.LENGTH_LONG).show()

        if(allow) {
            //show Key code
            view_code.setOnClickListener {
                //Show The Code and Generate if not Already Generated
                randygenrator()
            }

            //change password
            change_pass.setOnClickListener {
                //Switch to ChangePassword Activity
                val intent = Intent(this, ChangePassword::class.java)
                startActivity(intent)
            }

            //generate new Code
            new_code.setOnClickListener {
                //Generate New Code
                Key = ""
                randygenrator()
            }
        }
        else{
            Toast.makeText(this, "Some Permission is Denied", Toast.LENGTH_SHORT)
                    .show()
            android.os.Process.killProcess(android.os.Process.myPid())
            System.exit(1)
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

