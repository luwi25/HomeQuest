package com.android.homequest

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ImageButton
import android.widget.ListView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.android.homequest.Adapter.UpcomingTaskAdapter
import com.android.homequest.RC.RetrofitClient
import com.android.homequest.model.TaskAssignment
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UpcomingTaskActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_upcoming_task)

        val listView = findViewById<ListView>(R.id.listview)

        val sharedPreferences = getSharedPreferences("UserSession", MODE_PRIVATE)
        var ChildEmail = sharedPreferences.getString("childEmail", "default@email.com")
        var ChildFirstname = sharedPreferences.getString("childFirstname", "Default Firstname")

        RetrofitClient.instance.getUpcomingTasks(ChildFirstname.toString(), ChildEmail.toString()).enqueue(object :
            Callback<List<TaskAssignment>> {
            override fun onResponse(call: Call<List<TaskAssignment>>, response: Response<List<TaskAssignment>>) {
                if (response.isSuccessful) {
                    // Get the list of tasks
                    val taskList = response.body() ?: emptyList()

                    val targetList = taskList.filter {
                        it.assignTo.trim().equals(ChildFirstname?.trim(), ignoreCase = true) &&
                                it.assignToEmail.trim().equals(ChildEmail?.trim(), ignoreCase = true)
                    }


                    // Check if the list is not empty
                    if (targetList.isNotEmpty())
                    {
                        val adapter = UpcomingTaskAdapter(this@UpcomingTaskActivity, targetList)
                        listView.adapter = adapter

                    }
                    else
                    {

                    }

                } else {
                    Log.e("API", "Error: ${response.code()} - ${response.errorBody()?.string()}")
                }
            }

            override fun onFailure(call: Call<List<TaskAssignment>>, t: Throwable) {
                Log.e("API", "Error: ${t.message}")
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