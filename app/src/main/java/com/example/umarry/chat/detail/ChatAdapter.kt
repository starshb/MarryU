package com.example.umarry.chat.detail

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
import com.example.umarry.utils.FirebaseAuthUtils
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import de.hdodenhof.circleimageview.CircleImageView

class ChatAdapter (private val context: Context, private val items: MutableList<ChatDataModel>):
    RecyclerView.Adapter<ChatAdapter.ViewHolder>(){

    private val MESSAGE_LEFT = 0
    private val MESSAGE_RIGHT = 1
    private var uid = FirebaseAuthUtils.getUid()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatAdapter.ViewHolder {
        if (viewType == MESSAGE_LEFT){
            val view: View = LayoutInflater.from(parent.context).inflate(R.layout.itemchat_left,parent,false)
            return ViewHolder(view)
        }else{
            val view: View = LayoutInflater.from(parent.context).inflate(R.layout.itemchat_right,parent,false)
            return ViewHolder(view)
        }
    }

    override fun onBindViewHolder(holder: ChatAdapter.ViewHolder, position: Int) {
        val item = items[position]
        holder.chatMessage.text = item.message
//        Glide.with(context).load(item.uid).placeholder(R.drawable.ic_person).into(holder.chatUserimage)

//누구에게 어떤 이미지 넣어줄지
        if(items[position].senderId == uid){
            val storageRef = Firebase.storage.reference.child(item.senderId!!).child("01.png")
            storageRef.downloadUrl.addOnCompleteListener(OnCompleteListener { task ->
                if(task.isSuccessful){
                    Glide.with(context)
                        .load(task.result)
                        .into(holder.chatUserimageRight)
                }
            })
        }else{
            val storageRef = Firebase.storage.reference.child(item.senderId!!).child("01.png")
            storageRef.downloadUrl.addOnCompleteListener(OnCompleteListener { task ->
                if(task.isSuccessful){
                    Glide.with(context)
                        .load(task.result)
                        .into(holder.chatUserimageLeft)
                }
            })
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view){
        val chatUserimageLeft = view.findViewById<CircleImageView>(R.id.chat_userimage_left)
        val chatUserimageRight = view.findViewById<CircleImageView>(R.id.chat_userimage_right)
        val chatMessage = view.findViewById<TextView>(R.id.message_text)

    }

    override fun getItemViewType(position: Int): Int {
        if(items[position].senderId == uid){
            return MESSAGE_RIGHT
        }else{
            return MESSAGE_LEFT
        }
    }

}
