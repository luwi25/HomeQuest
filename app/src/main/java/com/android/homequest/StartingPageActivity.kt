package com.android.homequest

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class StartingPageActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_starting_page)

        val sharedPreferences = getSharedPreferences("UserSession", MODE_PRIVATE)
        val editor = sharedPreferences.edit()

        editor.putString("screen", "StartingPage")
        editor.commit()

        val button_login = findViewById<Button>(R.id.button_login)
        button_login.setOnClickListener {
            startActivity(
                Intent(this, LoginActivity::class.java)
            )
        }

        val button_register = findViewById<Button>(R.id.button_register)
        button_register.setOnClickListener {
            startActivity(
                Intent(this, RegisterActivity::class.java)
            )
        }


    }
}