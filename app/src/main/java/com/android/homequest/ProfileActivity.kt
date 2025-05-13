package com.android.homequest

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import com.android.homequest.RC.RetrofitClient
import com.android.homequest.model.ChildUpdateRequest
import com.android.homequest.model.ChildUpdateResponse
import com.android.homequest.model.ParentFirstnameUpdateRequest
import com.android.homequest.model.Relationship
import com.android.homequest.model.TaskAssignUpdateRequest
import com.android.homequest.model.TaskAssignmentResponse
import com.android.homequest.model.UpdateParentResponse
import com.android.homequest.model.UpdateUserRequest
import com.android.homequest.model.UpdateUserResponse
import com.android.homequest.model.User
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class ProfileActivity : Activity() {

    var oldChildEmail: String = ""
    var oldChildFirstname: String = ""
    var oldChildLastname: String = ""
    var newChildFirstname: String = ""
    var newChildLastname: String = ""
    var oldParentFirstname: String = ""
    var currentParentEmail: String = ""
    var newParentFirstname: String = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        val sharedPreferences = getSharedPreferences("UserSession", MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        val role = sharedPreferences.getString("role", "Default")

        val et_firstname = findViewById<EditText>(R.id.firstName)
        val et_lastname = findViewById<EditText>(R.id.lastName)
        val et_email = findViewById<EditText>(R.id.email)

        var getFirstname: String = ""
        var getLastname: String = ""
        var getID = ""
        var updatedfirstname = ""
        var updatedlastname = ""


        if(role == "Parent")
        {
            val firstname = sharedPreferences.getString("parentFirstname", "Default first name")
            val lastname = sharedPreferences.getString("parentLastname", "Default last name")
            val email = sharedPreferences.getString("parentEmail", "Default email")
            val id = sharedPreferences.getString("parentID", "Default ID")

            getID = id.toString()
            oldParentFirstname = firstname.toString()
            currentParentEmail = email.toString()





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
            val id = sharedPreferences.getString("childID", "Default ID")

            getID = id.toString()

            oldChildEmail = email.toString()
            oldChildFirstname = firstname.toString()
            oldChildLastname = lastname.toString()

            et_firstname.setText(firstname)
            et_lastname.setText(lastname)
            et_email.setText(email)

            getFirstname = et_firstname.text.toString()
            getLastname = et_lastname.text.toString()
        }

        val button_cancel = findViewById<Button>(R.id.button_cancel)

        val button_save = findViewById<Button>(R.id.button_save)
        button_save.setOnClickListener {
            val updateRequest = UpdateUserRequest(
                firstname = et_firstname.text.toString(),
                lastname = et_lastname.text.toString()
            )

            RetrofitClient.instance.updateUser(getID, updateRequest)
                .enqueue(object : Callback<UpdateUserResponse> {
                    override fun onResponse(
                        call: Call<UpdateUserResponse>,
                        response: Response<UpdateUserResponse>
                    ) {
                        if (response.isSuccessful)
                        {
                            Log.d("API", "User updated: ${response.body()?.user}")
                            RetrofitClient.instance.getUserById(getID).enqueue(object : Callback<User> {
                                override fun onResponse(call: Call<User>, response: Response<User>) {
                                    Log.d("API", "onResponse called")
                                    if (response.isSuccessful) {
                                        // Handle the success response
                                        val user = response.body()
                                        Log.d("API", "getUserById() - Response is successful")



                                        if (user != null) {
                                            val firstNameNew = user.firstname
                                            val lastNameNew = user.lastname

                                            updatedfirstname = firstNameNew
                                            updatedlastname = lastNameNew

                                            button_cancel.visibility = View.GONE
                                            button_save.visibility = View.GONE

                                            et_firstname.setText(updatedfirstname)
                                            et_lastname.setText(updatedlastname)

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

                                            if(role == "Parent")
                                            {
                                                editor.putString("parentFirstname", updatedfirstname)
                                                editor.putString("parentLastname", updatedlastname)
                                                editor.commit()

                                                newParentFirstname = updatedfirstname
                                                updateParent()
                                            }
                                            else if(role == "Child")
                                            {
                                                editor.putString("childFirstname", updatedfirstname)
                                                editor.putString("childLastname", updatedlastname)
                                                editor.commit()

                                                newChildFirstname = updatedfirstname
                                                newChildLastname = updatedlastname

                                                updatechild()
                                                updateTask()
                                            }

                                            Log.d("API", "${user.firstname} & ${user.lastname}")


                                            // You can now use the firstName and lastName as needed
                                        }
                                    } else {
                                        // Handle error response
                                        Log.e("API", "Error: ${response.code()} - ${response.errorBody()?.string()}")
                                    }
                                }

                                override fun onFailure(call: Call<User>, t: Throwable) {
                                    // Handle failure
                                    Log.e("API", "Error: ${t.message}")
                                }
                            })

                        } else {
                            Log.e("API", "Update failed: ${response.code()} - ${response.errorBody()?.string()}")
                        }
                    }

                    override fun onFailure(call: Call<UpdateUserResponse>, t: Throwable) {
                        Log.e("API", "Error: ${t.message}")
                    }
                })





        }



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

    }
    fun updatechild()
    {
        val request = ChildUpdateRequest(
            childEmail = oldChildEmail,
            newChildFirstname = newChildFirstname,
            newChildLastname = newChildLastname
        )

        Log.d("API", "${oldChildFirstname}, ${oldChildEmail}, ${newChildFirstname}, ${newChildLastname}")

        RetrofitClient.instance.updateChild(request).enqueue(object : Callback<ChildUpdateResponse> {
            override fun onResponse(
                call: Call<ChildUpdateResponse>,
                response: Response<ChildUpdateResponse>
            ) {
                Log.d("API", "onResponse called")
                if (response.isSuccessful) {
                    // Handle success response
                    val updatedRelationship = response.body()?.data
                    Log.d("API", "Child updated successfully: $updatedRelationship")
                } else {
                    // Handle error response
                    Log.e("API", "Error ${response.code()}: ${response.errorBody()?.string()}")
                }
            }

            override fun onFailure(call: Call<ChildUpdateResponse>, t: Throwable) {
                // Handle failure
                Log.e("API", "Failure: ${t.message}")
            }
        })
    }

    fun updateParent()
    {
        val request = ParentFirstnameUpdateRequest(
            parentEmail = currentParentEmail,
            newParentFirstname = newParentFirstname
        )

        RetrofitClient.instance.updateParentFirstname(request).enqueue(object : Callback<UpdateParentResponse> {
            override fun onResponse(call: Call<UpdateParentResponse>, response: Response<UpdateParentResponse>) {
                if (response.isSuccessful) {
                    Log.d("API", "Parent firstname updated: ${response.body()}")
                } else {
                    Log.e("API", "Update failed: ${response.code()} - ${response.errorBody()?.string()}")
                }
            }

            override fun onFailure(call: Call<UpdateParentResponse>, t: Throwable) {
                Log.e("API", "Error: ${t.message}")
            }
        })

    }

    fun updateTask()
    {
        val request = TaskAssignUpdateRequest(
            assignToEmail = oldChildEmail,
            newAssignTo = newChildFirstname
        )

        RetrofitClient.instance.updateAssignTo(request).enqueue(object : Callback<TaskAssignmentResponse> {
            override fun onResponse(call: Call<TaskAssignmentResponse>, response: Response<TaskAssignmentResponse>) {
                Log.d("API", "onResponse called")
                if (response.isSuccessful) {
                    val result = response.body()
                    Log.d("API", "Message: ${result?.message}")
                    Log.d("API", "Updated Task: ${result?.data}")
                } else {
                    Log.e("API", "Error: ${response.code()} - ${response.errorBody()?.string()}")
                }
            }

            override fun onFailure(call: Call<TaskAssignmentResponse>, t: Throwable) {
                Log.e("API", "Network failure: ${t.message}")
            }
        })
    }
}