package com.example.umarry.auth

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.umarry.R
import com.example.umarry.databinding.ActivityRegister01Binding

class RegisterActivity01:AppCompatActivity() {
    private lateinit var binding: ActivityRegister01Binding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegister01Binding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.nextButton.setOnClickListener {
            // todo db 연동 및 계정 추가
            var name = binding.name.text.toString()
            var nickname = binding.nickname.text.toString()
            var gender = binding.gender.text.toString()
            var phone = binding.phone.text.toString()
            var birth = binding.birth.text.toString()
            val email = binding.registerEmail.text.toString()
            var password = binding.registerPassword.text.toString()

            if(name.isNotEmpty() && nickname.isNotEmpty() && gender.isNotEmpty() && phone.isNotEmpty() && birth.isNotEmpty() && email.isNotEmpty() && password.isNotEmpty()) {
                val intent = Intent(this, RegisterActivity02::class.java)
                intent.putExtra("name",name)
                intent.putExtra("nickname",nickname)
                intent.putExtra("gender",gender)
                intent.putExtra("phone",phone)
                intent.putExtra("birth",birth)
                intent.putExtra("email",email)
                intent.putExtra("password",password)
                startActivityForResult(intent,200) // activity 이동
                overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left)
                            finish()
            } else {
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