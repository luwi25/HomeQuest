package com.android.homequest

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.ListView
import android.widget.TextView
import android.widget.Toast
import com.android.homequest.Adapter.ChildrenListAdapter
import com.android.homequest.Adapter.FamilyListAdapter
import com.android.homequest.RC.RetrofitClient
import com.android.homequest.model.Family
import com.android.homequest.model.Relationship
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FamilyListActivity : Activity() {

    private lateinit var listView: ListView
    private lateinit var childAdapter: ChildrenListAdapter
    private var childList: List<Relationship> = emptyList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_family_list)

        val tv_name = findViewById<TextView>(R.id.tv_name)

        listView = findViewById(R.id.listview)

        val sharedPreferences = getSharedPreferences("UserSession", MODE_PRIVATE)
        val ChildEmail = sharedPreferences.getString("childEmail", "default@email.com")
        val ChildFirstname = sharedPreferences.getString("childFirstname", "Default Firstname")

        var firstnameParent: String = ""
        var emailParent: String = ""

        RetrofitClient.instance.searchRelationship(parentFirstname = null,
            parentEmail = null,
            childFirstname = ChildFirstname,
            childEmail = ChildEmail)
            .enqueue(object : Callback<List<Relationship>> {
                override fun onResponse(call: Call<List<Relationship>>, response: Response<List<Relationship>>) {
                    if (response.isSuccessful) {
                        // Fetch the user data from the response
                        childList = response.body() ?: emptyList()

                        val targetRelationship = childList.find {
                            it.childFirstname.trim().equals(ChildFirstname?.trim(), ignoreCase = true) &&
                                    it.childEmail.trim().equals(ChildEmail?.trim(), ignoreCase = true)
                        }

                        if (targetRelationship != null)
                        {
                            firstnameParent = targetRelationship.parentFirstname
                            emailParent = targetRelationship.parentEmail


                            RetrofitClient.instance.searchRelationship(parentFirstname = firstnameParent,
                                parentEmail = emailParent,
                                childFirstname = null,
                                childEmail = null)
                                .enqueue(object : Callback<List<Relationship>> {
                                    override fun onResponse(call: Call<List<Relationship>>, response: Response<List<Relationship>>) {
                                        if (response.isSuccessful) {
                                            // Fetch the user data from the response
                                            childList = response.body() ?: emptyList()

                                            tv_name.setText("${targetRelationship.parentFirstname} ${targetRelationship.parentEmail}")

                                            // Initialize the adapter with the full list of users
                                            childAdapter = ChildrenListAdapter(
                                                this@FamilyListActivity,
                                                childList,
                                                onClick = {children ->

                                                    Toast.makeText(this@FamilyListActivity, "${children.childFirstname}", Toast.LENGTH_SHORT).show()


                                                },
                                                onLongCLick = {children ->
                                                    Toast.makeText(this@FamilyListActivity, "${children.childFirstname}", Toast.LENGTH_SHORT).show()

                                                })
                                            listView.adapter = null
                                            listView.adapter = childAdapter

                                        } else {
                                            // Log error if response is not successful
                                            Toast.makeText(this@FamilyListActivity, "Error: ${response.code()}", Toast.LENGTH_SHORT).show()
                                        }
                                    }

                                    override fun onFailure(call: Call<List<Relationship>>, t: Throwable) {
                                        // Handle failure case
                                        Toast.makeText(this@FamilyListActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
                                    }
                                })
                        }
                        else
                        {
                            Toast.makeText(this@FamilyListActivity, "No family found", Toast.LENGTH_SHORT).show()
                            return
                        }

                    } else {
                        // Log error if response is not successful
                        Toast.makeText(this@FamilyListActivity, "Error: ${response.code()}", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<List<Relationship>>, t: Throwable) {
                    // Handle failure case
                    Toast.makeText(this@FamilyListActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
                }
            })



        val buttonback = findViewById<ImageButton>(R.id.buttonback)
        buttonback.setOnClickListener {
            startActivity(
                Intent(this, ChildDashboardActivity::class.java)
            )
        }

    }
}