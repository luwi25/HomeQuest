package com.android.homequest

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.Toast

class LandingActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_landing)

        val button_settings = findViewById<LinearLayout>(R.id.button_settings)
        button_settings.setOnClickListener {
            val intent = Intent(this, SettingsActivity::class.java)
            startActivity(intent)
        }

        val button_profile = findViewById<LinearLayout>(R.id.button_profile)
        button_profile.setOnClickListener {
            val intent = Intent(this, ProfileActivity::class.java)
            startActivity(intent)
        }

        val button_developer = findViewById<LinearLayout>(R.id.button_developer)
        button_developer.setOnClickListener {
            val intent = Intent(this, DeveloperActivity::class.java)
            startActivity(intent)
        }

        val button_logout = findViewById<LinearLayout>(R.id.button_logout)
        button_logout.setOnClickListener {
            val intent = Intent(this, LogoutActivity::class.java)
            startActivity(intent)
        }
        val button_addtask = findViewById<LinearLayout>(R.id.button_addtask)
        button_addtask.setOnClickListener {
            Toast.makeText(this, "No Task Yet!", Toast.LENGTH_SHORT).show()
        }

        val button_famChal = findViewById<LinearLayout>(R.id.button_famChal)
        button_famChal.setOnClickListener {
            Toast.makeText(this, "No Challenges Yet!", Toast.LENGTH_SHORT).show()
        }
        val button_rewards = findViewById<LinearLayout>(R.id.button_rewards)
        button_rewards.setOnClickListener {
            Toast.makeText(this, "No Rewards Yet!", Toast.LENGTH_SHORT).show()
        }

        val overlayLayout = findViewById<LinearLayout>(R.id.overlayLayout)
        val buttonShowOverlay = findViewById<ImageButton>(R.id.buttonShowOverlay)
        val mainLayout =  findViewById<LinearLayout>(R.id.mainLayout)

        buttonShowOverlay.setOnClickListener {
            overlayLayout.visibility = View.VISIBLE  // Show Overlay

        }

        overlayLayout.setOnClickListener {
            overlayLayout.visibility = View.GONE  // Hide Overlay when clicked

        }
        val home = findViewById<LinearLayout>(R.id.home)
        home.setOnClickListener {
            overlayLayout.visibility = View.GONE
        }

    }
}