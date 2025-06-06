package com.android.homequest

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.ListView
import com.android.homequest.Adapter.RewardListAdapter
import com.android.homequest.Adapter.TaskAssignmentAdapter
import com.android.homequest.RC.RetrofitClient
import com.android.homequest.model.Reward
import com.android.homequest.model.TaskAssignment
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RewardListActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reward_list)

        val listView = findViewById<ListView>(R.id.listview)
        // Inflate the footer layout
        val footerView = layoutInflater.inflate(R.layout.footer_layout, listView, false)

// Add the footer before setting the adapter
        listView.addFooterView(footerView)

        val sharedPreferences = getSharedPreferences("UserSession", MODE_PRIVATE)
        val createdbyEmail = sharedPreferences.getString("parentEmail", "Default")

        // Fetch tasks from backend
        RetrofitClient.instance.getRewards().enqueue(object :
            Callback<List<Reward>> {
            override fun onResponse(call: Call<List<Reward>>, response: Response<List<Reward>>) {
                if (response.isSuccessful) {
                    // Get the list of tasks
                    val rewardList = response.body() ?: emptyList()

                    val filteredRewards = rewardList.filter { it.createdBy == createdbyEmail }

                    if(filteredRewards.isNotEmpty())
                    {
                        // Set up the adapter to display the task names
                        val taskAdapter = RewardListAdapter(this@RewardListActivity, filteredRewards)
                        listView.adapter = taskAdapter
                    }
                    else
                    {
                        val emptyRewardList = listOf(
                            Reward(
                                description = "No Rewards Available",
                                points = 0,
                                status = "Not Available",
                            )
                        )
                        val emptyAdapter = RewardListAdapter(this@RewardListActivity, emptyRewardList)
                        listView.adapter = emptyAdapter
                    }


                } else {
                    Log.e("API", "Error: ${response.code()} - ${response.errorBody()?.string()}")
                }
            }

            override fun onFailure(call: Call<List<Reward>>, t: Throwable) {
                Log.e("API", "Error: ${t.message}")
            }
        })

        val buttonback = findViewById<ImageButton>(R.id.buttonback)
        buttonback.setOnClickListener {
            startActivity(
                Intent(this, ParentDashboardActivity::class.java)
            )
        }

        val button_addreward = footerView.findViewById<Button>(R.id.button_addreward)
        button_addreward.setOnClickListener {
            startActivity(
                Intent(this, AddRewardActivity::class.java)
            )
        }

        val AddReward = findViewById<ImageButton>(R.id.AddReward)
        AddReward.setOnClickListener {
            startActivity(
                Intent(this, AddRewardActivity::class.java)
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