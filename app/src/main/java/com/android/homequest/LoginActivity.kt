package com.android.homequest

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.android.homequest.Adapter.TaskAssignmentAdapter
import com.android.homequest.RC.RetrofitClient
import com.android.homequest.model.ApiResponse
import com.android.homequest.model.LoginRequest
import com.android.homequest.model.LoginResponse
import com.android.homequest.model.OtpRequest
import com.android.homequest.model.TaskAssignment
import com.android.homequest.model.User
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.POST

class LoginActivity : Activity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)




        var Email = ""
        var Password  = ""
        var Role = ""

        val otpRequest = OtpRequest(
            to = "rabbihernaez@gmail.com" // Replace with the user's email address
        )

        RetrofitClient.instance.sendOtp(otpRequest).enqueue(object : retrofit2.Callback<ApiResponse> {
            override fun onResponse(call: Call<ApiResponse>, response: retrofit2.Response<ApiResponse>) {
                if (response.isSuccessful) {
                    val otp = response.body()?.otp
                    Log.d("API", "OTP Sent: ${response.body()?.message}")
                    Log.d("API", "OTP Generated: $otp")

                    // Store OTP locally for verification later
                    // Store the OTP value (or in SharedPreferences)
                } else {
                    Log.e("API", "Error: ${response.code()} - ${response.errorBody()?.string()}")
                }
            }

            override fun onFailure(call: Call<ApiResponse>, t: Throwable) {
                Log.e("API", "Error: ${t.message}")
            }
        })

        intent?.let {
            it.getStringExtra("email")?.let { email  ->
                Email = email
            }
            it.getStringExtra("password")?.let { password  ->
                Password = password
            }
            it.getStringExtra("role")?.let { role  ->
                Role = role
            }
        }

        val button_login = findViewById<Button>(R.id.button_login)
        button_login.setOnClickListener {

            val edittext_email = findViewById<EditText>(R.id.edittext_email)
            val edittext_password = findViewById<EditText>(R.id.edittext_password)

            val email = edittext_email.text.toString()
            val password = edittext_password.text.toString()

            val sharedPreferences = getSharedPreferences("UserSession", MODE_PRIVATE)
            val editor = sharedPreferences.edit()

// Create the login request object
            val loginRequest = LoginRequest(email, password)

            RetrofitClient.instance.login(loginRequest).enqueue(object : Callback<LoginResponse> {
                override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                    if (response.isSuccessful) {
                        // Handle the successful login response
                        val loginResponse = response.body()
                        val user = loginResponse?.user
                        val role = user?.role



                        if(role == "Parent")
                        {
                            editor.putString("parentFirstname", user.firstname)
                            editor.putString("parentLastname", user.lastname)
                            editor.putString("parentEmail", user.email)
                            editor.putString("role", "Parent")
                            editor.commit()

                            startParent()
                        }
                        else if(role == "Child")
                        {
                            editor.putInt("childPoints", user.points ?: 0)
                            editor.putString("childID", user.id)
                            editor.putString("childFirstname", user.firstname)
                            editor.putString("childEmail", user.email)
                            editor.putString("role", "Child")
                            editor.commit()
                            Toast.makeText(this@LoginActivity, "${user.firstname}", Toast.LENGTH_SHORT).show()
                            startChild()
                        }
                        Log.d("API", "Login successful: ${user?.firstname} ${user?.lastname}")
                        // Proceed with storing user data or navigate to another screen
                    } else {
                        // Handle the error response
                        Log.e("API", "Error: ${response.code()} - ${response.errorBody()?.string()}")
                        Toast.makeText(this@LoginActivity, "Invalid email or password", Toast.LENGTH_SHORT).show()
                        return
                    }
                }

                override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                    // Handle failure
                    Log.e("API", "Error: ${t.message}")
                    Toast.makeText(this@LoginActivity, "Login failed. Please try again.", Toast.LENGTH_SHORT).show()
                }
            })



        }

        val button_register = findViewById<TextView>(R.id.button_register)
        button_register.setOnClickListener {

            startActivity(
                Intent(this, RegisterActivity::class.java)
            )

        }


    }
    fun startParent()
    {
        startActivity(
            Intent(this, ParentDashboardActivity::class.java)
        )
    }
    fun startChild()
    {
        startActivity(
            Intent(this, ChildDashboardActivity::class.java)
        )
    }
}