package com.example.umarry.chat

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.umarry.auth.UserDataModel
import com.example.umarry.databinding.ActivityChatListBinding
import com.example.umarry.setting.MyPageActivity
import com.example.umarry.setting.SettingActivity
import com.example.umarry.utils.FirebaseAuthUtils
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class ChatListActivity : AppCompatActivity() {
    private lateinit var binding: ActivityChatListBinding
    private val userChatList = mutableListOf<UserDataModel>()
    private var uid = FirebaseAuthUtils.getUid()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChatListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.backBtn.setOnClickListener {
            super.onBackPressed()
        }
        binding.tbMypage.setOnClickListener {
            startActivity(Intent(this,SettingActivity::class.java))
        }

        binding.chatroomListView.layoutManager = LinearLayoutManager(this,
            RecyclerView.VERTICAL,false)

        getChatList()

    }

    fun getChatList(){
        var databaseReference:DatabaseReference = FirebaseDatabase.getInstance().getReference("member")

        databaseReference.addValueEventListener(object :ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                userChatList.clear()

                for(dataSnapShot:DataSnapshot in snapshot.children){
                    val user = dataSnapShot.getValue(UserDataModel::class.java)
                    if(!user!!.uid.toString().equals(uid)){
                        userChatList.add(user)
                    }
                }
                val chatListAdapter = UserChatListAdapter(this@ChatListActivity,userChatList)
                binding.chatroomListView.adapter = chatListAdapter

            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(applicationContext,error.message,Toast.LENGTH_SHORT).show()
            }
        })
    }

}