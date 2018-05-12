package com.staza.jpg.staza

import android.Manifest
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import kotlinx.android.synthetic.main.activity_welcome_splash.*
import android.content.Context
import android.content.pm.PackageManager
import android.preference.PreferenceManager.getDefaultSharedPreferences
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.widget.Toast
import java.util.*
import kotlin.concurrent.timerTask
import android.R.string.ok
import android.view.View
//import android.support.design.widget.Snackbar


class WelcomeSplash : AppCompatActivity() {

    private val REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS = 124

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_welcome_splash)
        val splash: Animation = AnimationUtils.loadAnimation(this,R.anim.splash)
        name.startAnimation(splash)
        image.startAnimation(splash)

        Timer().schedule(timerTask {
            checkFirstRun()
            finish() }, 3000)
    }



    private fun checkFirstRun() {

        val PREF_VERSION_CODE_KEY = "version_code"
        val DOESNT_EXIST = -1

        // Get current version code
        val currentVersionCode = BuildConfig.VERSION_CODE

        // Get saved version code
        val prefs = getDefaultSharedPreferences(applicationContext)
        val savedVersionCode = prefs.getInt(PREF_VERSION_CODE_KEY, DOESNT_EXIST)

        // Check for first run or upgrade
        if (currentVersionCode == savedVersionCode) {

            // This is just a normal run
            val i = Intent(this,Authenticate::class.java)
            startActivity(i)
            return

        } else if (savedVersionCode == DOESNT_EXIST) {

            // This is a new install (or the user cleared the shared preferences)
            val i = Intent(this,Register::class.java)
            startActivity(i)


        } else if (currentVersionCode > savedVersionCode) {

            //This is an upgrade
            val i = Intent(this,Register::class.java)
            startActivity(i)

        }
    }
}