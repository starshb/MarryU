package com.example.umarry.auth

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.umarry.R
import com.example.umarry.databinding.ActivityRegister02Binding

class RegisterActivity02:AppCompatActivity() {
    private lateinit var binding: ActivityRegister02Binding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegister02Binding.inflate(layoutInflater)
        setContentView(binding.root)


        binding.nextButton.setOnClickListener {
            // binding
            // todo db 연동 및 계정 추가
            var job = binding.job.text.toString()
            var ability = binding.ability.text.toString()
            var stature = binding.stature.text.toString()
            var bigArea = binding.bigArea.text.toString()
            var smallArea = binding.smallArea.text.toString()
            var yStature = binding.yStature.text.toString()
            var yAbility = binding.yAbility.text.toString()
            var name = intent.getStringExtra("name")
            var nickname = intent.getStringExtra("nickname")
            var gender = intent.getStringExtra("gender")
            var phone = intent.getStringExtra("phone")
            var birth = intent.getStringExtra("birth")
            val email = intent.getStringExtra("email")
            var password = intent.getStringExtra("password")


            if(job.isNotEmpty() && ability.isNotEmpty() && stature.isNotEmpty() && bigArea.isNotEmpty() && smallArea.isNotEmpty() && yStature.isNotEmpty() && yAbility.isNotEmpty()) {
                val intent = Intent(this, RegisterActivity03::class.java)
                intent.putExtra("name",name)
                intent.putExtra("nickname",nickname)
                intent.putExtra("gender",gender)
                intent.putExtra("phone",phone)
                intent.putExtra("birth",birth)
                intent.putExtra("email",email)
                intent.putExtra("password",password)
                intent.putExtra("job",job)
                intent.putExtra("ability",ability)
                intent.putExtra("stature",stature)
                intent.putExtra("bigArea",bigArea)
                intent.putExtra("smallArea",smallArea)
                intent.putExtra("yStature",yStature)
                intent.putExtra("yAbility",yAbility)
                startActivityForResult(intent,200) // activity 이동
                overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left)
                finish()
            } else {
                // If sign in fails, display a message to the user.
                Toast.makeText(
                    this,
                    "모든 항목을 입력하세요.",
                    Toast.LENGTH_SHORT,
                ).show()
            }
        }

        binding.registerHeader.setOnClickListener {
            onBackPressed()
        }

    }

    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right)
    }
}