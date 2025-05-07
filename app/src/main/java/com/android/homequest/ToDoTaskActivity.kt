package com.android.homequest

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import android.widget.ListView
import android.widget.Toast
import com.android.homequest.Adapter.ChildrenListAdapter
import com.android.homequest.Adapter.TaskListAdapter
import com.android.homequest.Adapter.ToDoListAdapter
import com.android.homequest.RC.RetrofitClient
import com.android.homequest.model.Relationship
import com.android.homequest.model.Task
import com.android.homequest.model.TaskAssignment
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


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

        RetrofitClient.instance.getTaskAssignments()
            .enqueue(object : Callback<List<TaskAssignment>> {
                override fun onResponse(call: Call<List<TaskAssignment>>, response: Response<List<TaskAssignment>>) {
                    if (response.isSuccessful) {
                        // Fetch the user data from the response
                        taskList = response.body() ?: emptyList()

                        val targetList = taskList.filter {
                            it.assignTo.trim().equals(ChildFirstname?.trim(), ignoreCase = true) &&
                                    it.assignToEmail.trim().equals(ChildEmail?.trim(), ignoreCase = true)
                        }

                        if(targetList != null)
                        {
                            taskAdapter = ToDoListAdapter(
                                this@ToDoTaskActivity,
                                targetList,
                                onClick = {task ->

                                    startTask()

                                },
                                onLongCLick = {task ->
                                    Toast.makeText(this@ToDoTaskActivity, "${task.taskname}", Toast.LENGTH_SHORT).show()

                                })
                            listView.adapter = null
                            listView.adapter = taskAdapter

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
    }

    private fun startTask()
    {
        startActivity(
            Intent(this, SubmitTaskActivity::class.java)
        )
    }
}