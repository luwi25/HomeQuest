package com.android.homequest

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Spinner
import android.widget.Toast
import com.android.homequest.RC.RetrofitClient
import com.android.homequest.model.User
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegisterActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        val edittext_password = findViewById<EditText>(R.id.edittext_password)
        val edittext_confirmpassword = findViewById<EditText>(R.id.edittext_confirmpassword)
        val edittext_lastname = findViewById<EditText>(R.id.edittext_lastname)
        val edittext_firstname = findViewById<EditText>(R.id.edittext_firstname)
        val edittext_email = findViewById<EditText>(R.id.edittext_email)
        val button_register = findViewById<Button>(R.id.button_register)

        val spinner: Spinner = findViewById(R.id.spinnerExample)

        val options = listOf("Select Role Option",  "Parent", "Child")

        val adapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item,
            options
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter

        // Optional: Listen for item selection
        spinner.setOnItemSelectedListener(object : android.widget.AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: android.widget.AdapterView<*>,
                view: android.view.View,
                position: Int,
                id: Long
            ) {
                val selectedItem = options[position]
                Toast.makeText(this@RegisterActivity, "Selected: $selectedItem", Toast.LENGTH_SHORT).show()
            }

            override fun onNothingSelected(parent: android.widget.AdapterView<*>) {
                // Handle no selection if needed
            }
        })


        button_register.setOnClickListener {
            val lastname = edittext_lastname.text.toString()
            val firstname = edittext_firstname.text.toString()
            val email = edittext_email.text.toString()
            val password = edittext_password.text.toString()
            val confirmpassword = edittext_confirmpassword.text.toString()
            val selectedRole = spinner.selectedItem.toString()

            var role: String = ""

            if (selectedRole == "Select Role Option")
            {
                Toast.makeText(this, "Please select an option", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            else if (selectedRole == "Parent")
            {
                role = "Parent"
            }
            else if (selectedRole == "Child")
            {
                role = "Child"
            }

            // Create a user
            val user = User(
                lastname = lastname,
                firstname = firstname,
                role = role,
                email = email,
                password = password
            )

            // POST request
            RetrofitClient.instance.createUser(user).enqueue(object : Callback<User> {
                override fun onResponse(call: Call<User>, response: Response<User>) {
                    if (response.isSuccessful) {
                        Log.d("API", "User Created: ${response.body()}")
                    }
                }

                override fun onFailure(call: Call<User>, t: Throwable) {
                    Log.e("API", "Error: ${t.message}")
                }
            })

            startActivity(
                Intent(this, LoginActivity::class.java).apply {
                    putExtra("email", email)
                    putExtra("password", password)
                    putExtra("role", role)

                }
            )
        }

        val button_back = findViewById<ImageButton>(R.id.button_back)
        button_back.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }


    }
}