package com.staza.jpg.staza

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.preference.PreferenceManager
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_register.*

class Register : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        submit.setOnClickListener{
            //Check if Both Passwords Match
            if(new_pass.text.toString()==new_pass_cnf.text.toString()){

                //Get Default SharedPreference
                val PREF_VERSION_CODE_KEY = "version_code"
                val currentVersionCode = BuildConfig.VERSION_CODE
                val prefs = PreferenceManager.getDefaultSharedPreferences(applicationContext)
                val editor = prefs.edit()

                //Save The New Password
                editor.putString("pass", new_pass.text.toString())
                editor.apply()

                //Welcome Message
                Toast.makeText(this@Register,"Welcome",Toast.LENGTH_LONG).show()

                //Switch to Main Activity
                val i = Intent(this,MainActivity::class.java)
                startActivity(i)

                // Update the shared preferences with the current version code
                prefs.edit().putInt(PREF_VERSION_CODE_KEY, currentVersionCode).apply()

                //Finish Current Activity
                finish()
            }
            else{
                //Error Toast
                Toast.makeText(this@Register,"Password didn't match",Toast.LENGTH_LONG).show()
            }
        }
    }
}
