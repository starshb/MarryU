package com.example.umarry.recycler

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintSet.Layout
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.umarry.R
import com.example.umarry.auth.UserDataModel
import com.example.umarry.utils.FirebaseRef
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage

class RecyclerView01Adapter(val context:Context, val items: MutableList<UserDataModel>):
    RecyclerView.Adapter<RecyclerView01Adapter.ViewHolder>(){
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RecyclerView01Adapter.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view :View = inflater.inflate(R.layout.itemrcv01,parent,false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerView01Adapter.ViewHolder, position: Int) {
        holder.binding(items[position])
    }

    override fun getItemCount(): Int {
        return items.size
    }


    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val image = itemView.findViewById<ImageView>(R.id.rcv01_img)
        val nickname = itemView.findViewById<TextView>(R.id.rcv01_nickname)

        fun binding(data: UserDataModel){
            val storageRef = Firebase.storage.reference.child(data.uid!!).child("01.png")
            storageRef.downloadUrl.addOnCompleteListener(OnCompleteListener { task ->
                if(task.isSuccessful){
                    Glide.with(context)
                        .load(task.result)
                        .into(image)
                }
            })
            nickname.text = data.nickname
        }
    }
}