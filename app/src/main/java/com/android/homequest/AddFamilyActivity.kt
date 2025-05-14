package com.android.homequest

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.Toast
import com.android.homequest.RC.RetrofitClient
import com.android.homequest.model.Relationship
import com.android.homequest.model.TaskAssignment
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AddFamilyActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_family)

        val et_firstname = findViewById<EditText>(R.id.et_firstname)
        val et_lastname = findViewById<EditText>(R.id.et_lastname)
        val et_email = findViewById<EditText>(R.id.et_email)

        val sharedPreferences = getSharedPreferences("UserSession", MODE_PRIVATE)

        val ChildFirstname = sharedPreferences.getString("childFirstname", "Default Firstname")
        val ChildLastname = sharedPreferences.getString("childLastname", "Default Lastname")
        val ChildEmail = sharedPreferences.getString("childEmail", "default@email.com")
        val ParentEmail = sharedPreferences.getString("parentEmail", "default@email.com")
        val ParentFirstname = sharedPreferences.getString("parentFirstname", "Default Firstname")


        et_firstname.setText(ChildFirstname)
        et_lastname.setText(ChildLastname)
        et_email.setText(ChildEmail)

        val button_addtofamily = findViewById<Button>(R.id.button_addtofamily)
        button_addtofamily.setOnClickListener {

            val relations = Relationship(
                parentFirstname = ParentFirstname.toString(),
                parentEmail = ParentEmail.toString(),
                childFirstname = ChildFirstname.toString(),
                childLastname = ChildLastname.toString(),
                childEmail = ChildEmail.toString()
            )

            RetrofitClient.instance.createRelationship(relations).enqueue(object : Callback<Relationship> {
                override fun onResponse(call: Call<Relationship>, response: Response<Relationship>) {
                    Log.d("API", "onResponse called")
                    if (response.isSuccessful) {
                        // Handle the success response
                        Log.d("API", "Task Assigned: ${response.body()}")
                    } else {
                        // Handle error response
                        Log.e("API", "Error: ${response.code()} - ${response.errorBody()?.string()}")
                    }
                }

                override fun onFailure(call: Call<Relationship>, t: Throwable) {
                    // Handle failure
                    Log.e("API", "Error: ${t.message}")
                }
            })

            Toast.makeText(this, "Add To Family Successfully", Toast.LENGTH_SHORT).show()
            startActivity(
                Intent(this, ParentDashboardActivity::class.java)
            )
        }

        val button_back = findViewById<ImageButton>(R.id.button_back)
        button_back.setOnClickListener {
            startActivity(
                Intent(this, RoleChildrenActivity::class.java)
            )
        }

        val home = findViewById<LinearLayout>(R.id.home)
        home.setOnClickListener {
            val sharedPreferences = getSharedPreferences("UserSession", MODE_PRIVATE)
            val role = sharedPreferences.getString("role", "Default")

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

        val settings = findViewById<LinearLayout>(R.id.settings)
        settings.setOnClickListener {
            startActivity(
                Intent(this, SettingsActivity::class.java)
            )
        }

        val addtask = findViewById<LinearLayout>(R.id.imagebutton_addtask)
        addtask.setOnClickListener {
            startActivity(
                Intent(this, AssignTaskActivity::class.java)
            )
        }
    }
}