package com.android.homequest

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ImageButton
import android.widget.LinearLayout
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

        val sharedPreferences = getSharedPreferences("UserSession", MODE_PRIVATE)
        val createdbyEmail = sharedPreferences.getString("parentEmail", "Default")

        // Fetch tasks from backend
        RetrofitClient.instance.getTodayTasks().enqueue(object : Callback<List<TaskAssignment>> {
            override fun onResponse(call: Call<List<TaskAssignment>>, response: Response<List<TaskAssignment>>) {
                if (response.isSuccessful) {
                    // Get the list of tasks
                    val taskList = response.body() ?: emptyList()

                    val filteredTasks = taskList.filter { it.createdByEmail == createdbyEmail }

                    // Set up the adapter to display the task names

                    if(filteredTasks.isNotEmpty())
                    {
                        val taskAdapter = TaskAssignmentAdapter(this@TaskListActivity, filteredTasks)
                        listView.adapter = taskAdapter
                    }
                    else
                    {
                        val emptyTaskList = listOf(
                            TaskAssignment(
                                assignTo = "empty",
                                assignToEmail = "empty",
                                taskname = "No Task for Today",
                                taskpoints = 0,
                                assignDate = "Assign a task now",
                                status = "Do it now"
                            )
                        )
                        val emptyAdapter = TaskAssignmentAdapter(this@TaskListActivity, emptyTaskList)
                        listView.adapter = null
                        listView.adapter = emptyAdapter
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