package com.example.umarry.chat.detail

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.example.umarry.R
import com.example.umarry.auth.UserDataModel
import com.example.umarry.databinding.ActivityChatBinding
import com.example.umarry.message.fcm.Repo
import com.example.umarry.utils.FirebaseAuthUtils
import com.example.umarry.utils.FirebaseRef
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import okhttp3.Call
import okhttp3.Callback
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.Response
import org.json.JSONObject
import java.io.IOException

class ChatActivity : AppCompatActivity() {
    private lateinit var binding: ActivityChatBinding
    private val uid = FirebaseAuthUtils.getUid()
    private lateinit var reference: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChatBinding.inflate(layoutInflater)
        setContentView(binding.root)

        var otherUid = intent.getStringExtra("otherUid")
        var otherNickname = intent.getStringExtra("otherNickname")
        reference = FirebaseDatabase.getInstance().getReference("member").child(uid)



        binding.tbTitle.text = otherNickname

        binding.backBtn.setOnClickListener {
            super.onBackPressed()
        }

//        reference.addValueEventListener()
        FirebaseRef.memberRef.child(otherUid!!).addValueEventListener(object:ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                val image = findViewById<ImageView>(R.id.chat_userimage)
                val user = snapshot.getValue(UserDataModel::class.java)
//                val storageRef = Firebase.storage.reference.child(user!!.uid!!).child("01.png")
//                storageRef.downloadUrl.addOnCompleteListener(OnCompleteListener { task ->
//                    if(task.isSuccessful){
//                        Glide.with(this@ChatActivity)
//                            .load(task.result)
//                            .into(image)
//                    }
//                })
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }
}
//    private lateinit var binding: ActivityChatBinding
//    private lateinit var chatAdapter: ChatAdapter
//    private lateinit var linearLayoutManager:LinearLayoutManager
//
//    private var chatRoomId: String = ""
//    private var otherUserId: String = ""
//    private var otherUserFcmToken: String = ""
//    private var myUserId: String = ""
//    private var myUserNickname: String = ""
//    private var isInit = false
//
//    private val chatItemList = mutableListOf<ChatDataModel>()
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        binding = ActivityChatBinding.inflate(layoutInflater)
//        setContentView(binding.root)
//
//        chatRoomId = intent.getStringExtra(EXTRA_CHAT_ROOM_ID) ?: return
//        otherUserId = intent.getStringExtra(EXTRA_OTHER_USER_ID) ?: return
//        myUserId = Firebase.auth.currentUser?.uid ?: ""
//        chatAdapter = ChatAdapter()
//        linearLayoutManager = LinearLayoutManager(applicationContext)
//
//        Firebase.database.reference.child("member").child(myUserId).get()
//            .addOnSuccessListener {
//                val myUserItem = it.getValue(UserDataModel::class.java)
//                myUserNickname = myUserItem?.nickname ?: ""
//
//                getOtherUserData()
//            }
//
//        binding.chatView.apply {
//            layoutManager = linearLayoutManager
//            adapter = chatAdapter
//        }
//
//        chatAdapter.registerAdapterDataObserver(object : RecyclerView.AdapterDataObserver() {
//            override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
//                super.onItemRangeInserted(positionStart, itemCount)
//
//                linearLayoutManager.smoothScrollToPosition(
//                    binding.chatView,
//                    null,
//                    chatAdapter.itemCount
//                )
//            }
//        })
//
//        binding.sendButton.setOnClickListener {
//            val message = binding.messageEdit.text.toString()
//
//            if (!isInit) {
//                return@setOnClickListener
//            }
//
//            if (message.isEmpty()) {
//                Toast.makeText(applicationContext, "빈 메시지를 전송할 수는 없습니다.", Toast.LENGTH_SHORT).show()
//                return@setOnClickListener
//            }
//
//            val newChatItem = ChatDataModel(
//                message = message,
//                uid = myUserId
//            )
//
//            Firebase.database.reference.child("chats").child(chatRoomId).push().apply {
//                newChatItem.chatId = key
//                setValue(newChatItem)
//            }
//
//            val updates: MutableMap<String, Any> = hashMapOf(
//                "chatRooms/$myUserId/$otherUserId/lastMessage" to message,
//                "chatRooms/$otherUserId/$myUserId/lastMessage" to message,
//                "chatRooms/$otherUserId/$myUserId/chatRoomId" to chatRoomId,
//                "chatRooms/$otherUserId/$myUserId/otherUserId" to myUserId,
//                "chatRooms/$otherUserId/$myUserId/otherUserName" to myUserNickname,
//            )
//            Firebase.database.reference.updateChildren(updates)
//
//            val client = OkHttpClient()
//
//            val root = JSONObject()
//            val notification = JSONObject()
//            notification.put("title", getString(R.string.app_name))
//            notification.put("body", message)
//
//            root.put("to", otherUserFcmToken)
//            root.put("priority", "high")
//            root.put("notification", notification)
//
//            val requestBody =
//                root.toString().toRequestBody("application/json; charset=utf-8".toMediaType())
//            val request =
//                Request.Builder().post(requestBody).url("https://fcm.googleapis.com/fcm/send")
//                    .header("Authorization", "key=${Repo.SERVER_KEY}").build()
//            client.newCall(request).enqueue(object : Callback {
//                override fun onFailure(call: Call, e: IOException) {
//                    e.stackTraceToString()
//                }
//
//                override fun onResponse(call: Call, response: Response) {
//                    // ignore onResponse
//                }
//
//            })
//
//            binding.messageEdit.text.clear()
//        }
//    }
//
//    private fun getOtherUserData() {
//        Firebase.database.reference.child("member").child(otherUserId).get()
//            .addOnSuccessListener {
//                val otherUserItem = it.getValue(UserDataModel::class.java)
//                otherUserFcmToken = otherUserItem?.token.orEmpty()
//                chatAdapter.otherUserItem = otherUserItem
//
//                isInit = true
//                getChatData()
//            }
//    }
//
//    private fun getChatData() {
//        Firebase.database.reference.child("chat").child(chatRoomId)
//            .addChildEventListener(object : ChildEventListener {
//                override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
//                    val chatItem = snapshot.getValue(ChatDataModel::class.java)
//                    chatItem ?: return
//                    chatItemList.add(chatItem)
//                    chatAdapter.submitList(chatItemList.toMutableList())
//                }
//
//                override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {}
//
//                override fun onChildRemoved(snapshot: DataSnapshot) {}
//
//                override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {}
//
//                override fun onCancelled(error: DatabaseError) {}
//
//            })
//    }
//
//    companion object {
//        const val EXTRA_CHAT_ROOM_ID = "CHAT_ROOM_ID"
//        const val EXTRA_OTHER_USER_ID = "OTHER_USER_ID"
//    }
