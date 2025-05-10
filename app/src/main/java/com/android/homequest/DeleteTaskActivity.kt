package com.android.homequest

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.ListView
import android.widget.RelativeLayout
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.android.homequest.Adapter.DeleteChildAdapter
import com.android.homequest.Adapter.DeleteTaskAdapter
import com.android.homequest.Adapter.TaskAssignmentAdapter
import com.android.homequest.RC.RetrofitClient
import com.android.homequest.model.TaskAssignment
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DeleteTaskActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_delete_task)

        val listView = findViewById<ListView>(R.id.listview)

        val overlayLayout = findViewById<RelativeLayout>(R.id.overlay_layout)
        val btnConfirmDelete = findViewById<Button>(R.id.btn_confirm_delete)
        val btnCancelDelete = findViewById<Button>(R.id.btn_cancel_delete)

        // Fetch tasks from backend
        RetrofitClient.instance.getTaskAssignments().enqueue(object : Callback<List<TaskAssignment>> {
            override fun onResponse(call: Call<List<TaskAssignment>>, response: Response<List<TaskAssignment>>) {
                if (response.isSuccessful) {
                    // Get the list of tasks
                    val taskList = response.body()?.toMutableList() ?: mutableListOf()

                    // Set up the adapter to display the task names
                    val taskAdapter = DeleteTaskAdapter(this@DeleteTaskActivity, taskList,
                        onDeleteButtonClick = {task ->
                            overlayLayout.visibility = View.VISIBLE
                        })
                    if(taskList.isNotEmpty())
                    {
                        listView.adapter = taskAdapter
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

        btnCancelDelete.setOnClickListener {

            overlayLayout.visibility = View.GONE
        }

        btnConfirmDelete.setOnClickListener {

            val sharedPref = getSharedPreferences("UserSession", MODE_PRIVATE)

            val taskId = sharedPref.getString("taskID", null)

            Log.d("atay", "${taskId}")

            RetrofitClient.instance.deleteTaskById(taskId.toString()).enqueue(object : Callback<TaskAssignment> {
                override fun onResponse(call: Call<TaskAssignment>, response: Response<TaskAssignment>) {
                    Log.d("API", "onResponse called")
                    if (response.isSuccessful) {
                        // Handle success
                        Log.d("API", "Task deleted successfully")
                    } else {
                        // Handle error response
                        Log.e("API", "Error: ${response.code()} - ${response.errorBody()?.string()}")
                    }
                }

                override fun onFailure(call: Call<TaskAssignment>, t: Throwable) {
                    // Handle failure
                    Log.e("API", "Error: ${t.message}")
                }
            })

            overlayLayout.visibility = View.GONE
        }
    }
}