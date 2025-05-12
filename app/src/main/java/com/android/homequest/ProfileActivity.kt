package com.android.homequest

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton



class ProfileActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        val sharedPreferences = getSharedPreferences("UserSession", MODE_PRIVATE)
        val role = sharedPreferences.getString("role", "Default")

        val et_firstname = findViewById<EditText>(R.id.firstName)
        val et_lastname = findViewById<EditText>(R.id.lastName)
        val et_email = findViewById<EditText>(R.id.email)

        var getFirstname: String = ""
        var getLastname: String = ""

        val button_save = findViewById<Button>(R.id.button_save)
        val button_cancel = findViewById<Button>(R.id.button_cancel)
        button_cancel.setOnClickListener {

            et_firstname.setText(getFirstname)
            et_lastname.setText(getLastname)

            button_cancel.visibility = View.GONE
            button_save.visibility = View.GONE

            et_lastname.isFocusable = false
            et_lastname.isFocusableInTouchMode = false
            et_lastname.isCursorVisible = false
            et_lastname.isClickable = false
            et_lastname.isEnabled = false
            et_lastname.setTextColor(Color.parseColor("#808080"))
            et_lastname.setBackgroundColor(Color.parseColor("#F0F0F0"))

            et_firstname.isFocusable = false
            et_firstname.isFocusableInTouchMode = false
            et_firstname.isCursorVisible = false
            et_firstname.isClickable = false
            et_firstname.isEnabled = false
            et_firstname.setTextColor(Color.parseColor("#808080"))
            et_firstname.setBackgroundColor(Color.parseColor("#F0F0F0"))
        }

        val buttonback = findViewById<ImageButton>(R.id.buttonback)
        buttonback.setOnClickListener {

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

        val button_edit = findViewById<ImageButton>(R.id.button_edit)
        button_edit.setOnClickListener {

            button_cancel.visibility = View.VISIBLE
            button_save.visibility = View.VISIBLE

            et_lastname.isFocusable = true
            et_lastname.isFocusableInTouchMode = true
            et_lastname.isCursorVisible = true
            et_lastname.isClickable = true
            et_lastname.isEnabled = true
            et_lastname.setTextColor(Color.BLACK)
            et_lastname.setText("")
            et_lastname.setBackgroundResource(R.drawable.roundtext)

            et_firstname.isFocusable = true
            et_firstname.isFocusableInTouchMode = true
            et_firstname.isCursorVisible = true
            et_firstname.isClickable = true
            et_firstname.isEnabled = true
            et_firstname.setTextColor(Color.BLACK)
            et_firstname.setText("")
            et_firstname.setBackgroundResource(R.drawable.roundtext)

            et_firstname.requestFocus()
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.showSoftInput(et_firstname, InputMethodManager.SHOW_IMPLICIT)
        }

        if(role == "Parent")
        {
            val firstname = sharedPreferences.getString("parentFirstname", "Default first name")
            val lastname = sharedPreferences.getString("parentLastname", "Default last name")
            val email = sharedPreferences.getString("parentEmail", "Default email")

            et_firstname.setText(firstname)
            et_lastname.setText(lastname)
            et_email.setText(email)

            getFirstname = et_firstname.text.toString()
            getLastname = et_lastname.text.toString()
        }
        else if(role == "Child")
        {
            val firstname = sharedPreferences.getString("childFirstname", "Default first name")
            val lastname = sharedPreferences.getString("childLastname", "Default last name")
            val email = sharedPreferences.getString("childEmail", "Default email")

            et_firstname.setText(firstname)
            et_lastname.setText(lastname)
            et_email.setText(email)

            getFirstname = et_firstname.text.toString()
            getLastname = et_lastname.text.toString()
        }




    }
}