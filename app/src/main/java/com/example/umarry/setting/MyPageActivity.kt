package com.example.umarry.setting

import android.os.Build
import android.os.Build.VERSION_CODES
import android.os.Build.VERSION_CODES.*
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.annotation.RequiresApi
import com.bumptech.glide.Glide
import com.example.umarry.R
import com.example.umarry.auth.UserDataModel
import com.example.umarry.databinding.ActivityMyPageBinding
import com.example.umarry.utils.FirebaseAuthUtils
import com.example.umarry.utils.FirebaseRef
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class MyPageActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMyPageBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMyPageBinding.inflate(layoutInflater)
        setContentView(binding.root)


        getMyData()
    }

    private fun getMyData(){
        val uid = FirebaseAuthUtils.getUid()
        val myName = binding.myName
        val myNickname = binding.myNickname
        val myAge = binding.myAge
        val myArea = binding.myArea
        val myGender = binding.myGender
        val myImg = binding.myImage


        val postListener = object : ValueEventListener {
            @RequiresApi(O)
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                Log.d("xxxxxx mypagedata", dataSnapshot.toString())
                val data = dataSnapshot.getValue(UserDataModel::class.java)
                var date = LocalDate.now()

                val formatter = DateTimeFormatter.ofPattern("yyyy")
                val formatted = date.format(formatter)
                val uid = FirebaseAuthUtils.getUid()

                myName.text = data!!.name
                myNickname.text = data!!.nickname
                myAge.text = (formatted.toInt()-data.birth!!.substring(0,4).toInt()).toString()
                myArea.text = data!!.bigArea +" "+ data!!.smallArea
                myGender.text = data!!.gender

                // mypage여서 현재 사용자의 uid를 get
                val storageRef = Firebase.storage.reference.child(uid).child("01.png")
                storageRef.downloadUrl.addOnCompleteListener(OnCompleteListener { task ->
                    if(task.isSuccessful){
                        Glide.with(baseContext)
                            .load(task.result)
                            .into(myImg)
                    }
                })

            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Getting Post failed, log a message
                Log.w("xxxxxx mypagedata", "loadPost:onCancelled", databaseError.toException())
            }
        }
        FirebaseRef.memberRef.child(uid).addValueEventListener(postListener) // 데이터 가져올 경로.addValue~  , post = 가져올 곳 ??

    }
}