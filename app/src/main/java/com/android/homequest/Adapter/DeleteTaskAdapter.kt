package com.android.homequest.Adapter

import android.content.Context
import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.android.homequest.R
import com.android.homequest.model.Relationship
import com.android.homequest.model.TaskAssignment
import java.text.SimpleDateFormat
import java.util.Locale

class DeleteTaskAdapter(
    private val context: Context,
    private val taskAssignmentlist: MutableList<TaskAssignment>,
    private val onDeleteButtonClick: (TaskAssignment) -> Unit

): BaseAdapter() {
    override fun getCount(): Int = taskAssignmentlist.size

    override fun getItem(position: Int): Any = taskAssignmentlist[position]

    override fun getItemId(position: Int): Long = position.toLong()

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view = convertView ?: LayoutInflater.from(context)
            .inflate(R.layout.custom_deletetask, parent, false)

        val desc = view.findViewById<TextView>(R.id.tv_desc)
        val assignDateText = view.findViewById<TextView>(R.id.tv_assigndate)
        val status = view.findViewById<TextView>(R.id.tv_taskstatus)
        val button_delete = view.findViewById<Button>(R.id.button_delete)

        val task = taskAssignmentlist[position]

        desc.setText(task.taskname)
        assignDateText.text = formatAssignDate(task.assignDate)
        if(task.status == "Pending")
        {
            status.setText("Pending")
            status.setTextColor(Color.RED)
            status.setTypeface(null, android.graphics.Typeface.BOLD_ITALIC)
        }
        else if(task.status == "Completed")
        {
            status.setText("Complete")
            status.setTextColor(Color.GREEN)
            status.setTypeface(null, android.graphics.Typeface.BOLD_ITALIC)
        }

        button_delete.setOnClickListener {
            onDeleteButtonClick(task)

            Log.d("atay", "Task Name: ${task.taskname} Task Id: ${task.id}")


            val sharedPref = context.getSharedPreferences("UserSession", Context.MODE_PRIVATE)
            val editor = sharedPref.edit()

            editor.putString("taskID", task.id.toString())
            editor.commit()

            taskAssignmentlist.removeAt(position)
            notifyDataSetChanged()

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