package com.android.homequest

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.ListView
import android.widget.Toast
import com.android.homequest.Adapter.ChildrenListAdapter
import com.android.homequest.Adapter.RewardListAdapter
import com.android.homequest.Adapter.RoleChildAdapter
import com.android.homequest.RC.RetrofitClient
import com.android.homequest.model.Relationship
import com.android.homequest.model.Reward
import com.android.homequest.model.User
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ChildrenListActivity : Activity() {

    private lateinit var listView: ListView
    private lateinit var childAdapter: ChildrenListAdapter
    private var childList: List<Relationship> = emptyList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_children_list)

        listView = findViewById(R.id.listview)
        val sharedPreferences = getSharedPreferences("UserSession", MODE_PRIVATE)
        val ParentEmail = sharedPreferences.getString("parentEmail", "default@email.com")
        val ParentFirstname = sharedPreferences.getString("parentFirstname", "Default Firstname")

        RetrofitClient.instance.searchRelationship(parentFirstname = ParentFirstname,
            parentEmail = ParentEmail,
            childFirstname = null,
            childEmail = null)
            .enqueue(object : Callback<List<Relationship>> {
                override fun onResponse(call: Call<List<Relationship>>, response: Response<List<Relationship>>) {
                    if (response.isSuccessful) {
                        // Fetch the user data from the response
                        childList = response.body() ?: emptyList()

                        if(childList.isNotEmpty()) {

                            // Initialize the adapter with the full list of users
                            childAdapter = ChildrenListAdapter(
                                this@ChildrenListActivity,
                                childList,
                                onClick = { children ->

                                    Toast.makeText(
                                        this@ChildrenListActivity,
                                        "${children.childFirstname}",
                                        Toast.LENGTH_SHORT
                                    ).show()


                                },
                                onLongCLick = { children ->
                                    Toast.makeText(
                                        this@ChildrenListActivity,
                                        "${children.parentFirstname}",
                                        Toast.LENGTH_SHORT
                                    ).show()

                                })
                            listView.adapter = childAdapter
                        }
                        else
                        {
                            val emptyChildList = listOf(
                                Relationship(
                                    parentFirstname = "Not Available",
                                    parentEmail = "Not Available",
                                    childFirstname = "No Child Available",
                                    childLastname = "Not Available",
                                    childEmail = "Not Available",
                                )
                            )
                            val emptyAdapter = ChildrenListAdapter(
                                this@ChildrenListActivity,
                                emptyChildList,
                                onClick = { children ->

                                    Toast.makeText(
                                        this@ChildrenListActivity,
                                        "You have no child/children yet\nAdd your child on the home screen",
                                        Toast.LENGTH_SHORT
                                    ).show()


                                },
                                onLongCLick = { children ->
                                    Toast.makeText(
                                        this@ChildrenListActivity,
                                        "Add your child/children on the dashboard",
                                        Toast.LENGTH_SHORT
                                    ).show()

                                })
                            listView.adapter = emptyAdapter
                        }

                    } else {
                        // Log error if response is not successful
                        Toast.makeText(this@ChildrenListActivity, "Error: ${response.code()}", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<List<Relationship>>, t: Throwable) {
                    // Handle failure case
                    Toast.makeText(this@ChildrenListActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
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
}