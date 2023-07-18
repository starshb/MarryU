package com.example.umarry.setting

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
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
//    private lateinit var tbinding:ToolbarBinding
    private lateinit var auth :FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingBinding.inflate(layoutInflater)
//        tbinding = binding.tbSetting
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

        binding.secondAuth.setOnClickListener {
            if (isBiometricSupported()) {
                showBiometricPrompt()
            } else {
                // 실패시 처리 이벤트 필요시 작성
            }
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

        binding.tbTitle.text  = "<"
        binding.tbTitle.setOnClickListener {
            onBackPressed()
            overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right)
        }
    }

    private fun showBiometricPrompt() {
        val promptInfo = BiometricPrompt.PromptInfo.Builder()
            .setTitle("Biometric Authentication")
            .setSubtitle("Log in using your biometric credential")
            .setNegativeButtonText("Cancel")
            .build()

        val biometricPrompt = BiometricPrompt(this, ContextCompat.getMainExecutor(this),
            object : BiometricPrompt.AuthenticationCallback() {
                override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                    super.onAuthenticationError(errorCode, errString)
                    showMessage("Authentication error: $errString")
                }

                override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                    super.onAuthenticationSucceeded(result)
                    showMessage("Authentication succeeded!")
                }

                override fun onAuthenticationFailed() {
                    super.onAuthenticationFailed()
                    showMessage("Authentication failed.")
                }
            })

        biometricPrompt.authenticate(promptInfo)
    }

    private fun showMessage(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun isBiometricSupported(): Boolean {
        val biometricManager = BiometricManager.from(this)
        val canAuthenticate = biometricManager.canAuthenticate(BiometricManager.Authenticators.BIOMETRIC_WEAK)
        when (canAuthenticate) {
            BiometricManager.BIOMETRIC_SUCCESS -> {
                return true
            }
            BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE, BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE, BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED -> {
                return false
            }
            else -> {
                return false
            }
        }
    }
}