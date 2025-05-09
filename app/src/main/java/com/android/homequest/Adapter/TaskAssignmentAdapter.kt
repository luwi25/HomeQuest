package com.android.homequest.Adapter

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.android.homequest.R
import com.android.homequest.model.Task
import com.android.homequest.model.TaskAssignment
import java.text.SimpleDateFormat
import java.util.Locale

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
        val assignDateText = view.findViewById<TextView>(R.id.tv_assigndate)
        val status = view.findViewById<TextView>(R.id.tv_taskstatus)

        val task = taskAssignmentlist[position]

        desc.setText(task.taskname)
        assignDateText.text = formatAssignDate(task.assignDate)
        if(task.status == "Pending")
        {
            status.setText("Pending")
            status.setTextColor(Color.RED)
            status.setTypeface(null, android.graphics.Typeface.BOLD_ITALIC)
        }
        else
        {
            status.setText("Complete")
            status.setTextColor(Color.GREEN)
            status.setTypeface(null, android.graphics.Typeface.BOLD_ITALIC)
        }



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