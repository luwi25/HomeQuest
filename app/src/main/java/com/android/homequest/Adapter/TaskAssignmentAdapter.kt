package com.android.homequest.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.android.homequest.R
import com.android.homequest.model.Task
import com.android.homequest.model.TaskAssignment

class TaskAssignmentAdapter(
    private val context: Context,
    private val taskAssignmentlist: List<TaskAssignment>
): BaseAdapter() {
    override fun getCount(): Int = taskAssignmentlist.size

    override fun getItem(position: Int): Any = taskAssignmentlist[position]

    override fun getItemId(position: Int): Long = position.toLong()

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view = convertView ?: LayoutInflater.from(context)
            .inflate(R.layout.custom_task_list, parent, false)

        val desc = view.findViewById<TextView>(R.id.tv_desc)

        val task = taskAssignmentlist[position]

        desc.setText(task.taskname)

        return view
    }
}