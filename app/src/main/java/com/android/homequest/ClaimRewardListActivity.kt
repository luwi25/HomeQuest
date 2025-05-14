package com.android.homequest

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.ListView
import android.widget.Toast
import com.android.homequest.Adapter.ChildrenListAdapter
import com.android.homequest.Adapter.ClaimRewardAdapter
import com.android.homequest.Adapter.RewardListAdapter
import com.android.homequest.Adapter.TaskAssignmentAdapter
import com.android.homequest.RC.RetrofitClient
import com.android.homequest.model.Relationship
import com.android.homequest.model.Reward
import com.android.homequest.model.TaskAssignment
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ClaimRewardListActivity : Activity() {

    private var createdByEmail: String? = null

    private var childList: List<Relationship> = emptyList()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_claim_reward_list)

        val listView = findViewById<ListView>(R.id.listview)

        val sharedPreferences = getSharedPreferences("UserSession", MODE_PRIVATE)
        val editor = sharedPreferences.edit()

        findChild { email ->
            if (email != null) {
                createdByEmail = email
                Log.d("CreatedByEmail", "Parent Email: $createdByEmail")

                RetrofitClient.instance.getRewards().enqueue(object :
                    Callback<List<Reward>> {
                    override fun onResponse(call: Call<List<Reward>>, response: Response<List<Reward>>) {
                        if (response.isSuccessful) {
                            // Get the list of tasks
                            val rewardList = response.body() ?: emptyList()

                            val matchingRewards = rewardList.filter { reward -> reward.createdBy == createdByEmail }
                            Log.d("API", "createdByEmail: ${createdByEmail}")

                            if (matchingRewards.isNotEmpty()) {

                                // Set up the adapter to display the task names
                                val taskAdapter = ClaimRewardAdapter(
                                    this@ClaimRewardListActivity,
                                    rewardList,
                                    onClick = { reward ->
                                        val rewardId = reward.id
                                        editor.putString("rewardID", rewardId)
                                        editor.putString("rewardName", reward.description)
                                        editor.putInt("rewardPoints", reward.points)
                                        editor.commit()

                                        startClaim()

                                    },
                                    onLongCLick = { reward ->
                                        Toast.makeText(
                                            this@ClaimRewardListActivity,
                                            "${reward.description}",
                                            Toast.LENGTH_SHORT
                                        ).show()

                                    })
                                listView.adapter = taskAdapter
                            }
                            else
                            {
                                val emptyTaskList = listOf(
                                    Reward(
                                        description = "No Reward To Claim",
                                        points = 0,
                                        status = "Not Available",
                                        createdBy = "Not Available"
                                    )
                                )

                                val emptyAdapter = ClaimRewardAdapter(
                                    this@ClaimRewardListActivity,
                                    emptyTaskList,
                                    onClick = { reward ->
                                        Toast.makeText(this@ClaimRewardListActivity, "No Reward Available", Toast.LENGTH_SHORT).show()


                                    },
                                    onLongCLick = { reward ->
                                        Toast.makeText(this@ClaimRewardListActivity, "No Reward Available", Toast.LENGTH_SHORT).show()

                                    })
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

                // You can now use createdByEmail here or call another function
                // example: updateUIWithEmail(createdByEmail)
            } else {
                Log.d("CreatedByEmail", "No parent email found")
            }
        }





        val buttonback = findViewById<ImageButton>(R.id.buttonback)
        buttonback.setOnClickListener {
            startActivity(
                Intent(this, ChildDashboardActivity::class.java)
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
    }

    private fun startClaim()
    {
        startActivity(
            Intent(this, ClaimingRewardActivity::class.java)
        )
    }
    fun findChild(onEmailFound: (String?) -> Unit)
    {
        val sharedPreferences = getSharedPreferences("UserSession", MODE_PRIVATE)
        val ChildEmail = sharedPreferences.getString("childEmail", "default@email.com")
        val ChildFirstname = sharedPreferences.getString("childFirstname", "Default Firstname")

        RetrofitClient.instance.searchRelationship(parentFirstname = null,
            parentEmail = null,
            childFirstname = ChildFirstname,
            childEmail = ChildEmail)
            .enqueue(object : Callback<List<Relationship>> {
                override fun onResponse(call: Call<List<Relationship>>, response: Response<List<Relationship>>) {
                    if (response.isSuccessful) {
                        // Fetch the user data from the response
                        childList = response.body() ?: emptyList()

                        val targetRelationship = childList.find {
                            it.childFirstname.trim().equals(ChildFirstname?.trim(), ignoreCase = true) &&
                                    it.childEmail.trim().equals(ChildEmail?.trim(), ignoreCase = true)
                        }

                        if (targetRelationship != null)
                        {
                            onEmailFound(targetRelationship.parentEmail)

                        }
                        else
                        {
                            Toast.makeText(this@ClaimRewardListActivity, "No family found", Toast.LENGTH_SHORT).show()
                            onEmailFound("Not Found")
                        }

                    } else {
                        // Log error if response is not successful
                        Toast.makeText(this@ClaimRewardListActivity, "Error: ${response.code()}", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<List<Relationship>>, t: Throwable) {
                    // Handle failure case
                    Toast.makeText(this@ClaimRewardListActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
                }
            })

    }
}