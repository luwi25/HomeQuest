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
import com.android.homequest.model.Relationship

class ChildrenListAdapter(
    private val context: Context,
    private val childrenlist: List<Relationship>,
    private val onClick: (Relationship) -> Unit,
    private val onLongCLick: (Relationship) -> Unit
): BaseAdapter() {
    override fun getCount(): Int = childrenlist.size

    override fun getItem(position: Int): Any = childrenlist[position]

    override fun getItemId(position: Int): Long = position.toLong()

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view = convertView ?: LayoutInflater.from(context)
            .inflate(R.layout.custom_children_list, parent, false)

        val name = view.findViewById<TextView>(R.id.tv_name)

        val children = childrenlist[position]

        name.setText(children.childFirstname)

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