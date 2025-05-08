package com.android.homequest

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import com.android.homequest.RC.RetrofitClient
import com.android.homequest.model.Reward
import com.android.homequest.model.StatusUpdate
import com.android.homequest.model.TaskAssignment
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class ClaimingRewardActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_claiming_reward)

        val sharedPreferences = getSharedPreferences("UserSession", MODE_PRIVATE)
        val rewardName = sharedPreferences.getString("rewardName", "Default Name")

        val tv_rewardname = findViewById<TextView>(R.id.tv_rewardname)
        tv_rewardname.setText(rewardName)

        val button_back = findViewById<ImageButton>(R.id.button_back)
        button_back.setOnClickListener {
            startActivity(
                Intent(this, ClaimRewardListActivity::class.java)
            )
        }

        val button_claim = findViewById<Button>(R.id.button_claim)
        button_claim.setOnClickListener {
            val rewardID = sharedPreferences.getString("rewardID", "Default ID")

            // Prepare the request body
            val statusUpdate = StatusUpdate(status = "Completed")

            // Call the API to update the status
            RetrofitClient.instance.updateRewardStatus(rewardID.toString(), statusUpdate).enqueue(object :
                Callback<Reward> {
                override fun onResponse(call: Call<Reward>, response: Response<Reward>) {
                    if (response.isSuccessful) {
                        Log.d("API", "Reward Status Updated: ${response.body()}")
                        Toast.makeText(this@ClaimingRewardActivity, "Reward status updated successfully", Toast.LENGTH_SHORT).show()
                    } else {
                        Log.e("API", "Error: ${response.code()} - ${response.errorBody()?.string()}")
                        Toast.makeText(this@ClaimingRewardActivity, "Failed to update task status", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<Reward>, t: Throwable) {
                    Log.e("API", "Error: ${t.message}")
                    Toast.makeText(this@ClaimingRewardActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
                }
            })

        }



    }
}