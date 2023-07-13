package com.example.umarry.chat

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.umarry.R
import com.example.umarry.databinding.ActivityChatListBinding

class ChatListActivity : AppCompatActivity() {
    private lateinit var binding: ActivityChatListBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChatListBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}