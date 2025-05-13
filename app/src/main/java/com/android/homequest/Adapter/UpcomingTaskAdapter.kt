package com.android.homequest.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.android.homequest.R
import com.android.homequest.model.TaskAssignment
import java.text.SimpleDateFormat
import java.util.Locale

class UpcomingTaskAdapter(
    private val context: Context,
    private val taskAssignmentlist: List<TaskAssignment>

): BaseAdapter() {
    override fun getCount(): Int = taskAssignmentlist.size

    override fun getItem(position: Int): Any = taskAssignmentlist[position]

    override fun getItemId(position: Int): Long = position.toLong()

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view = convertView ?: LayoutInflater.from(context).inflate(R.layout.custom_upcoming_task, parent, false)
        val task = taskAssignmentlist[position]

        val taskName = view.findViewById<TextView>(R.id.task_name)
        val taskDate = view.findViewById<TextView>(R.id.task_date)

        taskName.text = task.taskname
        taskDate.text = formatAssignDate(task.assignDate)

        return view
    }

    fun formatAssignDate(assignDate: String): String {
        return try {
            val inputFormat = SimpleDateFormat("d/M/yyyy", Locale.getDefault())
            val outputFormat = SimpleDateFormat("MMMM d, yyyy", Locale.getDefault())
            val date = inputFormat.parse(assignDate)
            outputFormat.format(date!!)
        } catch (e: Exception) {
            assignDate // fallback to original if parsing fails
        }
    }
}