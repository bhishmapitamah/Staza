package com.staza.jpg.staza

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.preference.PreferenceManager
import android.widget.EditText
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_authenticate.*

class Authenticate : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_authenticate)
        //val settings = PreferenceManager.getDefaultSharedPreferences(this)
        //var pass = settings.getString("pass", "")
        val pass="6969"
        Toast.makeText(this@Authenticate,pass, Toast.LENGTH_LONG).show()
        pass_enter.setOnClickListener {
            val password = findViewById<EditText>(R.id.pass_field)
            if (password.text.toString()==pass) {
                startService(Intent(this, StazaIntent::class.java))
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
            }
        }
    }
}
