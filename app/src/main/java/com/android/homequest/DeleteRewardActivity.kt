package com.android.homequest

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.ListView
import android.widget.RelativeLayout
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.android.homequest.Adapter.DeleteRewardAdapter
import com.android.homequest.Adapter.RewardListAdapter
import com.android.homequest.RC.RetrofitClient
import com.android.homequest.model.Reward
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DeleteRewardActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_delete_reward)

        val listView = findViewById<ListView>(R.id.listview)

        val overlayLayout = findViewById<RelativeLayout>(R.id.overlay_layout)
        val btnConfirmDelete = findViewById<Button>(R.id.btn_confirm_delete)
        val btnCancelDelete = findViewById<Button>(R.id.btn_cancel_delete)

        val buttonback = findViewById<ImageButton>(R.id.buttonback)
        buttonback.setOnClickListener {
            startActivity(
                Intent(this, ParentDashboardActivity::class.java)
            )
        }

        btnCancelDelete.setOnClickListener {
            overlayLayout.visibility = View.GONE
        }


        RetrofitClient.instance.getRewards().enqueue(object :
            Callback<List<Reward>> {
            override fun onResponse(call: Call<List<Reward>>, response: Response<List<Reward>>) {
                if (response.isSuccessful) {
                    // Get the list of tasks
                    val rewardList = response.body()?.toMutableList() ?: mutableListOf()

                    val targetList = rewardList.filter {
                        it.status?.trim()?.equals("Pending", ignoreCase = true) == true
                    }
                    if(rewardList.isNotEmpty())
                    {
                        // Set up the adapter to display the task names
                        val taskAdapter = DeleteRewardAdapter(
                            this@DeleteRewardActivity,
                            rewardList,
                            onDeleteButtonClick = {reward ->

                                overlayLayout.visibility = View.VISIBLE

                            })
                        listView.adapter = taskAdapter
                    }


                } else {
                    Log.e("API", "Error: ${response.code()} - ${response.errorBody()?.string()}")
                }
            }

            override fun onFailure(call: Call<List<Reward>>, t: Throwable) {
                Log.e("API", "Error: ${t.message}")
            }
        })

        btnConfirmDelete.setOnClickListener {

            val sharedPref = getSharedPreferences("UserSession", MODE_PRIVATE)

            val rewardId = sharedPref.getString("rewardID", null)

            RetrofitClient.instance.deleteRewardById(rewardId.toString()).enqueue(object : Callback<Reward> {
                override fun onResponse(call: Call<Reward>, response: Response<Reward>) {
                    Log.d("API", "onResponse called")
                    if (response.isSuccessful) {
                        Log.d("API", "Reward deleted: ${response.body()}")
                        // Optionally update UI or adapter here
                    } else {
                        Log.e("API", "Error: ${response.code()} - ${response.errorBody()?.string()}")
                    }
                }

                override fun onFailure(call: Call<Reward>, t: Throwable) {
                    Log.e("API", "Error: ${t.message}")
                }
            })


            overlayLayout.visibility = View.GONE
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