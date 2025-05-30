package com.android.homequest

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import android.widget.ListView
import android.widget.TextView
import android.widget.Toast
import com.android.homequest.Adapter.ChildrenListAdapter
import android.graphics.Color
import android.widget.LinearLayout
import com.android.homequest.Adapter.TaskAssignmentAdapter
import com.android.homequest.Adapter.TaskListAdapter
import com.android.homequest.Adapter.ToDoListAdapter
import com.android.homequest.RC.RetrofitClient
import com.android.homequest.model.Relationship
import com.android.homequest.model.Task
import com.android.homequest.model.TaskAssignment
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.Locale


class ToDoTaskActivity : Activity() {
    private lateinit var listView: ListView
    private lateinit var taskAdapter: ToDoListAdapter
    private var taskList: List<TaskAssignment> = emptyList()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_to_do_task)

        listView = findViewById(R.id.listview)

        val sharedPreferences = getSharedPreferences("UserSession", MODE_PRIVATE)
        var ChildEmail = sharedPreferences.getString("childEmail", "default@email.com")
        var ChildFirstname = sharedPreferences.getString("childFirstname", "Default Firstname")


        RetrofitClient.instance.getTodayTasks()
            .enqueue(object : Callback<List<TaskAssignment>> {
                override fun onResponse(call: Call<List<TaskAssignment>>, response: Response<List<TaskAssignment>>) {
                    if (response.isSuccessful) {
                        // Fetch the user data from the response
                        taskList = response.body() ?: emptyList()

                        val targetList = taskList.filter {
                            it.assignTo.trim().equals(ChildFirstname?.trim(), ignoreCase = true) &&
                                    it.assignToEmail.trim().equals(ChildEmail?.trim(), ignoreCase = true)
                        }

                        val pendingTasks = targetList.filter { it.status.equals("Pending", ignoreCase = true) }


                        if(targetList.isNotEmpty())
                        {
                            taskAdapter = ToDoListAdapter(
                                this@ToDoTaskActivity,
                                targetList,
                                onClick = {task ->
                                    if(task.status == "Pending") {
                                        val taskId = task.id
                                        val taskPoints = task.taskpoints
                                        val sharedPreferences =
                                            getSharedPreferences("UserSession", MODE_PRIVATE)
                                        val editor = sharedPreferences.edit()
                                        editor.putString("TaskID", taskId)
                                        editor.putInt("TaskPoints", taskPoints)
                                        editor.commit()
                                        startTask()
                                    }
                                    else{
                                        Toast.makeText(this@ToDoTaskActivity, "${task.status} task for today", Toast.LENGTH_SHORT).show()
                                    }

                                },
                                onLongCLick = {task ->
                                    Toast.makeText(this@ToDoTaskActivity, "${task.status} task for today", Toast.LENGTH_SHORT).show()


                                })
                            listView.adapter = null
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
                                    assignDate = "Parent forgot to task you today xd",
                                    status = "Do it now"
                                )
                            )
                            val emptyAdapter = ToDoListAdapter(
                                this@ToDoTaskActivity,
                                emptyTaskList,
                                onClick = {task ->
                                    Toast.makeText(this@ToDoTaskActivity, "No Task", Toast.LENGTH_SHORT).show()
                                },
                                onLongCLick = {task ->
                                    Toast.makeText(this@ToDoTaskActivity, "No Task", Toast.LENGTH_SHORT).show()
                                })
                            listView.adapter = emptyAdapter
                        }


                    } else {
                        // Log error if response is not successful
                        Toast.makeText(this@ToDoTaskActivity, "Error: ${response.code()}", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<List<TaskAssignment>>, t: Throwable) {
                    // Handle failure case
                    Toast.makeText(this@ToDoTaskActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
                }
            })

        val buttonback = findViewById<ImageButton>(R.id.buttonback)
        buttonback.setOnClickListener {
            startActivity(
                Intent(this, ChildDashboardActivity::class.java)
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
    }

    private fun startTask()
    {
        startActivity(
            Intent(this, SubmitTaskActivity::class.java)
        )
    }
    fun formatAssignDate(assignDate: String): String {
        return try {
            val inputFormat = SimpleDateFormat("d/M/yyyy", Locale.getDefault())
            val outputFormat = SimpleDateFormat("MMMM d, yyyy", Locale.getDefault())
            val date = inputFormat.parse(assignDate)
            outputFormat.format(date!!)
        } catch (e: Exception) {
            assignDate // fallback to original if parsing fails
        }
    }
}