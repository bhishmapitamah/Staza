package com.staza.jpg.staza

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.preference.PreferenceManager
import android.widget.Toast
//import com.staza.jpg.staza.R.id.*
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*

val random = Random()
class MainActivity : AppCompatActivity() {

    private var count = 6
    private var Key = ""

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

        //show Key code
        view_code.setOnClickListener{
            //Show The Code and Generate if not Already Generated
            randygenrator()
        }

        //change password
        change_pass.setOnClickListener{
            //Switch to ChangePassword Activity
            val intent = Intent(this, ChangePassword::class.java)
            startActivity(intent)
        }

        //generate new Code
        new_code.setOnClickListener{
            //Generate New Code
            Key = ""
            randygenrator()
        }
    }
}

