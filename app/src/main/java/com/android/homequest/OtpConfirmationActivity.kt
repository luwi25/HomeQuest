package com.android.homequest

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.android.homequest.RC.RetrofitClient
import com.android.homequest.model.ApiResponse
import com.android.homequest.model.OtpRequest
import com.android.homequest.model.User
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class OtpConfirmationActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_otp_confirmation)

        val sharedPreferences = getSharedPreferences("UserSession", MODE_PRIVATE)


        // Retrieve the data from the Intent
        val lastname = intent.getStringExtra("lastname")
        val firstname = intent.getStringExtra("firstname")
        val email = intent.getStringExtra("email")
        val role = intent.getStringExtra("role")
        val password = intent.getStringExtra("password")
        val otp = intent.getStringExtra("otp")


        val sentence = findViewById<TextView>(R.id.sentence)
        sentence.setText("Enter the verification code we just sent to your email: ${email}")


        val button_verify = findViewById<Button>(R.id.button_verify)
        button_verify.setOnClickListener {

            val part1 = findViewById<EditText>(R.id.et1).text.toString()
            val part2 = findViewById<EditText>(R.id.et2).text.toString()
            val part3 = findViewById<EditText>(R.id.et3).text.toString()
            val part4 = findViewById<EditText>(R.id.et4).text.toString()
            val part5 = findViewById<EditText>(R.id.et5).text.toString()
            val part6 = findViewById<EditText>(R.id.et6).text.toString()

            val combinedOTP = part1 + part2 + part3 + part4 + part5 + part6





            if(otp == combinedOTP)
            {


                // Create a user
                val user = User(
                    lastname = lastname.toString(),
                    firstname = firstname.toString(),
                    role = role.toString(),
                    email = email.toString(),
                    password = password.toString()
                )

                // POST request
                RetrofitClient.instance.createUser(user).enqueue(object : Callback<User> {
                    override fun onResponse(call: Call<User>, response: Response<User>) {
                        if (response.isSuccessful) {
                            // User created successfully
                            Log.d("API", "User Created: ${response.body()}")
                            Toast.makeText(this@OtpConfirmationActivity, "Registered Successfully", Toast.LENGTH_SHORT).show()
                        } else if (response.code() == 409) {
                            // Handle duplicate email error
                            Toast.makeText(this@OtpConfirmationActivity, "Email already exists. Please use another email.", Toast.LENGTH_SHORT).show()
                            startActivity(
                                Intent(this@OtpConfirmationActivity, RegisterActivity::class.java).apply {
                                    putExtra("firstname", firstname)
                                    putExtra("lastname", lastname)
                                    putExtra("role", role)
                                }
                            )
                        } else {
                            // Handle other errors
                            Log.e("API", "Error: ${response.code()} - ${response.message()}")
                            Toast.makeText(this@OtpConfirmationActivity, "Something went wrong. Please try again.", Toast.LENGTH_SHORT).show()
                        }
                    }

                    override fun onFailure(call: Call<User>, t: Throwable) {
                        Log.e("API", "Error: ${t.message}")
                    }
                })
            }
            else
            {
                Toast.makeText(this, "Invalid OTP. Click Resend", Toast.LENGTH_SHORT).show()
                return@setOnClickListener

            }


        }

        val tv_resend = findViewById<TextView>(R.id.tv_resend)
        tv_resend.setOnClickListener {

            val otpRequest = OtpRequest(
                to = email.toString()
            )

            RetrofitClient.instance.sendOtp(otpRequest).enqueue(object : retrofit2.Callback<ApiResponse> {
                override fun onResponse(call: Call<ApiResponse>, response: retrofit2.Response<ApiResponse>) {
                    if (response.isSuccessful) {
                        val otp = response.body()?.otp
                        Log.d("API", "OTP Sent: ${response.body()?.message}")
                        Log.d("API", "OTP Generated: $otp")


                    } else {
                        Log.e("API", "Error: ${response.code()} - ${response.errorBody()?.string()}")
                    }
                }

                override fun onFailure(call: Call<ApiResponse>, t: Throwable) {
                    Log.e("API", "Error: ${t.message}")
                }
            })

        }

    }
}