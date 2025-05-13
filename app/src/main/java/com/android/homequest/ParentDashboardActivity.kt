package com.android.homequest

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.PopupMenu
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.fragment.app.FragmentActivity

class ParentDashboardActivity : FragmentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_parent_dashboard)

        val imagebutton_addtask = findViewById<LinearLayout>(R.id.imagebutton_addtask)
        imagebutton_addtask.setOnClickListener {
            startActivity(
                Intent(this, AssignTaskActivity::class.java)
            )
        }


        //popup menu task
        val ibutton_moretask = findViewById<ImageView>(R.id.ibutton_moretask)

        ibutton_moretask.setOnClickListener {
            val popupMenu = PopupMenu(this, ibutton_moretask)
            popupMenu.menuInflater.inflate(R.menu.popup_menu, popupMenu.menu)

            popupMenu.setOnMenuItemClickListener { item ->
                when (item.itemId) {
                    R.id.option_edit -> {
                        startActivity(
                            Intent(this, DeleteTaskActivity::class.java)
                        )
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
                        startActivity(
                            Intent(this, ProfileActivity::class.java)
                        )
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

        //pop menu reward
        val ibutton_morereward = findViewById<ImageView>(R.id.ibutton_morereward)

        ibutton_morereward.setOnClickListener {
            val popupMenu = PopupMenu(this, ibutton_morereward)
            popupMenu.menuInflater.inflate(R.menu.popupreward_menu, popupMenu.menu)

            popupMenu.setOnMenuItemClickListener { item ->
                when (item.itemId) {
                   
                    R.id.option_delete -> {
                        startActivity(
                            Intent(this, DeleteRewardActivity::class.java)
                        )
                        true
                    }
                    else -> false
                }
            }

            popupMenu.show()
        }

        val cv_family = findViewById<CardView>(R.id.cv_family)
        cv_family.setOnClickListener {
            startActivity(
                Intent(this, ChildrenListActivity::class.java)
            )
        }

        val cv_task = findViewById<CardView>(R.id.cv_task)
        cv_task.setOnClickListener {
            startActivity(
                Intent(this, TaskListActivity::class.java)
            )
        }

        val cv_viewreward = findViewById<CardView>(R.id.cv_viewreward)
        cv_viewreward.setOnClickListener {
            startActivity(
                Intent(this, RewardListActivity::class.java)
            )
        }

        val iv_familyoption = findViewById<ImageView>(R.id.iv_familyoption)
        iv_familyoption.setOnClickListener {
            val popupMenu = PopupMenu(this, iv_familyoption)
            popupMenu.menuInflater.inflate(R.menu.popup_parentfam, popupMenu.menu)

            popupMenu.setOnMenuItemClickListener { item ->
                when (item.itemId) {
                    R.id.option_add -> {
                        startActivity(
                            Intent(this, RoleChildrenActivity::class.java)
                        )

                        true
                    }
                    R.id.option_delete -> {
                        startActivity(
                            Intent(this, DeleteChildActivity::class.java)
                        )

                        true
                    }
                    else -> false
                }
            }

            popupMenu.show()
        }

        val settings = findViewById<LinearLayout>(R.id.settings)
        settings.setOnClickListener {
            startActivity(
                Intent(this, SettingsActivity::class.java)
            )
        }
        val ib_ct = findViewById<ImageButton>(R.id.ib_ct)
        ib_ct.setOnClickListener {
            startActivity(
                Intent(this, AssignTaskActivity::class.java)
            )
        }

        val tv_ct = findViewById<TextView>(R.id.tv_ct)
        tv_ct.setOnClickListener {
            startActivity(
                Intent(this, AssignTaskActivity::class.java)
            )
        }


        val ib_settings = findViewById<ImageButton>(R.id.ib_settings)
        ib_settings.setOnClickListener {
            startActivity(
                Intent(this, SettingsActivity::class.java)
            )
        }

        val tv_settings = findViewById<TextView>(R.id.tv_settings)
        tv_settings.setOnClickListener {
            startActivity(
                Intent(this, SettingsActivity::class.java)
            )
        }

        // ✅ Google Map overlay click
        val mapOverlay = findViewById<View>(R.id.map_overlay_clickable)
        mapOverlay.setOnClickListener {
            startActivity(Intent(this, FullMapActivity::class.java))
        }
    }
}