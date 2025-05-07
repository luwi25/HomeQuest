package com.android.homequest.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import com.android.homequest.R
import com.android.homequest.model.Reward
import com.android.homequest.model.Task

class ClaimRewardAdapter(
    private val context: Context,
    private val rewardlist: List<Reward>,
    private val onClick: (Reward) -> Unit,
    private val onLongCLick: (Reward) -> Unit
): BaseAdapter() {
    override fun getCount(): Int = rewardlist.size


    override fun getItem(position: Int): Any = rewardlist[position]

    override fun getItemId(position: Int): Long = position.toLong()

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view = convertView ?: LayoutInflater.from(context)
            .inflate(R.layout.custom_reward_list, parent, false)

        val desc = view.findViewById<TextView>(R.id.tv_desc)
        val points = view.findViewById<TextView>(R.id.tv_points)

        val reward = rewardlist[position]

        desc.setText(reward.description)
        points.setText(reward.points.toString())

        view.setOnClickListener {
            onClick(reward)
        }

        view.setOnLongClickListener {
            onLongCLick(reward)
            true
        }

        return view
    }

}