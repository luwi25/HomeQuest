package com.android.homequest

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton



class ProfileActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)


        val button_back = findViewById<ImageButton>(R.id.button_back)

        button_back.setOnClickListener {
            val intent = Intent(this, ParentDashboardActivity::class.java)
            startActivity(intent)
        }

    }
}