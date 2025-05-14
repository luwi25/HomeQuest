package com.android.homequest

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.ListView
import android.widget.Toast
import com.android.homequest.Adapter.ChildrenListAdapter
import com.android.homequest.RC.RetrofitClient
import com.android.homequest.model.Children
import com.android.homequest.model.Relationship
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class AssignTaskActivity : Activity() {
    private lateinit var listView: ListView
    private lateinit var childAdapter: ChildrenListAdapter
    private var childList: List<Relationship> = emptyList()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_assign_task)

        listView = findViewById(R.id.listview)
        val sharedPreferences = getSharedPreferences("UserSession", MODE_PRIVATE)
        val ParentEmail = sharedPreferences.getString("parentEmail", "default@email.com")
        val ParentFirstname = sharedPreferences.getString("parentFirstname", "Default Firstname")
        val ChildFirstname = sharedPreferences.getString("childFirstname", "Default Firstname")
        val ChildEmail = sharedPreferences.getString("childEmail", "default@email.com")


        RetrofitClient.instance.searchRelationship(parentFirstname = ParentFirstname, parentEmail = ParentEmail)
            .enqueue(object : Callback<List<Relationship>> {
                override fun onResponse(call: Call<List<Relationship>>, response: Response<List<Relationship>>) {
                    if (response.isSuccessful) {
                        // Fetch the user data from the response
                        childList = response.body() ?: emptyList()


                        // Initialize the adapter with the full list of users
                        childAdapter = ChildrenListAdapter(
                            this@AssignTaskActivity,
                            childList,
                            onClick = {children ->

                                val sharedPreferences = getSharedPreferences("UserSession", MODE_PRIVATE)
                                val editor = sharedPreferences.edit()
                                editor.putString("childFirstname", children.childFirstname)
                                editor.putString("childEmail", children.childEmail)
                                editor.commit()

                                startTask()


                            },
                            onLongCLick = {children ->
                                Toast.makeText(this@AssignTaskActivity, "${children.parentFirstname}", Toast.LENGTH_SHORT).show()

                            })
                        listView.adapter = childAdapter

                    } else {
                        // Log error if response is not successful
                        Toast.makeText(this@AssignTaskActivity, "Error: ${response.code()}", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<List<Relationship>>, t: Throwable) {
                    // Handle failure case
                    Toast.makeText(this@AssignTaskActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
                }
            })

        val buttonback = findViewById<ImageButton>(R.id.buttonback)
        buttonback.setOnClickListener {
            startActivity(
                Intent(this, ParentDashboardActivity::class.java)
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
    private fun startTask()
    {
        startActivity(
            Intent(this, TaskActivity::class.java)
        )
    }
}