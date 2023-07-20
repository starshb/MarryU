package com.example.umarry.chat.detail

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.umarry.databinding.ActivityChatBinding
import com.example.umarry.message.fcm.PushNotification
import com.example.umarry.message.fcm.RetrofitInstance
import com.example.umarry.utils.FirebaseAuthUtils
import com.example.umarry.utils.FirebaseRef
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ChatActivity : AppCompatActivity() {
    private lateinit var binding: ActivityChatBinding
    private val uid = FirebaseAuthUtils.getUid()
    private val ChatList = mutableListOf<ChatDataModel>()
//    private lateinit var topic

//firebaseUser = uid
//userId = otherUid
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChatBinding.inflate(layoutInflater)
        setContentView(binding.root)

        var otherUid = intent.getStringExtra("otherUid")
        var otherNickname = intent.getStringExtra("otherNickname")
        binding.chatView.layoutManager = LinearLayoutManager(this,
            RecyclerView.VERTICAL,false)


        binding.tbTitle.text = otherNickname

        binding.backBtn.setOnClickListener {
            super.onBackPressed()
        }

//        FirebaseRef.memberRef.child(otherUid!!).addValueEventListener(object:ValueEventListener{
//            override fun onDataChange(snapshot: DataSnapshot) {
//                val image = findViewById<ImageView>(R.id.chat_userimage)
//                val user = snapshot.getValue(UserDataModel::class.java)
////                val storageRef = Firebase.storage.reference.child(user!!.uid!!).child("01.png")
////                storageRef.downloadUrl.addOnCompleteListener(OnCompleteListener { task ->
////                    if(task.isSuccessful){
////                        Glide.with(this@ChatActivity)
////                            .load(task.result)
////                            .into(image) //image 가 null이라고 에러
////                    }
////                })
//            }
//
//            override fun onCancelled(error: DatabaseError) {
//            }
//        })

        binding.sendButton.setOnClickListener {
            var message = binding.messageEdit.text.toString()

            if(message.isNotEmpty()){
                binding.sendButton.visibility = View.VISIBLE
                sendMessage(uid,otherUid,message)
                binding.messageEdit.setText("")
//                topic = "topics/$otherUid"
//                PushNotification(NotiModel("title",message),topic).also {
//                    MPush(it)
//                }

            }else{
                binding.sendButton.visibility = View.GONE
            }
        }

        readChatList(uid,otherUid)

    }

    fun readChatList(senderId: String, receiverId: String?){
        FirebaseRef.chatRef.addValueEventListener(object :ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                ChatList.clear()

                for(dataSnapShot:DataSnapshot in snapshot.children){
                    val chat = dataSnapShot.getValue(ChatDataModel::class.java)
                    if(chat!!.senderId.equals(senderId)&&chat.receiverId.equals(receiverId)||
                        chat!!.senderId.equals(receiverId)&&chat.receiverId.equals(senderId)    ){
                        ChatList.add(chat)
                    }
                }
                val chatAdapter = ChatAdapter(this@ChatActivity,ChatList)
                binding.chatView.adapter = chatAdapter

            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(applicationContext,error.message,Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun sendMessage(senderId: String, receiverId: String?, message: String){
        var hashMap:HashMap<String,String> = HashMap()
        hashMap.put("senderId",senderId)
        hashMap.put("receiverId",receiverId!!)
        hashMap.put("message",message)

        FirebaseRef.chatRef.push().setValue(hashMap)
    }

    private fun MPush(notification: PushNotification) = CoroutineScope(Dispatchers.IO).launch {
        try{
            val response = RetrofitInstance.api.postNotification(notification)
            if(response.isSuccessful){
                Toast.makeText(this@ChatActivity, "Response ${Gson().toJson(response)}",Toast.LENGTH_SHORT).show()
            }else{
                Toast.makeText(this@ChatActivity, response.errorBody().toString(),Toast.LENGTH_SHORT).show()
            }
        }catch (e:Exception){
            Toast.makeText(this@ChatActivity, e.message,Toast.LENGTH_SHORT).show()
        }

    }
}
