package com.staza.jpg.staza

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.preference.PreferenceManager
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_change_password.*

class ChangePassword : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_change_password)
        enter.setOnClickListener {
            val settings = PreferenceManager.getDefaultSharedPreferences(this)
            val oldpass = settings.getString("pass", "")
            if (old_pass.text.toString()==oldpass) {
                val editor = settings.edit()
                editor.putString("pass",new_pass.text.toString())
                editor.apply()
                Toast.makeText(this@ChangePassword,"Password changed",Toast.LENGTH_LONG).show()
            }
            else{
                Toast.makeText(this@ChangePassword,"Incorrect Password",Toast.LENGTH_LONG).show()
            }
        }
    }
}
