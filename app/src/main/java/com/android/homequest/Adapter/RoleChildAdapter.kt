package com.android.homequest.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.android.homequest.R
import com.android.homequest.model.Children
import com.android.homequest.model.User

class RoleChildAdapter(
    private val context: Context,
    private val originalList: List<User>,
    private val onClick: (User) -> Unit,
    private val onLongCLick: (User) -> Unit
) : BaseAdapter() {

    var childrenlist: List<User> = originalList.toList()  // This is the list that will be filtered

    override fun getCount(): Int = childrenlist.size

    override fun getItem(position: Int): Any = childrenlist[position]

    override fun getItemId(position: Int): Long = position.toLong()

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view = convertView ?: LayoutInflater.from(context)
            .inflate(R.layout.custom_rolechild_list, parent, false)

        val firstname = view.findViewById<TextView>(R.id.tv_firstname)
        val lastname = view.findViewById<TextView>(R.id.tv_lastname)

        val userChild = childrenlist[position]

        firstname.text = userChild.firstname
        lastname.text = userChild.lastname

        view.setOnClickListener {
            onClick(userChild)
        }

        view.setOnLongClickListener {
            onLongCLick(userChild)
            true
        }

        return view
    }

    // Implement the filter function
    fun filter(query: String?) {
        childrenlist = if (!query.isNullOrEmpty()) {
            val queryTrimmed = query.trim().toLowerCase() // Trim spaces and make it case-insensitive
            originalList.filter {
                val fullName = "${it.firstname} ${it.lastname}".toLowerCase() // Combine first and last names
                it.firstname.toLowerCase().contains(queryTrimmed) ||
                        it.lastname.toLowerCase().contains(queryTrimmed) ||
                        fullName.contains(queryTrimmed) // Check for the full name match
            }
        } else {
            originalList // Return to the original list if query is empty
        }

        notifyDataSetChanged() // Notify the adapter that the data has changed
    }

}
