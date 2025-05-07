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
import com.android.homequest.model.Family

class FamilyListAdapter(
    private val context: Context,
    private val familylist: List<Family>
): BaseAdapter() {
    override fun getCount(): Int = familylist.size

    override fun getItem(position: Int): Any = familylist[position]

    override fun getItemId(position: Int): Long = position.toLong()

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view = convertView ?: LayoutInflater.from(context)
            .inflate(R.layout.custom_children_list, parent, false)

        val name = view.findViewById<TextView>(R.id.tv_name)
        val image = view.findViewById<ImageView>(R.id.iv_img)
        val ppimg = view.findViewById<ImageView>(R.id.iv_ppimg)

        val family = familylist[position]

        ppimg.setImageResource(family.ppimg)
        name.setText(family.name)
        image.setImageResource(family.Img)

        return view
    }

}