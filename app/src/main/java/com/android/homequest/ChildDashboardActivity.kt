package com.android.homequest

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ListView
import android.widget.PopupMenu
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import com.android.homequest.Adapter.TaskAssignmentAdapter
import java.text.SimpleDateFormat
import java.util.*
import com.android.homequest.RC.RetrofitClient
import com.android.homequest.model.TaskAssignment
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class ChildDashboardActivity : Activity() {
    private lateinit var task1_date: TextView
    private lateinit var task1_name: TextView
    private lateinit var task2_date: TextView
    private lateinit var task2_name: TextView
    private lateinit var task3_date: TextView
    private lateinit var task3_name: TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_child_dashboard)

        displayUpcomingTask()

        val tv_seeAll = findViewById<TextView>(R.id.tv_seeAll)
        tv_seeAll.setOnClickListener {
            startActivity(
                Intent(this, UpcomingTaskActivity::class.java)
            )
        }

        val cv_todotask = findViewById<CardView>(R.id.cv_todotask)
        cv_todotask.setOnClickListener {
            startActivity(
                Intent(this, ToDoTaskActivity::class.java)
            )
        }

        val cv_viewreward = findViewById<CardView>(R.id.cv_viewreward)
        cv_viewreward.setOnClickListener {
            startActivity(
                Intent(this, ClaimRewardListActivity::class.java)
            )
        }

        val cv_seefamily = findViewById<CardView>(R.id.cv_seefamily)
        cv_seefamily.setOnClickListener {
            startActivity(
                Intent(this, FamilyListActivity::class.java)
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



        val button_profile = findViewById<ImageButton>(R.id.button_profile)

        button_profile.setOnClickListener {
            val popupMenu = PopupMenu(this, button_profile)
            popupMenu.menuInflater.inflate(R.menu.popup_profile, popupMenu.menu)

            popupMenu.setOnMenuItemClickListener { item ->
                when (item.itemId) {
                    R.id.option_profile -> {
                        startActivity(
                            Intent(this, ProfileActivity::class.java)
                        )
                        true
                    }
                    R.id.option_logout -> {
                        startActivity(
                            Intent(this, LogoutActivity::class.java)
                        )
                        true
                    }
                    else -> false
                }
            }

            popupMenu.show()
        }



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

    fun displayUpcomingTask()
    {
        task1_date = findViewById(R.id.task1_date)
        task1_name = findViewById(R.id.task1_name)
        task2_date = findViewById(R.id.task2_date)
        task2_name = findViewById(R.id.task2_name)
        task3_date = findViewById(R.id.task3_date)
        task3_name = findViewById(R.id.task3_name)

        val sharedPreferences = getSharedPreferences("UserSession", MODE_PRIVATE)
        var ChildEmail = sharedPreferences.getString("childEmail", "default@email.com")
        var ChildFirstname = sharedPreferences.getString("childFirstname", "Default Firstname")


        RetrofitClient.instance.getUpcomingTasks(ChildFirstname.toString(), ChildEmail.toString()).enqueue(object : Callback<List<TaskAssignment>> {
            override fun onResponse(call: Call<List<TaskAssignment>>, response: Response<List<TaskAssignment>>) {
                if (response.isSuccessful) {
                    // Get the list of tasks
                    val taskList = response.body() ?: emptyList()

                    val targetList = taskList.filter {
                        it.assignTo.trim().equals(ChildFirstname?.trim(), ignoreCase = true) &&
                                it.assignToEmail.trim().equals(ChildEmail?.trim(), ignoreCase = true)
                    }


                    // Check if the list is not empty
                    if (targetList.isNotEmpty()) {
                        if (targetList.size > 0) {
                            val firstTask = targetList[0]
                            task1_name.text = firstTask.taskname
                            val formattedDate = formatAssignDate(firstTask.assignDate)
                            task1_date.text = formattedDate

                        }
                        if (targetList.size > 1) {
                            val secondTask = targetList[1]
                            task2_name.text = secondTask.taskname
                            val formattedDate2 = formatAssignDate(secondTask.assignDate)
                            task2_date.text = formattedDate2
                        }
                        if (targetList.size > 2) {
                            val thirdTask = targetList[2]
                            task3_name.text = thirdTask.taskname
                            val formattedDate3 = formatAssignDate(thirdTask.assignDate)
                            task3_date.text = formattedDate3
                        }

                    } else {
                        task1_name.text = "No Upcoming Task Yet"
                    }

                } else {
                    Log.e("API", "Error: ${response.code()} - ${response.errorBody()?.string()}")
                }
            }

            override fun onFailure(call: Call<List<TaskAssignment>>, t: Throwable) {
                Log.e("API", "Error: ${t.message}")
            }
        })
    }
}