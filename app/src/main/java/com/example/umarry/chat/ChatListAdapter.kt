package com.example.umarry.chat

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.umarry.R
import com.example.umarry.auth.UserDataModel
import de.hdodenhof.circleimageview.CircleImageView

class ChatListAdapter(private val context: Context, private val items: MutableList<ChatListDataModel>):RecyclerView.Adapter<ChatListAdapter.ViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatListAdapter.ViewHolder {
        val view:View = LayoutInflater.from(parent.context).inflate(R.layout.itemchatlist,parent,false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ChatListAdapter.ViewHolder, position: Int) {
        val item = items[position]
        holder.usernickname.text = item.otherUserName
        Glide.with(context).load(item.otherUserImage).into(holder.userimage)

//        val storageRef = Firebase.storage.reference.child(data.otherUserId!!).child("01.png")
//            storageRef.downloadUrl.addOnCompleteListener(OnCompleteListener { task ->
//                if (task.isSuccessful) {
//                    Glide.with(context)
//                        .load(task.result)
//                        .into(binding.userimage)
//                }
//            })
    }

    override fun getItemCount(): Int {
        return items.size
    }

    inner class ViewHolder(view:View) : RecyclerView.ViewHolder(view){
        val userimage = view.findViewById<CircleImageView>(R.id.userimage)
        val usernickname = view.findViewById<TextView>(R.id.chatlist_nickname)
        val lastmessage = view.findViewById<TextView>(R.id.lastmessage)
    }

}

//class ChatListAdapter(val context: Context,private val onClick: (ChatListDataModel) -> Unit) : ListAdapter<ChatListDataModel, ChatListAdapter.ViewHolder>(differ) {
//
//    inner class ViewHolder(private val binding: ItemchatlistBinding) :
//        RecyclerView.ViewHolder(binding.root) {
//
//        fun bind(data: ChatListDataModel) {
//            val storageRef = Firebase.storage.reference.child(data.otherUserId!!).child("01.png")
//            storageRef.downloadUrl.addOnCompleteListener(OnCompleteListener { task ->
//                if (task.isSuccessful) {
//                    Glide.with(context)
//                        .load(task.result)
//                        .into(binding.userimage)
//                }
//            })
//            binding.chatlistNickname.text = data.otherUserName
//            binding.lastmessage.text = data.lastMessage
//
//            binding.root.setOnClickListener {
//                onClick(data)
//            }
//        }
//    }
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
//        return ViewHolder(
//            ItemchatlistBinding.inflate(
//                LayoutInflater.from(parent.context),
//                parent,
//                false
//            )
//        )
//    }
//
//    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
//        holder.bind(currentList[position])
//    }
//
//    companion object {
//        val differ = object : DiffUtil.ItemCallback<ChatListDataModel>() {
//            override fun areItemsTheSame(oldItem: ChatListDataModel, newItem: ChatListDataModel): Boolean {
//                return oldItem.chatRoomId == newItem.chatRoomId
//            }
//
//            @SuppressLint("DiffUtilEquals")
//            override fun areContentsTheSame(oldItem: ChatListDataModel, newItem: ChatListDataModel): Boolean {
//                return oldItem == newItem
//            }
//
//        }
//    }
//}