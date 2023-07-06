package com.example.umarry

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.umarry.cardstack.NewtodayActivity
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
        supportActionBar?.setTitle("") // 제목없음설정
        actionBar?.show()

        val user = Firebase.auth.currentUser

//        binding.signOut.setOnClickListener {
//            if(user != null){
//                Firebase.auth.signOut()
//                Log.d("MAINxxxxxSign OUt","Sign Out")
//            }
//        }

        val uid = FirebaseAuthUtils.getUid()
        Log.d("MAINxxxxx uid",uid)

        setUpTabBar()


    }
    private fun setUpTabBar() {
        binding.bottomNavBar.setOnItemSelectedListener {
            when (it) {
                R.id.nav_news -> binding.textMain.text = "Near"
                R.id.nav_home -> {
                    startActivity(Intent(this,MainActivity::class.java))
                    binding.textMain.text = "Chat"
                }
                R.id.nav_matching -> {
                    startActivity(Intent(this,NewtodayActivity::class.java))
                    binding.textMain.text = "Profile"
//                     binding.bottomNavBar.showBadge(R.id.nav_settings)
                }

                R.id.nav_store -> {
                    binding.textMain.text = "Settings"
//                     binding.bottomNavBar.dismissBadge(R.id.nav_settings)
                }
            }

        }
    }
}