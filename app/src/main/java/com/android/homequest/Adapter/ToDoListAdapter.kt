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
import com.android.homequest.model.Children
import com.android.homequest.model.Task
import com.android.homequest.model.TaskAssignment
import java.text.SimpleDateFormat
import java.util.Locale

class ToDoListAdapter(
    private val context: Context,
    private val tasklist: List<TaskAssignment>,
    private val onClick: (TaskAssignment) -> Unit,
    private val onLongCLick: (TaskAssignment) -> Unit
): BaseAdapter() {
    override fun getCount(): Int = tasklist.size

    override fun getItem(position: Int): Any = tasklist[position]

    override fun getItemId(position: Int): Long = position.toLong()

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view = convertView ?: LayoutInflater.from(context)
            .inflate(R.layout.custom_todotask, parent, false)

        val desc = view.findViewById<TextView>(R.id.tv_desc)
        val assignDateText = view.findViewById<TextView>(R.id.tv_assigndate)
        val status = view.findViewById<TextView>(R.id.tv_taskstatus)

        val task = tasklist[position]




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


        view.setOnClickListener {
            onClick(task)
        }

        view.setOnLongClickListener {
            onLongCLick(task)
            true
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