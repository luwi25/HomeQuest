package com.android.homequest

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.ListView
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.android.homequest.Adapter.ChildrenListAdapter
import com.android.homequest.Adapter.DeleteChildAdapter
import com.android.homequest.RC.RetrofitClient
import com.android.homequest.model.Relationship
import com.android.homequest.model.TaskAssignment
import com.android.homequest.model.User
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DeleteChildActivity : Activity() {

    private lateinit var listView: ListView
    private lateinit var childAdapter: DeleteChildAdapter
    private var childList: MutableList<Relationship> = mutableListOf()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_delete_child)

        val overlayLayout = findViewById<RelativeLayout>(R.id.overlay_layout)
        val btnConfirmDelete = findViewById<Button>(R.id.btn_confirm_delete)
        val btnCancelDelete = findViewById<Button>(R.id.btn_cancel_delete)

        listView = findViewById(R.id.listview)
        val sharedPreferences = getSharedPreferences("UserSession", MODE_PRIVATE)
        val ParentEmail = sharedPreferences.getString("parentEmail", "default@email.com")
        val ParentFirstname = sharedPreferences.getString("parentFirstname", "Default Firstname")
        val childEmail = sharedPreferences.getString("childEmail", "Default Email")


        val editor = sharedPreferences.edit()

        RetrofitClient.instance.searchRelationship(parentFirstname = ParentFirstname,
            parentEmail = ParentEmail,
            childFirstname = null,
            childEmail = null)
            .enqueue(object : Callback<List<Relationship>> {
                override fun onResponse(call: Call<List<Relationship>>, response: Response<List<Relationship>>) {
                    if (response.isSuccessful) {
                        // Fetch the user data from the response
                        childList = response.body()?.toMutableList() ?: mutableListOf()

                        if(childList.isNotEmpty()) {

                            // Initialize the adapter with the full list of users
                            childAdapter = DeleteChildAdapter(
                                this@DeleteChildActivity,
                                childList,
                                onClick = { children ->


                                },
                                onLongCLick = { children ->
                                    Toast.makeText(
                                        this@DeleteChildActivity,
                                        "${children.parentFirstname}",
                                        Toast.LENGTH_SHORT
                                    ).show()

                                },
                                onDeleteButtonClick = { children ->
                                    overlayLayout.visibility = View.VISIBLE

                                })
                            listView.adapter = childAdapter
                        }

                    } else {
                        // Log error if response is not successful
                        Toast.makeText(this@DeleteChildActivity, "Error: ${response.code()}", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<List<Relationship>>, t: Throwable) {
                    // Handle failure case
                    Toast.makeText(this@DeleteChildActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
                }
            })

        val buttonback = findViewById<ImageButton>(R.id.buttonback)
        buttonback.setOnClickListener {
            startActivity(
                Intent(this, ParentDashboardActivity::class.java)
            )
        }

        btnConfirmDelete.setOnClickListener {
            val sharedPref = getSharedPreferences("UserSession", MODE_PRIVATE)

            val relationshipId = sharedPref.getString("relationshipID", null)
            val childFirstname = sharedPref.getString("childFirstname", null)
            val childEmail = sharedPref.getString("childEmail", null)

            RetrofitClient.instance.deleteRelationshipById(relationshipId.toString())
                .enqueue(object : Callback<Relationship> {
                    override fun onResponse(call: Call<Relationship>, response: Response<Relationship>) {
                        if (response.isSuccessful) {
                            Log.d("DELETE", "Relationship deleted successfully")

                            RetrofitClient.instance.deleteTaskByAssignee(childFirstname.toString(), childEmail.toString())
                                .enqueue(object : Callback<TaskAssignment> {
                                    override fun onResponse(call: Call<TaskAssignment>, response: Response<TaskAssignment>) {
                                        if (response.isSuccessful) {
                                            Log.d("DELETE", "${childFirstname.toString()} Task deleted successfully")
                                        } else {
                                            Log.e("DELETE", "Error: ${response.code()}")
                                        }
                                    }

                                    override fun onFailure(call: Call<TaskAssignment>, t: Throwable) {
                                        Log.e("DELETE", "Failure: ${t.message}")
                                    }
                                })
                        } else {
                            Log.e("DELETE", "Error: ${response.code()}")
                        }
                    }

                    override fun onFailure(call: Call<Relationship>, t: Throwable) {
                        Log.e("DELETE", "Failure: ${t.message}")
                    }
                })

            overlayLayout.visibility = View.GONE

        }
        btnCancelDelete.setOnClickListener {
            overlayLayout.visibility = View.GONE
            recreate()
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