package com.android.homequest

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.LinearLayout

class DeveloperActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_developer)

        val button_back = findViewById<ImageButton>(R.id.button_back)
        button_back.setOnClickListener {
            val intent = Intent(this, LandingActivity::class.java)
            startActivity(intent)
        }

        val overlayLayout = findViewById<LinearLayout>(R.id.overlayLayout)
        val buttonShowOverlay = findViewById<Button>(R.id.buttonShowOverlay)
        val mainLayout =  findViewById<LinearLayout>(R.id.mainLayout)

        buttonShowOverlay.setOnClickListener {
            overlayLayout.visibility = View.VISIBLE  // Show Overlay

        }

        overlayLayout.setOnClickListener {
            overlayLayout.visibility = View.GONE  // Hide Overlay when clicked

        }


    }
}