package com.android.homequest

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Button

class LogoutActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_logout)

        val sharedPreferences = getSharedPreferences("UserSession", MODE_PRIVATE)
        val role = sharedPreferences.getString("role", "Default")

        val btnCancel = findViewById<Button>(R.id.btnCancel)
        btnCancel.setOnClickListener {

            if(role == "Parent")
            {
                startActivity(
                    Intent(this, ParentDashboardActivity::class.java)
                )
            }
            else if(role == "Child")
            {
                startActivity(
                    Intent(this, ChildDashboardActivity::class.java)
                )
            }

        }
        val btnLogout = findViewById<Button>(R.id.btnLogout)
        btnLogout.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

    }
}