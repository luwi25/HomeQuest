package com.android.homequest

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ImageButton
import android.widget.ListView
import android.widget.Toast
import com.android.homequest.Adapter.ClaimRewardAdapter
import com.android.homequest.Adapter.RewardListAdapter
import com.android.homequest.Adapter.TaskAssignmentAdapter
import com.android.homequest.RC.RetrofitClient
import com.android.homequest.model.Reward
import com.android.homequest.model.TaskAssignment
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ClaimRewardListActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_claim_reward_list)

        val listView = findViewById<ListView>(R.id.listview)

        val sharedPreferences = getSharedPreferences("UserSession", MODE_PRIVATE)
        val editor = sharedPreferences.edit()

        RetrofitClient.instance.getRewards().enqueue(object :
            Callback<List<Reward>> {
            override fun onResponse(call: Call<List<Reward>>, response: Response<List<Reward>>) {
                if (response.isSuccessful) {
                    // Get the list of tasks
                    val rewardList = response.body() ?: emptyList()

                    // Set up the adapter to display the task names
                    val taskAdapter = ClaimRewardAdapter(
                        this@ClaimRewardListActivity,
                        rewardList,
                        onClick = {reward ->
                            val rewardId = reward.id
                            editor.putString("rewardID", rewardId)
                            editor.putString("rewardName", reward.description)
                            editor.commit()

                            startClaim()

                        },
                        onLongCLick = {reward ->
                            Toast.makeText(this@ClaimRewardListActivity, "${reward.description}", Toast.LENGTH_SHORT).show()

                        })
                    listView.adapter = taskAdapter
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
                Intent(this, ChildDashboardActivity::class.java)
            )
        }
    }

    private fun startClaim()
    {
        startActivity(
            Intent(this, ClaimingRewardActivity::class.java)
        )
    }
}