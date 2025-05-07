package com.android.homequest

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.PopupMenu
import android.widget.Toast
import androidx.cardview.widget.CardView


class ChildDashboardActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_child_dashboard)

        val cv_todotask = findViewById<CardView>(R.id.cv_todotask)
        cv_todotask.setOnClickListener {
            startActivity(
                Intent(this, ToDoTaskActivity::class.java)
            )
        }

        val cv_viewreward = findViewById<CardView>(R.id.cv_viewreward)
        cv_viewreward.setOnClickListener {
            startActivity(
                Intent(this, ClaimRewardListActivity::class.java)
            )
        }

        val cv_seefamily = findViewById<CardView>(R.id.cv_seefamily)
        cv_seefamily.setOnClickListener {
            startActivity(
                Intent(this, FamilyListActivity::class.java)
            )
        }

        val iv_familyoption = findViewById<ImageView>(R.id.iv_familyoption)
        iv_familyoption.setOnClickListener {
            val popupMenu = PopupMenu(this, iv_familyoption)
            popupMenu.menuInflater.inflate(R.menu.popup_menu, popupMenu.menu)

            popupMenu.setOnMenuItemClickListener { item ->
                when (item.itemId) {
                    R.id.option_edit -> {
                        Toast.makeText(this, "Edit clicked", Toast.LENGTH_SHORT).show()
                        true
                    }
                    R.id.option_delete -> {
                        Toast.makeText(this, "Delete clicked", Toast.LENGTH_SHORT).show()
                        true
                    }
                    else -> false
                }
            }

            popupMenu.show()
        }

        val button_profile = findViewById<ImageButton>(R.id.button_profile)

        button_profile.setOnClickListener {
            val popupMenu = PopupMenu(this, button_profile)
            popupMenu.menuInflater.inflate(R.menu.popup_profile, popupMenu.menu)

            popupMenu.setOnMenuItemClickListener { item ->
                when (item.itemId) {
                    R.id.option_profile -> {
                        Toast.makeText(this, "Edit clicked", Toast.LENGTH_SHORT).show()
                        true
                    }
                    R.id.option_logout -> {
                        startActivity(
                            Intent(this, LogoutActivity::class.java)
                        )
                        true
                    }
                    else -> false
                }
            }

            popupMenu.show()
        }


    }
}