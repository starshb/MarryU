package com.example.umarry

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.umarry.cardstack.NewtodayAdapter
import com.example.umarry.databinding.ActivityMainBinding
import com.example.umarry.utils.FirebaseAuthUtils
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.yuyakaido.android.cardstackview.CardStackLayoutManager
 //전화번호, 이메일 유니크
class MainActivity : AppCompatActivity() {
    private lateinit var binding : ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val user = Firebase.auth.currentUser

        binding.signOut.setOnClickListener {
            if(user != null){
                Firebase.auth.signOut()
                Log.d("MAINxxxxxSign OUt","Sign Out")
            }
        }
        val uid = FirebaseAuthUtils.getUid()
        Log.d("MAINxxxxx uid",uid)



    }
}