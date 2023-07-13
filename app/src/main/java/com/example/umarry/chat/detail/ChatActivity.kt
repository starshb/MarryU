package com.example.umarry.chat.detail

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.umarry.R
import com.example.umarry.databinding.ActivityChatBinding

class ChatActivity : AppCompatActivity() {
    private lateinit var binding: ActivityChatBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChatBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}