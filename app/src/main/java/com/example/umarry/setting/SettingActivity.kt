package com.example.umarry.setting

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import com.example.umarry.R
import com.example.umarry.auth.LoginActivity
import com.example.umarry.databinding.ActivitySettingBinding
import com.example.umarry.databinding.ToolbarBinding
import com.example.umarry.message.MyLikeListActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class SettingActivity : AppCompatActivity() {
    private lateinit var binding:ActivitySettingBinding
    private lateinit var tbinding:ToolbarBinding
    private lateinit var auth :FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingBinding.inflate(layoutInflater)
        tbinding = binding.tbSetting
        auth = Firebase.auth

        setContentView(binding.root)

        binding.myPageBtn.setOnClickListener {
            startActivity(Intent(this,MyPageActivity::class.java))
            overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left)
        }
        binding.myLikeList.setOnClickListener {
            startActivity(Intent(this,MyLikeListActivity::class.java))
            overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left)
        }
        binding.SignOutBtn.setOnClickListener {
            if(auth.currentUser !=null){
                Firebase.auth.signOut()
                Toast.makeText(this,"정상적으로 로그아웃했습니다.",Toast.LENGTH_SHORT).show()
                Handler(Looper.getMainLooper()).postDelayed({
                    startActivity(Intent(this,LoginActivity::class.java))
                },1500)

            }

        }
        tbinding.tbTitle.text  = "<"
        tbinding.tbTitle.setOnClickListener {
            onBackPressed()
            overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right)
        }
    }
}