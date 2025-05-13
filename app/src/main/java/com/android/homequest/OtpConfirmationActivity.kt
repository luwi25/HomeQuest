package com.android.homequest

import android.app.Activity
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
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class OtpConfirmationActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_otp_confirmation)

        val sharedPreferences = getSharedPreferences("UserSession", MODE_PRIVATE)

        var otp = sharedPreferences.getString("otp", "Default")
        var emailToSend = sharedPreferences.getString("sendToEmail", "Default")



        val part1 = findViewById<EditText>(R.id.et1).text.toString()
        val part2 = findViewById<EditText>(R.id.et2).text.toString()
        val part3 = findViewById<EditText>(R.id.et3).text.toString()
        val part4 = findViewById<EditText>(R.id.et4).text.toString()
        val part5 = findViewById<EditText>(R.id.et5).text.toString()
        val part6 = findViewById<EditText>(R.id.et6).text.toString()

        val combinedOTP = part1 + part2 + part3 + part4 + part5 + part6

        val button_verify = findViewById<Button>(R.id.button_verify)
        button_verify.setOnClickListener {

            if(otp == combinedOTP)
            {
                Log.d("API", "Otp: ${otp}")
            }


        }

        val tv_resend = findViewById<TextView>(R.id.tv_resend)
        tv_resend.setOnClickListener {

            val otpRequest = OtpRequest(
                to = emailToSend.toString()
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