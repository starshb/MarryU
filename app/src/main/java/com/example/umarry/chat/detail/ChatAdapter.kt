package com.example.umarry.chat.detail

import android.annotation.SuppressLint
import android.view.Gravity
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.umarry.auth.UserDataModel
import com.example.umarry.databinding.ItemchatBinding

class ChatAdapter: ListAdapter<ChatDataModel, ChatAdapter.ViewHolder>(differ) {

    var otherUserItem: UserDataModel? = null

    inner class ViewHolder(private val binding: ItemchatBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: ChatDataModel) {
            if (item.uid == otherUserItem?.uid) {
                binding.usernicknameText.isVisible = true
                binding.usernicknameText.text = otherUserItem?.name
                binding.messageText.text = item.message
                binding.messageText.gravity = Gravity.START
            } else {
                binding.usernicknameText.isVisible = false
                binding.messageText.text = item.message
                binding.messageText.gravity = Gravity.END
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemchatBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(currentList[position])
    }

    companion object {
        val differ = object : DiffUtil.ItemCallback<ChatDataModel>() {
            override fun areItemsTheSame(oldItem: ChatDataModel, newItem: ChatDataModel): Boolean {
                return oldItem.chatId == newItem.chatId
            }

            @SuppressLint("DiffUtilEquals")
            override fun areContentsTheSame(oldItem: ChatDataModel, newItem: ChatDataModel): Boolean {
                return oldItem == newItem
            }

        }
    }
}