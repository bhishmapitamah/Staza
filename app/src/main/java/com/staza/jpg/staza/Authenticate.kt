package com.staza.jpg.staza

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_authenticate.*
import android.preference.PreferenceManager
import android.widget.Toast

class Authenticate : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_authenticate)

        //Get Default SharedPreferences
        val settings = PreferenceManager.getDefaultSharedPreferences(applicationContext)
        val pass = settings.getString("pass", "")

        //Authenticate and Switch to Main Activity
        pass_enter.setOnClickListener {
            if (pass_field.text.toString()==pass) {

                //startService(Intent(this, StazaIntent::class.java))
                //Switch to Main Activity
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)

                //Display Success Message
                Toast.makeText(this@Authenticate,"Welcome",Toast.LENGTH_LONG).show()

                //Finish current Activity
                finish()
            }
            else {

                //Display Error Message
                Toast.makeText(this@Authenticate,"Incorrect Password",Toast.LENGTH_LONG).show()
            }
        }
    }
}
