package com.android.homequest

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import android.widget.TextView


class ClaimingRewardActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_claiming_reward)

        val sharedPreferences = getSharedPreferences("UserSession", MODE_PRIVATE)
        val rewardName = sharedPreferences.getString("rewardName", "default@email.com")

        val tv_rewardname = findViewById<TextView>(R.id.tv_rewardname)
        tv_rewardname.setText(rewardName)

        val button_back = findViewById<ImageButton>(R.id.button_back)
        button_back.setOnClickListener {
            startActivity(
                Intent(this, ClaimRewardListActivity::class.java)
            )
        }



    }
}