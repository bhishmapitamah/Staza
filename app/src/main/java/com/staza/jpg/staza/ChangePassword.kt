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

        //Match Passwords on Submit
        enter.setOnClickListener {

            //Get Default SharedPreferences
            val settings = PreferenceManager.getDefaultSharedPreferences(applicationContext)
            val oldPass = settings.getString("pass", "")

            //Match Passwords
            if (old_pass.text.toString()==oldPass) {

                //Store New Password
                val editor = settings.edit()
                editor.putString("pass", new_pass_view.text.toString())
                editor.apply()

                //Display Success Toast
                Toast.makeText(this@ChangePassword,"Password changed",Toast.LENGTH_LONG).show()

                //Back to Main Activity
                finish()
            }
            else{

                //Display Error Toast
                Toast.makeText(this@ChangePassword,"Incorrect Password",Toast.LENGTH_LONG).show()
            }
        }
    }
}
