package com.example.umarry.message

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.BaseAdapter
import android.widget.Toast
import com.example.umarry.R
import com.example.umarry.auth.UserDataModel
import com.example.umarry.databinding.ActivityMyLikeListBinding
import com.example.umarry.message.fcm.NotiModel
import com.example.umarry.message.fcm.PushNotification
import com.example.umarry.message.fcm.RetrofitInstance
import com.example.umarry.utils.FirebaseAuthUtils
import com.example.umarry.utils.FirebaseRef
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MyLikeListActivity : AppCompatActivity() {
    private lateinit var binding:ActivityMyLikeListBinding
    private val uid = FirebaseAuthUtils.getUid()
    private val likeUserUid = mutableListOf<String>()
    private val likeUserList = mutableListOf<UserDataModel>()
    lateinit var listviewAdapter: ListViewAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMyLikeListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        listviewAdapter = ListViewAdapter(this, likeUserList)
        binding.userListView.adapter = listviewAdapter

        getLikeList()
        binding.userListView.setOnItemClickListener { parent, view, position, id ->
            matchingCheck(likeUserList[position].uid.toString())


            //todo 내용수정
            val notimodel = NotiModel("a","B")
            val pushmodel = PushNotification(notimodel,likeUserList[position].token.toString())
            MPush(pushmodel)
        }


    }

    private fun matchingCheck(otherUid: String){
        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.children.count() == 0){
                    Toast.makeText(this@MyLikeListActivity,"매칭이 되지 않았습니다.",Toast.LENGTH_SHORT).show()
                }else {
                    //todo for문으로 리스트 돌때마다 매칭유무 모두 메시지뜨는거 고치기

                    for(dataModel in dataSnapshot.children){
                        val likeUserKey = dataModel.key.toString()
                        if(likeUserKey.equals(uid)){
                            Toast.makeText(this@MyLikeListActivity,"매칭이 되었습니다.",Toast.LENGTH_SHORT).show()
                        }else{
                            Toast.makeText(this@MyLikeListActivity,"매칭이 되지 않았습니다.",Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Getting Post failed, log a message
                Log.w("xxxxxx userdata", "loadPost:onCancelled", databaseError.toException())
            }
        }
        FirebaseRef.memberLikeRef.child(otherUid).addValueEventListener(postListener)
    }

    private fun getUserDataList(){
        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for(dataModel in dataSnapshot.children){
                    val user = dataModel.getValue(UserDataModel::class.java)
                    if(likeUserUid.contains(user?.uid)){
                        likeUserList.add(user!!)

                    }
                }
                Log.d("xxxxxuserlist",likeUserList.toString())
                listviewAdapter.notifyDataSetChanged()
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Getting Post failed, log a message
                Log.w("xxxxxx userdata", "loadPost:onCancelled", databaseError.toException())
            }
        }
        FirebaseRef.memberRef.addValueEventListener(postListener) // 데이터 가져올 경로.addValue~  , post = 가져올 곳 ??
    }


    private fun getLikeList(){
        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {

                for(dataModel in dataSnapshot.children){
                    likeUserUid.add(dataModel.key.toString())
                }
                getUserDataList()
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Getting Post failed, log a message
                Log.w("xxxxxx userdata", "loadPost:onCancelled", databaseError.toException())
            }
        }
        FirebaseRef.memberLikeRef.child(uid).addValueEventListener(postListener)

    }

    private fun MPush(notification:PushNotification) = CoroutineScope(Dispatchers.IO).launch {
        RetrofitInstance.api.postNotification(notification)
    }
}