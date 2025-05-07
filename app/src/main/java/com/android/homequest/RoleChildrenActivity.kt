package com.android.homequest

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.ListView
import android.widget.PopupMenu
import android.widget.SearchView
import android.widget.Toast
import com.android.homequest.Adapter.RoleChildAdapter
import com.android.homequest.RC.RetrofitClient
import com.android.homequest.model.User
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RoleChildrenActivity : Activity() {

    private lateinit var listView: ListView
    private lateinit var searchView: SearchView
    private lateinit var childAdapter: RoleChildAdapter
    private var childList: List<User> = emptyList()  // This will store the full list of users

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_role_children)

        // Initialize views
        listView = findViewById(R.id.listview)
        searchView = findViewById(R.id.searchView)

        // Load users
        loadAllUsers()

        // Set up SearchView listener
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                // You can handle any logic when the query is submitted
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                // Trigger filter function when query text changes
                childAdapter.filter(newText)
                return true
            }
        })

        // Back button to go back to the ParentDashboardActivity
        val buttonBack = findViewById<ImageButton>(R.id.buttonback)
        buttonBack.setOnClickListener {
            startActivity(Intent(this, ParentDashboardActivity::class.java))
        }





    }

    private fun loadAllUsers() {
        RetrofitClient.instance.searchUsers(role = "Child", firstname = null, lastname = null)
            .enqueue(object : Callback<List<User>> {
                override fun onResponse(call: Call<List<User>>, response: Response<List<User>>) {
                    if (response.isSuccessful) {
                        // Fetch the user data from the response
                        childList = response.body() ?: emptyList()

                        // Initialize the adapter with the full list of users
                        childAdapter = RoleChildAdapter(
                            this@RoleChildrenActivity,
                            childList,
                            onClick = {userChild ->
                                val sharedPreferences = getSharedPreferences("UserSession", MODE_PRIVATE)
                                with (sharedPreferences.edit()) {
                                    putString("childFirstname", userChild.firstname)
                                    putString("childLastname", userChild.lastname)
                                    putString("childEmail", userChild.email)
                                    apply()
                                }
                                Toast.makeText(this@RoleChildrenActivity, "${userChild.firstname}", Toast.LENGTH_SHORT).show()
                                startFamily()


                            },
                            onLongCLick = {userChild ->
                                Toast.makeText(this@RoleChildrenActivity, "${userChild.firstname}", Toast.LENGTH_SHORT).show()

                            })
                        listView.adapter = childAdapter

                    } else {
                        // Log error if response is not successful
                        Toast.makeText(this@RoleChildrenActivity, "Error: ${response.code()}", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<List<User>>, t: Throwable) {
                    // Handle failure case
                    Toast.makeText(this@RoleChildrenActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
                }
            })
    }

    private fun startFamily()
    {
        startActivity(
            Intent(this, AddFamilyActivity::class.java)
        )
    }
}
