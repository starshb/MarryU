package com.example.umarry.cardstack

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.umarry.R
import com.example.umarry.auth.UserDataModel

class NewtodayAdapter(val context: Context, val items: MutableList<UserDataModel>) :
    RecyclerView.Adapter<NewtodayAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewtodayAdapter.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view :View =  inflater.inflate(R.layout.itemcard, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder : NewtodayAdapter.ViewHolder, position: Int) {
        holder.binding(items[position]) // 어댑터 ViewHolder의 binding 함수를 실행하여 UserDataModel item 홀딩함
    }

    override fun getItemCount(): Int {
        return items.size
    }

    //view와 data를 연결시켜주는 inner class
    inner class ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {
        val name = itemView.findViewById<TextView>(R.id.itemName)
        val age = itemView.findViewById<TextView>(R.id.itemAge)
        fun binding(data : UserDataModel) {
            name.text = data.name
            age.text = data.age
        }
    }

}