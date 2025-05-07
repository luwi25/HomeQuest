package com.android.homequest

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import com.android.homequest.RC.RetrofitClient
import com.android.homequest.model.Reward
import com.android.homequest.model.TaskAssignment
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AddRewardActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_reward)


        val button_back = findViewById<ImageButton>(R.id.button_back)
        button_back.setOnClickListener {
            startActivity(
                Intent(this, RewardListActivity::class.java)
            )
        }

        val button_createReward = findViewById<Button>(R.id.button_createReward)
        button_createReward.setOnClickListener {

            val et_rewarDesc = findViewById<EditText>(R.id.et_rewarDesc)
            val et_taskpoints = findViewById<EditText>(R.id.et_taskpoints)

            val rewardDescription = et_rewarDesc.text.toString()
            val taskpointsStr = et_taskpoints.text.toString()
            val taskpoints = taskpointsStr.toIntOrNull() ?: 0

            val rewardS = Reward(
                rewardDescription,
                taskpoints
            )

            RetrofitClient.instance.createReward(rewardS).enqueue(object : Callback<Reward> {
                override fun onResponse(call: Call<Reward>, response: Response<Reward>) {
                    Log.d("API", "onResponse called")
                    if (response.isSuccessful) {
                        // Handle the success response
                        Log.d("API", "Task Assigned: ${response.body()}")
                    } else {
                        // Handle error response
                        Log.e("API", "Error: ${response.code()} - ${response.errorBody()?.string()}")
                    }
                }

                override fun onFailure(call: Call<Reward>, t: Throwable) {
                    // Handle failure
                    Log.e("API", "Error: ${t.message}")
                }
            })

            Toast.makeText(this, "Add Reward Successfully", Toast.LENGTH_SHORT).show()
            startActivity(
                Intent(this, RewardListActivity::class.java)
            )
        }


    }
}