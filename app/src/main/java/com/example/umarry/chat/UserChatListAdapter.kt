package com.example.umarry.chat

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.umarry.R
import com.example.umarry.auth.UserDataModel
import com.example.umarry.chat.detail.ChatActivity
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import de.hdodenhof.circleimageview.CircleImageView

class UserChatListAdapter (private val context: Context, private val items: MutableList<UserDataModel>):
    RecyclerView.Adapter<UserChatListAdapter.ViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserChatListAdapter.ViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.itemchatlist,parent,false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: UserChatListAdapter.ViewHolder, position: Int) {
        val item = items[position]
        holder.nickname.text = item.nickname.toString()
        Glide.with(context).load(item.uid).placeholder(R.drawable.ic_person).into(holder.userimage)

        holder.layoutChatList.setOnClickListener{
            val intent = Intent(context, ChatActivity::class.java)
            intent.putExtra("otherUid",item.uid)
            intent.putExtra("otherNickname",item.nickname)
            context.startActivity(intent)
        }

        val storageRef = Firebase.storage.reference.child(item.uid!!).child("01.png")
        storageRef.downloadUrl.addOnCompleteListener(OnCompleteListener { task ->
            if(task.isSuccessful){
                Glide.with(context)
                    .load(task.result)
                    .into(holder.userimage)
            }
        })

    }

    override fun getItemCount(): Int {
        return items.size
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view){
        val userimage = view.findViewById<CircleImageView>(R.id.userimage)
        val nickname = view.findViewById<TextView>(R.id.chatlist_nickname)
        val lastmessage = view.findViewById<TextView>(R.id.lastmessage)
        val layoutChatList = view.findViewById<LinearLayout>(R.id.layoutChatList)

    }

}
