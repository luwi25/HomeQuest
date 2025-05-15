package com.android.homequest

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.util.TypedValue
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
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

        val edittext_email = findViewById<EditText>(R.id.edittext_email)
        val edittext_password = findViewById<EditText>(R.id.edittext_password)

        val drawable = ContextCompat.getDrawable(this, R.drawable.email)
        val drawablePassword = ContextCompat.getDrawable(this, R.drawable.password)

        // Convert dp to pixels
        val sizeInDp = 25
        val sizeInPx = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            sizeInDp.toFloat(),
            resources.displayMetrics
        ).toInt()

        // Set bounds (this resizes the drawable)
        drawable?.setBounds(0, 0, sizeInPx, sizeInPx)
        drawablePassword?.setBounds(0, 0, sizeInPx, sizeInPx)

        // Apply the drawable to the end of the EditText
        edittext_email.setCompoundDrawablesRelative(null, null, drawable, null)
        edittext_password.setCompoundDrawablesRelative(null, null, drawablePassword, null)




        var Email = ""
        var Password  = ""
        var Role = ""

        val sharedPreferences = getSharedPreferences("UserSession", MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString("screen", "Login")
        editor.commit()



        val button_login = findViewById<Button>(R.id.button_login)
        button_login.setOnClickListener {



            val email = edittext_email.text.toString()
            val password = edittext_password.text.toString()

            if(email.isNullOrEmpty() || password.isNullOrEmpty())
            {
                Toast.makeText(this, "Username or password is empty", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

//            val sharedPreferences = getSharedPreferences("UserSession", MODE_PRIVATE)
//            val editor = sharedPreferences.edit()



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
                            editor.putString("parentID", user.id)
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
                            editor.putString("childLastname", user.lastname)
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