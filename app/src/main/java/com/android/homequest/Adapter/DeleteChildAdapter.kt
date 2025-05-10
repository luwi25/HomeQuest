package com.android.homequest.Adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.android.homequest.DeleteChildActivity
import com.android.homequest.R
import com.android.homequest.model.Relationship

class DeleteChildAdapter(
    private val context: Context,
    private val childrenlist: MutableList<Relationship>,
    private val onClick: (Relationship) -> Unit,
    private val onLongCLick: (Relationship) -> Unit,
    private val onDeleteButtonClick: (Relationship) -> Unit

): BaseAdapter() {
    override fun getCount(): Int = childrenlist.size

    override fun getItem(position: Int): Any = childrenlist[position]

    override fun getItemId(position: Int): Long = position.toLong()

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view = convertView ?: LayoutInflater.from(context)
            .inflate(R.layout.custom_deletechild, parent, false)

        val name = view.findViewById<TextView>(R.id.tv_name)
        val button_delete = view.findViewById<Button>(R.id.button_delete)

        val children = childrenlist[position]

        name.setText(children.childFirstname)

        button_delete.setOnClickListener {
            onDeleteButtonClick(children)
            Toast.makeText(context, "Clicked: ${children.childFirstname}", Toast.LENGTH_SHORT).show()

            val sharedPref = context.getSharedPreferences("UserSession", Context.MODE_PRIVATE)
            val editor = sharedPref.edit()

            editor.putString("relationshipID", children.id.toString())
            editor.putString("childFirstname", children.childFirstname)
            editor.putString("childEmail", children.childEmail)
            editor.commit()

            childrenlist.removeAt(position)
            notifyDataSetChanged()

        }

        view.setOnClickListener {
            onClick(children)
        }

        view.setOnLongClickListener {
            onLongCLick(children)
            true
        }
        return view
    }
}