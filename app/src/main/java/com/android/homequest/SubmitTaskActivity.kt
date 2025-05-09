package com.android.homequest

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.CheckBox
import android.widget.ImageButton
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.android.homequest.RC.RetrofitClient
import com.android.homequest.model.PointsUpdate
import com.android.homequest.model.StatusUpdate
import com.android.homequest.model.TaskAssignment
import com.android.homequest.model.User
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SubmitTaskActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_submit_task)

        val chk = findViewById<CheckBox>(R.id.chk)

        val button_submit = findViewById<Button>(R.id.button_submit)
        button_submit.setOnClickListener {
            if(chk.isChecked)
            {
                Toast.makeText(this, "Task Finished!", Toast.LENGTH_SHORT).show()
                val sharedPreferences = getSharedPreferences("UserSession", MODE_PRIVATE)
                val taskId = sharedPreferences.getString("TaskID", "Default Task ID")


                val editor = sharedPreferences.edit()





                // Prepare the request body
                val statusUpdate = StatusUpdate(status = "Completed")

                // Call the API to update the status
                RetrofitClient.instance.updateTaskStatus(taskId.toString(), statusUpdate).enqueue(object : Callback<TaskAssignment> {
                    override fun onResponse(call: Call<TaskAssignment>, response: Response<TaskAssignment>) {
                        if (response.isSuccessful) {
                            Log.d("API", "Task Status Updated: ${response.body()}")

                        } else {
                            Log.e("API", "Error: ${response.code()} - ${response.errorBody()?.string()}")
                            Toast.makeText(this@SubmitTaskActivity, "Failed to update task status", Toast.LENGTH_SHORT).show()
                        }
                    }

                    override fun onFailure(call: Call<TaskAssignment>, t: Throwable) {
                        Log.e("API", "Error: ${t.message}")
                        Toast.makeText(this@SubmitTaskActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
                    }
                })
                val childId = sharedPreferences.getString("childID", "Default Child ID")
                val childpoints = sharedPreferences.getInt("childPoints", 0)
                val taskpoints = sharedPreferences.getInt("TaskPoints", 0)

                Log.d("before", "${childpoints}")

                val pointsUpdate = PointsUpdate(points = taskpoints + childpoints)

                RetrofitClient.instance.updateChildPoints(childId.toString(), pointsUpdate).enqueue(object : Callback<User> {
                    override fun onResponse(call: Call<User>, response: Response<User>) {
                        if (response.isSuccessful) {
                            Log.d("API", "Points Updated: ${response.body()}")
                            Toast.makeText(this@SubmitTaskActivity, "Points updated successfully", Toast.LENGTH_SHORT).show()
                        } else {
                            Log.e("API", "Error: ${response.code()} - ${response.errorBody()?.string()}")
                            Toast.makeText(this@SubmitTaskActivity, "Failed to update points", Toast.LENGTH_SHORT).show()
                        }
                    }

                    override fun onFailure(call: Call<User>, t: Throwable) {
                        Log.e("API", "Error: ${t.message}")
                        Toast.makeText(this@SubmitTaskActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
                    }
                })

                Log.d("after", "${pointsUpdate.points}")

                editor.putInt("childPoints", pointsUpdate.points)
                editor.commit()

                val success = sharedPreferences.getInt("childPoints", 0)

                Log.d("Success", "${success}")
            }
        }

        val button_back = findViewById<ImageButton>(R.id.button_back)
        button_back.setOnClickListener {
            startActivity(
                Intent(this, ToDoTaskActivity::class.java)
            )
        }

    }
}