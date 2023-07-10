package com.example.umarry.message

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.example.umarry.R
import com.example.umarry.auth.UserDataModel

class ListViewAdapter(val context:Context, val items:MutableList<UserDataModel>):BaseAdapter() {
    override fun getCount(): Int {
        return items.size
    }

    override fun getItem(position: Int): Any {
        return items[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        var convertView = convertView
        if(convertView == null){
            convertView = LayoutInflater.from(parent?.context).inflate(R.layout.itemlistview,parent,false)
        }

        val nickname = convertView!!.findViewById<TextView>(R.id.listnickname)
        nickname.text = items[position].nickname
        return convertView!!
    }

}