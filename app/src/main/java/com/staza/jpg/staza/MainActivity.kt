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

    private fun rand(from: Int, to: Int) : Int {
        return random.nextInt(to - from) + from
    }
    private var count = 16
    var code = ""                               //code
    private fun randygenrator(){
        if(code.isEmpty()) {
            while (count > 0) {
                code += rand(0, 9).toString()
                --count
            }
            count = 16
            val settings = PreferenceManager.getDefaultSharedPreferences(this)
            val editor = settings.edit()
            editor.putString("key", code)
            editor.apply()
        }
        Toast.makeText(this@MainActivity, code, Toast.LENGTH_LONG).show()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        val keyString = "key"
        val settings = PreferenceManager.getDefaultSharedPreferences(this)
        val editor=settings.edit()
        editor.putString("pass","6969")
        editor.apply()
        code = settings.getString(keyString, "")
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //show otp code
        view_code.setOnClickListener{
            randygenrator()
        }
        //change password
        change_pass.setOnClickListener{
            val intent = Intent(this, ChangePassword::class.java)
            startActivity(intent)
        }
        //generate new Code
        new_code.setOnClickListener{
            code = ""
            randygenrator()
        }

    }
}

