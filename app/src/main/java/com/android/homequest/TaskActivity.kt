package com.android.homequest

import android.app.Activity
import android.os.Bundle
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import java.text.SimpleDateFormat
import java.util.*
import android.content.Intent
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import com.android.homequest.RC.RetrofitClient

import com.android.homequest.model.TaskAssignment
import com.google.gson.annotations.SerializedName
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

import java.util.Calendar

class TaskActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_task)



        // Calendar DateTime Picker
        //calendar date picker
        val calendar = Calendar.getInstance()

        val buttonPickDate = findViewById<EditText>(R.id.buttonPickDate)

        buttonPickDate.inputType = android.text.InputType.TYPE_NULL

        buttonPickDate.setOnClickListener {
            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH)
            val day = calendar.get(Calendar.DAY_OF_MONTH)

            val datePickerDialog = DatePickerDialog(
                this, // context
                { view, selectedYear, selectedMonth, selectedDayOfMonth ->
                    // When user picks a date
                    val selectedDate = "$selectedDayOfMonth/${selectedMonth + 1}/$selectedYear"
                    buttonPickDate.setText(selectedDate) // Set the picked date to your button (or EditText)
                },
                year,
                month,
                day
            )

            datePickerDialog.show()
        }



        val button_back = findViewById<ImageButton>(R.id.button_back)
        button_back.setOnClickListener {
            startActivity(
                Intent(this, AssignTaskActivity::class.java)
            )
        }

        var children = ""

        intent?.let {
            it.getStringExtra("assignChild")?.let { child  ->
                children = child
            }

        }



        val button_assign = findViewById<Button>(R.id.button_assign)
        button_assign.setOnClickListener {

            val et_taskname = findViewById<EditText>(R.id.et_taskname)
            val et_taskpoints = findViewById<EditText>(R.id.et_taskpoints)
            val taskpointsStr = et_taskpoints.text.toString()
            val taskname = et_taskname.text.toString()
            val taskpoints = taskpointsStr.toIntOrNull() ?: 0

            val atayDate: String = buttonPickDate.text.toString()
            Log.d("DEBUG", "assignDate from EditText: $atayDate")

            val sharedPreferences = getSharedPreferences("UserSession", MODE_PRIVATE)

            val ChildFirstname = sharedPreferences.getString("childFirstname", "Default Firstname")
            val ChildEmail = sharedPreferences.getString("childEmail", "default@email.com")

            val taskAssignment = TaskAssignment(
                assignTo = ChildFirstname.toString(),
                assignToEmail = ChildEmail.toString(),
                taskname = taskname,
                taskpoints = taskpoints,
                assignDate = atayDate,
                status = "Pending"

            )

// POST request to assign task
            RetrofitClient.instance.assignTask(taskAssignment).enqueue(object : Callback<TaskAssignment> {
                override fun onResponse(call: Call<TaskAssignment>, response: Response<TaskAssignment>) {
                    Log.d("API", "onResponse called")
                    if (response.isSuccessful) {
                        // Handle the success response
                        Log.d("API", "Task Assigned: ${response.body()}")
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

            Toast.makeText(this, "Task Added Successfully", Toast.LENGTH_SHORT).show()
            startActivity(
                Intent(this, ParentDashboardActivity::class.java)
            )

        }



    }
}