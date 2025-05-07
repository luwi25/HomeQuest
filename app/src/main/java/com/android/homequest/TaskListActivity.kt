package com.android.homequest

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ImageButton
import android.widget.ListView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.android.homequest.Adapter.ChildrenListAdapter
import com.android.homequest.Adapter.TaskAssignmentAdapter
import com.android.homequest.Adapter.TaskListAdapter
import com.android.homequest.RC.RetrofitClient
import com.android.homequest.model.Children
import com.android.homequest.model.Task
import com.android.homequest.model.TaskAssignment
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class TaskListActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_task_list)

        val listView = findViewById<ListView>(R.id.listview)

        // Fetch tasks from backend
        RetrofitClient.instance.getTaskAssignments().enqueue(object : Callback<List<TaskAssignment>> {
            override fun onResponse(call: Call<List<TaskAssignment>>, response: Response<List<TaskAssignment>>) {
                if (response.isSuccessful) {
                    // Get the list of tasks
                    val taskList = response.body() ?: emptyList()

                    // Set up the adapter to display the task names
                    val taskAdapter = TaskAssignmentAdapter(this@TaskListActivity, taskList)
                    listView.adapter = taskAdapter
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
                Intent(this, ParentDashboardActivity::class.java)
            )
        }

    }
}