package com.example.umarry.chat

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.umarry.R
import com.example.umarry.databinding.ActivityChatbotBinding
import com.example.umarry.utils.BotResponse
import com.example.umarry.utils.Constants.OPEN_GOOGLE
import com.example.umarry.utils.Constants.OPEN_SEARCH
import com.example.umarry.utils.Constants.RECEIVE_ID
import com.example.umarry.utils.Constants.SEND_ID
import com.example.umarry.utils.Time
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ChatbotActivity : AppCompatActivity() {
    private var messagesList = mutableListOf<ChatBotModel>()
    private lateinit var adapter:ChatbotAdapter

    private lateinit var binding: ActivityChatbotBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChatbotBinding.inflate(layoutInflater)
        setContentView(binding.root)

        recyclerView()

        clickEvents()

        val random = (0..3).random()
        customBotMessage("안녕하세요! 문의를 도와드릴 하트입니다, 무엇을 도와드릴까요?")
    }

    private fun clickEvents() {

        binding.backBtn.setOnClickListener {
            super.onBackPressed()
            overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right)
        }


        binding.sendButton.setOnClickListener {
            sendMessage()
        }


        binding.messageEdit.setOnClickListener {
            GlobalScope.launch {
                delay(100)

                withContext(Dispatchers.Main) {
                    binding.chatbotView.scrollToPosition(adapter.itemCount - 1)

                }
            }
        }
    }

    private fun recyclerView() {
        adapter = ChatbotAdapter()
        binding.chatbotView.adapter = adapter
        binding.chatbotView.layoutManager = LinearLayoutManager(applicationContext)

    }

    override fun onStart() {
        super.onStart()
        GlobalScope.launch {
            delay(100)
            withContext(Dispatchers.Main) {
                binding.chatbotView.scrollToPosition(adapter.itemCount - 1)
            }
        }
    }

    private fun sendMessage() {
        val message = binding.messageEdit.text.toString()
        val timeStamp = Time.timeStamp()

        if (message.isNotEmpty()) {
            messagesList.add(ChatBotModel(message, SEND_ID, timeStamp))
            binding.messageEdit.setText("")

            adapter.insertMessage(ChatBotModel(message, SEND_ID, timeStamp))
            binding.chatbotView.scrollToPosition(adapter.itemCount - 1)

            botResponse(message)
        }
    }

    private fun botResponse(message: String) {
        val timeStamp = Time.timeStamp()

        GlobalScope.launch {
            delay(1000)

            withContext(Dispatchers.Main) {
                val response = BotResponse.basicResponses(message)


                messagesList.add(ChatBotModel(response, RECEIVE_ID, timeStamp))


                adapter.insertMessage(ChatBotModel(response, RECEIVE_ID, timeStamp))


                binding.chatbotView.scrollToPosition(adapter.itemCount - 1)


                when (response) {
                    OPEN_GOOGLE -> {
                        val site = Intent(Intent.ACTION_VIEW)
                        site.data = Uri.parse("https://www.google.com/")
                        startActivity(site)
                    }
                    OPEN_SEARCH -> {
                        val site = Intent(Intent.ACTION_VIEW)
                        val searchTerm: String?
                        if(message.contains("search")){
                            searchTerm = message.substringAfterLast("search")
                        }else{
                            searchTerm = message.substringBeforeLast("검색")
                        }
                        site.data = Uri.parse("https://www.google.com/search?&q=$searchTerm")
                        startActivity(site)
                    }

                }
            }
        }
    }

    private fun customBotMessage(message: String) {

        GlobalScope.launch {
            delay(1000)
            withContext(Dispatchers.Main) {
                val timeStamp = Time.timeStamp()
                messagesList.add(ChatBotModel(message, RECEIVE_ID, timeStamp))
                adapter.insertMessage(ChatBotModel(message, RECEIVE_ID, timeStamp))

                binding.chatbotView.scrollToPosition(adapter.itemCount - 1)
            }
        }
    }
}