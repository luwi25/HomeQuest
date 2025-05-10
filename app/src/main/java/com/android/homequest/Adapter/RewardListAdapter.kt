package com.android.homequest.Adapter

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import com.android.homequest.R
import com.android.homequest.model.Reward

class RewardListAdapter(
    private val context: Context,
    private val rewardlist: List<Reward>
):BaseAdapter() {
    override fun getCount(): Int = rewardlist.size

    override fun getItem(position: Int): Any = rewardlist[position]

    override fun getItemId(position: Int): Long = position.toLong()

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view = convertView ?: LayoutInflater.from(context)
            .inflate(R.layout.custom_reward_list, parent, false)

        val desc = view.findViewById<TextView>(R.id.tv_desc)
        val points = view.findViewById<TextView>(R.id.tv_points)
        val status = view.findViewById<TextView>(R.id.tv_status)

        val reward = rewardlist[position]

        desc.setText(reward.description)
        points.setText("${reward.points ?: 0} points")
        if(reward.status== "Pending")
        {
            status.setText("Pending")
            status.setTextColor(Color.RED)
            status.setTypeface(null, android.graphics.Typeface.BOLD_ITALIC)
        }
        else
        {
            status.setText("Claimed")
            status.setTextColor(Color.GREEN)
            status.setTypeface(null, android.graphics.Typeface.BOLD_ITALIC)
        }

        return view
    }

}