package com.example.umarry

import android.content.Intent
import android.graphics.Rect
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.MotionEvent
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Toast
import com.example.umarry.cardstack.MatchingActivity
import com.example.umarry.databinding.ActivityMainBinding
import com.example.umarry.databinding.ToolbarBinding
import com.example.umarry.setting.SettingActivity
import com.example.umarry.utils.FirebaseAuthUtils
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.FirebaseMessaging

//전화번호, 이메일 유니크
class MainActivity : AppCompatActivity() {
    private lateinit var binding : ActivityMainBinding
    private lateinit var tbinding : ToolbarBinding  //include Layout 바인딩
    private var isExpanded = false

    private val fromBottomFabAnim: Animation by lazy {
        AnimationUtils.loadAnimation(this, R.anim.from_bottom_fab)
    }
    private val toBottomFabAnim: Animation by lazy {
        AnimationUtils.loadAnimation(this, R.anim.to_bottom_fab)
    }
    private val rotateClockWiseFabAnim: Animation by lazy {
        AnimationUtils.loadAnimation(this, R.anim.rotate_clock_wise)
    }
    private val rotateAntiClockWiseFabAnim: Animation by lazy {
        AnimationUtils.loadAnimation(this, R.anim.rotate_anti_clock_wise)
    }
    private val fromBottomBgAnim: Animation by lazy {
        AnimationUtils.loadAnimation(this, R.anim.from_bottom_anim)
    }
    private val toBottomBgAnim: Animation by lazy {
        AnimationUtils.loadAnimation(this, R.anim.to_bottom_anim)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        tbinding = binding.tbMain
        setContentView(binding.root)
        supportActionBar?.setTitle("") // 제목없음설정
        actionBar?.show()

        val user = Firebase.auth.currentUser

        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                Log.e("TOKENFaild", "Fetching FCM registration token failed", task.exception)
                return@OnCompleteListener
            }
            // Get new FCM registration token
            val token = task.result

            Log.e("TOKEN",token )
        })

        tbinding.tbMypage.setOnClickListener {
            startActivity(Intent(this, SettingActivity::class.java))
            overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left)
        }

        val uid = FirebaseAuthUtils.getUid()
        Log.d("MAINxxxxx uid",uid)

        binding.mainFabBtn.setOnClickListener {
            if (isExpanded) {
                shrinkFab()
            } else {
                expandFab()
            }
        }

        binding.chatbotFabBtn.setOnClickListener {
            onGalleryClicked()
        }

        binding.chatbotTv.setOnClickListener {
            onGalleryClicked()
        }

        binding.bottomNavBar.setItemSelected(R.id.nav_home,true)
        setUpTabBar()

    }

    private fun onGalleryClicked() {
        Toast.makeText(this, "Gallery Clicked", Toast.LENGTH_SHORT).show()
    }

    private fun shrinkFab() {
        binding.mainFabBtn.startAnimation(rotateAntiClockWiseFabAnim)
        binding.chatbotFabBtn.startAnimation(toBottomFabAnim)
        binding.settingFabBtn.startAnimation(toBottomFabAnim)
        binding.callFabBtn.startAnimation(toBottomFabAnim)
        binding.chatbotTv.startAnimation(toBottomFabAnim)
        binding.settingTv.startAnimation(toBottomFabAnim)
        binding.callTv.startAnimation(toBottomFabAnim)

        isExpanded = !isExpanded
    }

    private fun expandFab() {
        binding.mainFabBtn.startAnimation(rotateClockWiseFabAnim)
        binding.chatbotFabBtn.startAnimation(fromBottomFabAnim)
        binding.settingFabBtn.startAnimation(fromBottomFabAnim)
        binding.callFabBtn.startAnimation(fromBottomFabAnim)
        binding.chatbotTv.startAnimation(fromBottomFabAnim)
        binding.settingTv.startAnimation(fromBottomFabAnim)
        binding.callTv.startAnimation(fromBottomFabAnim)

        isExpanded = !isExpanded
    }

    override fun onBackPressed() {
        if (isExpanded) {
            shrinkFab()
        } else {
            super.onBackPressed()
        }
    }

    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        if (ev?.action == MotionEvent.ACTION_DOWN) {
            if (isExpanded) {
                val outRect = Rect()
                binding.fabConstraint.getGlobalVisibleRect(outRect)
                if (!outRect.contains(ev.rawX.toInt(), ev.rawY.toInt())) {
                    shrinkFab()
                }
            }
        }
        return super.dispatchTouchEvent(ev)
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
                    Handler(Looper.getMainLooper()).postDelayed({
                        startActivity(Intent(this,MatchingActivity::class.java))
                    },250)

//                    overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left)
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