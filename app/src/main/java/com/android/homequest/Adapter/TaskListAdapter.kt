package com.android.homequest.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import com.android.homequest.R
import com.android.homequest.model.Children
import com.android.homequest.model.Task

class TaskListAdapter(
    private val context: Context,
    private val tasklist: List<Task>
):BaseAdapter() {
    override fun getCount(): Int = tasklist.size

    override fun getItem(position: Int): Any  = tasklist[position]

    override fun getItemId(position: Int): Long = position.toLong()

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view = convertView ?: LayoutInflater.from(context)
            .inflate(R.layout.custom_task_list, parent, false)

        val desc = view.findViewById<TextView>(R.id.tv_desc)

        val task = tasklist[position]

        desc.setText(task.description)

        return view
    }

}